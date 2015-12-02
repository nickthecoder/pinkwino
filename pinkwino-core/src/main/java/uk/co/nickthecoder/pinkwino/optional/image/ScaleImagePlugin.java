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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Dependency;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.plugins.ImagePlugin;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Given a page called 'photo.jpg', this creates an ImagePlugin to the page
 * 'photo.jpg{scale(width='xxx' height='yyy')}) The MediaProcessor will do the
 * scaling.
 */

public class ScaleImagePlugin extends ImagePlugin
{

    protected static Logger _logger = LogManager.getLogger(ScaleImagePlugin.class);

    public ScaleImagePlugin(String pluginName)
    {
        super(pluginName);
        addParameterDescription(ParameterDescription.find("image:width").defaultValue("0"));
        addParameterDescription(ParameterDescription.find("image:height").defaultValue("0"));

    }

    public ScaleImagePlugin(String pluginName, String jspPath)
    {
        super(pluginName, jspPath);
        addParameterDescription(ParameterDescription.find("image:width").defaultValue("0"));
        addParameterDescription(ParameterDescription.find("image:height").defaultValue("0"));
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        Parameter pageParameter = parameters.getParameter("page");

        WikiEngine wikiEngine = WikiEngine.instance();
        WikiPage sourcePage = wikiEngine.getWikiPage(pageParameter.getValue());

        int width = parameters.getParameter("width").getIntValue();
        int height = parameters.getParameter("height").getIntValue();

        String relation;
        if (height == 0) {
            if (width == 0) {
                relation = null;
            } else {
                relation = "scale(width='" + width + "')";
            }
        } else {
            if (width == 0) {
                relation = "scale(height='" + height + "')";
            } else {
                relation = "scale(height='" + height + "' width='" + width + "')";
            }
        }

        WikiPage destinationPage = sourcePage.getRelatedPage(relation);

        // Make the "page" parameter's value be the destination page, so that
        // the image plugin jsp can use the "page" parameter to render the
        // processed image.
        parameters.addParameter(new Parameter(pageParameter.getParameterDescription(), destinationPage.getWikiName()
                        .getFormatted()));

        wikiDocument.addDependency(sourcePage.getWikiName(), Dependency.DEPENDENCY_ATTACH);

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}
