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

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class MakeStaticPlugin extends JspPlugin
{

    protected static Logger _logger = LogManager.getLogger(MakeStaticPlugin.class);

    protected static String _defaultWgetPath = "/usr/bin/wget";

    /**
     * The full path to the wget command
     */
    protected String _wget;

    protected File _directory;

    protected WikiName _startPageName;

    protected String _oldBaseUrl;

    /**
     * This is the base url for the new web site - it is used for any absolute
     * url's. It is a shame that this is needed, however, when using PayPal, we
     * need to give them a complete url for the cancel and return addresses, and
     * wget cannot magically convert this for us.
     */
    protected String _newBaseUrl;

    public MakeStaticPlugin(File directory, WikiName startPageName, String newBaseUrl)
    {
        this(_defaultWgetPath, directory, startPageName, newBaseUrl);
    }

    public MakeStaticPlugin(String wget, File directory, WikiName startPageName, String newBaseUrl)
    {
        this(wget, "makeStatic", "makeStatic.jsp", directory, startPageName, newBaseUrl);
    }

    public MakeStaticPlugin(String wget, String name, String jspPath, File directory, WikiName startPageName,
                    String newBaseUrl)
    {
        super(name, jspPath, BODY_TYPE_NONE);

        _wget = wget;
        _directory = directory;
        _startPageName = startPageName;
        _newBaseUrl = newBaseUrl;
        _oldBaseUrl = null;

        addParameterDescription(ParameterDescription.find("log").required());
        addParameterDescription(ParameterDescription.find("label").required());
    }

    public void preRender(JspNode jspNode)
    {
        if (_oldBaseUrl == null) {
            _oldBaseUrl = WikiEngine.instance().getBaseUrl() + WikiEngine.instance().getContextPath();
        }

        Parameters parameters = jspNode.getParameters();

        Parameter logPageParameter = parameters.getParameter("log");
        WikiPage logPage = WikiEngine.instance().getWikiPage(logPageParameter.getValue());

        WikiPage currentPage = WikiContext.getWikiContext().getWikiPage();
        if (currentPage == null) {
            jspNode.setRequestAttribute("makeStatic_url", getUrl());
        } else {
            jspNode.setRequestAttribute("makeStatic_url", currentPage.getViewUrl());
        }

        if (logPage.canEdit()) {

            // If this is a post from this plugin, then do the post processing
            // (i.e. make the static site using wget).
            if (isPost() && (logPage.getWikiName().toString().equals(getRequest().getParameter("makeStatic_page")))) {

                makeStaticSite(logPage);
                jspNode.setRequestAttribute("makeStatic_message", "Job started");

            } else {

                jspNode.setRequestAttribute("makeStatic_allow", "true");

            }

        }

    }

    protected void makeStaticSite(WikiPage logPage)
    {
        HttpServletRequest request = getRequest();
        StringBuffer url = new StringBuffer();
        url.append(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80) {
            url.append(":").append(request.getServerPort());
        }
        url.append(_startPageName.getViewUrl());

        String startUrl = url.toString();

        createMakeStaticHelper(startUrl, logPage).makeStaticSite();
    }

    protected MakeStaticHelper createMakeStaticHelper(String startUrl, WikiPage logPage)
    {
        MakeStaticHelper msh = new MakeStaticHelper(_wget, _directory, startUrl, _newBaseUrl, _oldBaseUrl, logPage);
        return msh;

    }

}
