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
import uk.co.nickthecoder.pinkwino.parser.tree.ExternalLinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.Link;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Creates a link to the media within the wiki page specified.
 */
public class MediaPlugin extends AbstractVisualPlugin
{

    public MediaPlugin()
    {
        this("media");
    }

    public MediaPlugin(String name)
    {
        super(name, BODY_TYPE_NONE);

        addParameterDescription(ParameterDescription.find("page").required());
    }

    /**
     * Creates a link tag to the media within the wiki page specified.
     */
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        Parameter pageParameter = parameters.getParameter("page");
        WikiPage wikiPage = WikiEngine.instance().getWikiPage(pageParameter.getValue());

        String href = wikiPage.getMediaUrl();

        Link link = new Link();
        link.setDestination(new ExternalLinkDestination(href));

        pluginSupport.add(link);
        pluginSupport.getWikiDocument().addDependency(wikiPage.getWikiName(), Dependency.DEPENDENCY_INTERNAL_LINK);

        return null;
    }

}
