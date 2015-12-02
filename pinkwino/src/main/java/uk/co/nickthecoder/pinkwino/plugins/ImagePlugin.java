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

package uk.co.nickthecoder.pinkwino.plugins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Dependency;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Displays an image, with the option of floating the element to either side of
 * the page, with a caption, and a link when the image is clicked.
 */

public class ImagePlugin extends JspPlugin
{

    protected static Logger _logger = LogManager.getLogger(ImagePlugin.class);

    public ImagePlugin()
    {
        this("image");
    }

    public ImagePlugin(String name)
    {
        this(name, "image.jsp");

    }

    public ImagePlugin(String name, String jspPath)
    {
        super(name, jspPath, BODY_TYPE_WIKI);

        addParameterDescription(ParameterDescription.find("page").required());
        addParameterDescription(ParameterDescription.find("float").defaultValue("none"));
        addParameterDescription(ParameterDescription.find("align").defaultValue("center"));
        addParameterDescription(ParameterDescription.find("renderLink"));
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        Parameter pageParameter = parameters.getParameter("page");

        WikiName wikiName = pluginSupport.parseWikiName(pageParameter.getValue());

        pluginSupport.getWikiDocument().addDependency(wikiName, Dependency.DEPENDENCY_ATTACH);

        return super.doBegin(pluginSupport, parameters);
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        if (parameters.getParameter("renderLink") == null) {

            Object staticObject = WikiEngine.instance().getAttributes().getAttribute("wiki_staticSite");

            if (staticObject != null) {
                parameters.addParameter(new Parameter("renderLink", staticObject.toString().equals("true") ? "false"
                                : "true"));
            }

        }

        Parameter pageParameter = parameters.getParameter("page");

        WikiName wikiName = WikiEngine.instance().getWikiNameFormat()
                        .parse(wikiDocument.getVersion().getWikiPage().getNamespace(), pageParameter.getValue());
        jspNode.setRequestAttribute("imageWikiPage", wikiName.getWikiPage());

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}
