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
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.parser.tree.ImageNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class SimpleImagePlugin extends AbstractVisualPlugin
{

    protected static Logger _logger = LogManager.getLogger(SimpleImagePlugin.class);

    public SimpleImagePlugin()
    {
        this("img");
    }

    public SimpleImagePlugin(String name)
    {
        super(name, BODY_TYPE_NONE);

        addParameterDescription(ParameterDescription.find("page").required());
    }

    /**
     * Creates a html img tag.
     */
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        Parameter pageParameter = parameters.getParameter("page");
        WikiName wikiName = pluginSupport.parseWikiName(pageParameter.getValue());

        String href = wikiName.getMediaUrl();
        String alt = wikiName.getFormatted();

        pluginSupport.getWikiDocument().addDependency(wikiName, Dependency.DEPENDENCY_ATTACH);

        pluginSupport.add(new ImageNode(href, alt));

        return null;
    }

}
