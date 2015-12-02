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

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.ExternalLinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.Link;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainText;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class ExternalLinkPlugin extends AbstractVisualPlugin
{

    protected static Logger _logger = LogManager.getLogger(SimpleImagePlugin.class);

    public ExternalLinkPlugin()
    {
        this("externalLink");
    }

    public ExternalLinkPlugin(String name)
    {
        super(name, BODY_TYPE_NONE);

        addParameterDescription(ParameterDescription.find("url").required());
        addParameterDescription(ParameterDescription.find("label").required());
    }

    /**
     * Creates a html anchor tag.
     */
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        String template = parameters.getParameter("url").getValue();
        String label = parameters.getParameter("label").getValue();
        WikiPage page = WikiContext.getWikiContext().getWikiPage();

        String replacement = uk.co.nickthecoder.webwidgets.util.TagUtil.encodeUrl(page.getWikiName().getTitle());
        String href = template.replaceAll("%s", replacement);

        ExternalLinkDestination ld = new ExternalLinkDestination(href);
        Link link = new Link();
        link.setDestination(ld);

        link.add(new PlainText(label));

        pluginSupport.add(link);

        return null;
    }

}
