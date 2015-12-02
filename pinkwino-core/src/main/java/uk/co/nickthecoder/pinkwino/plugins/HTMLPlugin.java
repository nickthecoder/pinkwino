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

import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.SimpleParentNode;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Allows arbitary HTML to be embedded into the page. Only use this on a secured
 * wiki, because letting untrusted people add arbitary HTML on your web site is
 * seriously bad!
 */

public class HTMLPlugin extends AbstractVisualPlugin
{

    public HTMLPlugin()
    {
        this("html");
    }

    public HTMLPlugin(String name)
    {
        super(name, BODY_TYPE_TEXT);
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        ParentNode div = new SimpleParentNode("div", true);

        pluginSupport.begin(div);

        return null;
    }

    public void bodyText(PluginSupport pluginSupport, Parameters parameters, String text, Object pluginState)
    {
        // pluginSupport.add( new VerbatimNode( text ) );
    }

}
