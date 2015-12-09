/* {{{
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
}}} */

package uk.co.nickthecoder.pinkwino.parser.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Any node which has a set of child nodes
 */

public abstract class AbstractParentNode extends AbstractNode implements ParentNode, Cloneable
{

    private List<Node> _children;

    public AbstractParentNode()
    {
        super();
        _children = new ArrayList<Node>();
    }

    @Override
    public Iterator<Node> getChildren()
    {
        return _children.iterator();
    }

    @Override
    public void add(Node node)
    {
        _children.add(node);
    }

    public void clear()
    {
        _children.clear();
    }

    @Override
    public void render(StringBuffer buffer)
    {
        for (Iterator<Node> i = getChildren(); i.hasNext();) {
            Node node = i.next();

            try {

                node.render(buffer);

            } catch (Exception e) {

                new ErrorText("(Exception) " + node.getClass().getName()).render(buffer);
                new ExceptionNode(e).render(buffer);

            }
        }

    }

    @Override
    public void text(StringBuffer buffer)
    {
        boolean middle = false;
        for (Iterator<Node> i = getChildren(); i.hasNext();) {
            if (middle) {
                buffer.append(" ");
            } else {
                middle = true;
            }
            Node node = i.next();

            node.text(buffer);
        }
    }

    public String getText()
    {
        StringBuffer buffer = new StringBuffer();
        text(buffer);
        return buffer.toString();
    }

    @Override
    public Object clone()
    {
        try {
            AbstractParentNode clone = (AbstractParentNode) super.clone();
            clone._children = new ArrayList<Node>(_children);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
