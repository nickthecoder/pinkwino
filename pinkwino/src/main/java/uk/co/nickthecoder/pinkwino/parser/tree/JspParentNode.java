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

package uk.co.nickthecoder.pinkwino.parser.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.co.nickthecoder.pinkwino.plugins.JspPlugin;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Allows an arbitary jsp page to be part of a wiki. This is used by
 * VisualPlugins, such as the PluginsPlugin.
 */

public class JspParentNode extends JspNode implements ParentNode
{
    private List<Node> _children;

    public JspParentNode(JspPlugin plugin, Parameters parameters, String jspPath, boolean isBlock)
    {
        super(plugin, parameters, jspPath, isBlock);
        _children = new ArrayList<Node>();
    }

    public Iterator<Node> getChildren()
    {
        return _children.iterator();
    }

    public void add(Node node)
    {
        _children.add(node);
    }

    public String getRenderChildren()
    {
        StringBuffer buffer = new StringBuffer();

        for (Iterator<Node> i = getChildren(); i.hasNext();) {
            Node node = i.next();

            try {

                node.render(buffer);

            } catch (Exception e) {

                new ErrorText("(JspExcepion) " + node.getClass().getName()).render(buffer);
                new ExceptionNode(e).render(buffer);

            }
        }

        return buffer.toString();
    }

    public String getText()
    {
        StringBuffer buffer = new StringBuffer();
        text(buffer);
        return buffer.toString();
    }
}
