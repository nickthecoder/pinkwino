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
import uk.co.nickthecoder.pinkwino.parser.tree.PlainParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainText;
import uk.co.nickthecoder.pinkwino.parser.tree.SimpleParentNode;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * An example plugin for demonstation purposes only.
 */
public class HelloWorldPlugin extends AbstractVisualPlugin
{

    /**
     * Create a HelloWorldPlugin with the give plugin name, and the path to the
     * jsp page responsible for rendering the plugin. If jspPath begins with a
     * slash, then that path is used verbatim, otherwise, the HelloWorldPlugin
     * root is prefixed to it. This HelloWorldPlugin root is obtained from the
     * PluginManager.
     * 
     * This version of the contructor creates a HelloWorldPlugin which is not a
     * parent.
     */
    public HelloWorldPlugin(String name)
    {
        super(name, BODY_TYPE_NONE);
    }

    /**
     * Creates a the "Hello World", with the "World" part in bold.
     */
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        // As "hello" is plain text, and "world" is bold, they are two separate
        // things, therefore we need to group them together with the help of the
        // PlainParentNode.
        ParentNode parent = new PlainParentNode(false);

        parent.add(new PlainText("Hello "));

        // Create a html <b> tag (which will be wrapped around the "World"
        // text).
        // The "false", indicates that this is not a html block element
        // This information is used within the wiki parser to ensure that
        // paragraphs
        // are terminated correctly.
        SimpleParentNode bold = new SimpleParentNode("b", false);

        // Note that "bold" uses a parent node too, and it could have had more
        // than one child nodes, but in this example, it will just have one
        // child (the "World" text)

        bold.add(new PlainText("World"));

        parent.add(bold);

        /*
         * The tree of nodes now looks like this :
         * 
         * PlainParentNode | +-- PlainText ("Hello") | +-- SimpleParentNode (
         * html "<b>" tag ) | +-- PlainText ("World")
         */

        // Add this brach of the tree.
        pluginSupport.add(parent);

        // We don't need to store any state information for the doEnd method, so
        // return null.
        return null;
    }

}
