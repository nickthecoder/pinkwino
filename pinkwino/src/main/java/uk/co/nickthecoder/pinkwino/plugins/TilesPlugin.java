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

import uk.co.nickthecoder.pinkwino.Dependency;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.TemplateNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Allows a template to be inserted into the wiki page, and each parameter of
 * the template can be a seperate block of wiki code within a "tile" plugin.
 * Here's an example wiki markup using tiles :
 * 
 * <pre>
 * 
 *   {{tiles( page="personTemplate" )
 *     {{tile( name="name" )Nick Robinson}}
 *     {{tile( name="comments" )Check out [my wiki page|Nick Robinson] for more info.}}
 *   }}
 * </pre>
 */
public class TilesPlugin extends AbstractVisualPlugin
{

    public static final String WIKI_CURRENT_TEMPLATE = "wiki_currentTemplate";

    public static TemplateNode getCurrentTemplateNode()
    {
        return (TemplateNode) WikiEngine.instance().getWikiContext().getAttribute(WIKI_CURRENT_TEMPLATE);
    }

    public TilesPlugin()
    {
        this("tiles");
    }

    public TilesPlugin(String name)
    {
        super(name, BODY_TYPE_WIKI);

        addParameterDescription(ParameterDescription.find("page"));
        getParameterDescriptions().setAllowUnknown(true);
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        // If one template is nested within another, then we need to keep track
        // of which is the inner one
        // (as that is the one that the TilePlugin will need to reference). So
        // each time we start a new
        // template, we remember the previous, parent template (if there is
        // one), and restore it in doEnd.
        TemplateNode pushedTemplateNode = getCurrentTemplateNode();

        Parameter pageParameter = parameters.getParameter("page");

        if (pageParameter == null) {
            pluginSupport.add(new ErrorText("page=???"));
            return pushedTemplateNode;
        }

        WikiPage wikiPage = WikiEngine.instance().getWikiPage(pageParameter.getValue());
        if (wikiPage == null) {
            pluginSupport.add(new ErrorText("page not found"));
            return pushedTemplateNode;
        }

        TemplateNode templateNode = new TemplateNode(wikiPage, parameters);
        pluginSupport.add(templateNode);
        WikiEngine.instance().getWikiContext().setAttribute(WIKI_CURRENT_TEMPLATE, templateNode);

        // MORE - add the body as a template parameter called "BODY"

        pluginSupport.getWikiDocument().addDependency(wikiPage.getWikiName(), Dependency.DEPENDENCY_INCLUDE);

        return pushedTemplateNode;
    }

    public void doEnd(PluginSupport pluginSupport, Parameters parameters, Object pluginState)
    {
        // Restore the previous template if there was one. Most of the time this
        // will be null - it is
        // only needed if there are nested templates.
        TemplateNode pushedTemplateNode = (TemplateNode) pluginState;

        WikiEngine.instance().getWikiContext().setAttribute(WIKI_CURRENT_TEMPLATE, pushedTemplateNode);
    }

}
