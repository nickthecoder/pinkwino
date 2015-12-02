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

import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.HiddenParent;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.TemplateNode;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**

*/

public class TilePlugin extends AbstractVisualPlugin
{
    public TilePlugin()
    {
        this("tile");
    }

    public TilePlugin(String name)
    {
        super(name, BODY_TYPE_WIKI);

        addParameterDescription(ParameterDescription.find("name"));
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        TemplateNode templateNode = TilesPlugin.getCurrentTemplateNode();

        if (templateNode == null) {
            pluginSupport.add(new ErrorText("Must be within a 'tiles' plugin"));
            return null;
        }

        String name = parameters.getParameter("name").getValue();
        if (name == null) {
            pluginSupport.add(new ErrorText("name=???"));
            return null;
        }

        // We hide from view the body contents of this plugin
        HiddenParent hiddenParent = new HiddenParent(false /* is not a block */);
        pluginSupport.begin(hiddenParent);

        ParentNode parentNode = new PlainParentNode(false /* is not a block */);
        pluginSupport.begin(parentNode);

        // Now we add this node to the template.
        templateNode.addNode(name, parentNode);

        return null;
    }

}
