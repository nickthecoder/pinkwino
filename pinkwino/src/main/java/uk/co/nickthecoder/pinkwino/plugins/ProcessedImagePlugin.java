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
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Uses Sun's JAI Java Advanced Image toolkit to create resize and create a drop
 * shadow for a given image.
 */
public class ProcessedImagePlugin extends ImagePlugin
{

    protected static Logger _logger = LogManager.getLogger(ProcessedImagePlugin.class);

    /**
     * The processed image in stored as a related wiki page. For example,
     * processing the image "welcome.jpg" would create the page
     * welcome.jpg$thumbnail if _relationName was "thumbnail".
     */
    private String _relationName;

    public ProcessedImagePlugin(String bothNames)
    {
        this(bothNames, bothNames);
    }

    public ProcessedImagePlugin(String pluginName, String relationName)
    {
        super(pluginName);

        _relationName = relationName;
    }

    public ProcessedImagePlugin(String pluginName, String relationName, String jspPath)
    {
        super(pluginName, jspPath);

        _relationName = relationName;
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        Parameter pageParameter = parameters.getParameter("page");

        WikiEngine wikiEngine = WikiEngine.instance();
        WikiPage sourcePage = wikiEngine.getWikiPage(pageParameter.getValue());
        WikiPage destinationPage = sourcePage.getRelatedPage(_relationName);

        // Make the "page" parameter's value be the destination page, so that
        // the image plugin jsp can use the "page" parameter to render the
        // processed image.
        parameters.addParameter(new Parameter(pageParameter.getParameterDescription(), destinationPage.getWikiName()
                        .getFormatted()));

        wikiDocument.addDependency(sourcePage.getWikiName(), Dependency.DEPENDENCY_ATTACH);

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}
