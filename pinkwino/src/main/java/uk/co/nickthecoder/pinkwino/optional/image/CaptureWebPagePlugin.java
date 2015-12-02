/* 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*/

package uk.co.nickthecoder.pinkwino.optional.image;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.commands.Command;
import uk.co.nickthecoder.pinkwino.commands.CommandProcessor;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.plugins.JspPlugin;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class CaptureWebPagePlugin extends JspPlugin
{

    protected static Logger _logger = LogManager.getLogger(CaptureWebPagePlugin.class);

    public static final int DEFAULT_WIDTH = 800;

    public static final int DEFAULT_HEIGHT = 600;

    private String _script;

    public CaptureWebPagePlugin(String script)
    {
        this(script, "captureWebPage", "captureWebPage.jsp");
    }

    public CaptureWebPagePlugin(String script, int width, int height)
    {
        this(script, "captureWebPage", "captureWebPage.jsp", width, height);
    }

    public CaptureWebPagePlugin(String script, String name, String jspPath)
    {
        this(script, name, jspPath, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public CaptureWebPagePlugin(String script, String name, String jspPath, int width, int height)
    {
        super(name, jspPath, BODY_TYPE_NONE);

        _script = script;

        addParameterDescription(ParameterDescription.find("page").required());
        addParameterDescription(ParameterDescription.find("log"));
        addParameterDescription(ParameterDescription.find("url").required());
        addParameterDescription(ParameterDescription.find("label").defaultValue("Capture Web Page"));
        addParameterDescription(ParameterDescription.find("width").defaultValue("" + width));
        addParameterDescription(ParameterDescription.find("height").defaultValue("" + height));
    }

    public void preRender(JspNode jspNode)
    {
        Parameters parameters = jspNode.getParameters();

        Parameter pageParameter = parameters.getParameter("page");

        WikiPage wikiPage = WikiEngine.instance().getWikiPage(pageParameter.getValue());

        jspNode.setRequestAttribute("captureWebPage_url", getUrl());
        jspNode.setRequestAttribute("captureWebPage_key", wikiPage.getWikiName().getFormatted());

        if (wikiPage.canEdit()) {

            // If this is a post from this plugin, then do the post processing
            // (i.e. make the static site using wget).
            if ((!wikiPage.getExists())
                            || wikiPage.getWikiName().getFormatted()
                                            .equals(getRequest().getParameter("captureWebPage_key"))) {

                Parameter urlParameter = parameters.getParameter("url");
                Parameter widthParameter = parameters.getParameter("width");
                Parameter heightParameter = parameters.getParameter("height");
                Parameter logParameter = parameters.getParameter("log");

                WikiPage logPage = logParameter == null ? null : WikiEngine.instance().getWikiPage(
                                logParameter.getValue());

                captureWebPage(wikiPage.getWikiName(), urlParameter.getValue(), widthParameter.getValue(),
                                heightParameter.getValue(), logPage);
            }

        }

    }

    public void captureWebPage(WikiName wikiName, String url, String width, String height, WikiPage logPage)
    {
        try {
            File outputFile = File.createTempFile("CaptureWebPagePlugin", ".png");

            /*
             * String[] commandArray = new String[] { _khtml2png, "--width",
             * width, "--height", height, url, outputFile.getPath() };
             */
            String[] commandArray = new String[] { _script, "-g", width, height, "-o", outputFile.getPath(), url };

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < commandArray.length; i++) {
                sb.append("'").append(commandArray[i]).append("' ");
            }
            _logger.error("Running command : " + sb.toString());

            Command command = new Command();
            command.setCommandArray(commandArray);

            CommandProcessor cp = new CaptureWebPageCommandProcessor(command, logPage, wikiName, outputFile);

            cp.start();

        } catch (Exception e) {
            _logger.error("CaptureWebPagePlugin failed");
            _logger.error(e);
        }
    }

    class CaptureWebPageCommandProcessor extends CommandProcessor
    {
        private WikiName _wikiName;
        private File _file;

        public CaptureWebPageCommandProcessor(Command command, WikiPage logPage, WikiName wikiName, File file)
        {
            super(command, logPage);
            _wikiName = wikiName;
            _file = file;
        }

        public void finished(int exitStatus)
        {
            super.finished(exitStatus);
            String content;

            WikiPage wikiPage = WikiEngine.instance().getWikiPage(_wikiName);

            if (wikiPage.getExists()) {
                content = wikiPage.getCurrentVersion().getContent();
            } else {
                content = "Auto generated by the captureWebPage plugin\n";
            }

            WikiEngine.instance().save(wikiPage, content, _file);
            _file.delete();
        }
    }

}
