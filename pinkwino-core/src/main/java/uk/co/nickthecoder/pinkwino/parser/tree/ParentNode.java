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

import java.util.Iterator;

/**
 * Any node which has a set of child nodes
 */
public interface ParentNode extends Node
{
    /**
     * @return An Iterator of all Nodes, in the order that they were added.
     */
    public Iterator<Node> getChildren();

    /**
     * Adds a child Node to the end of the list.
     * @param node
     */
    public void add(Node node);

    /**
     * Renders the node as HTML, and appends it to the StringBuffer.
     */
    public void render(StringBuffer buffer);

    /**
     * Appends the text of this parent node (including all its children, grandchildren etc) to
     * the StringBuffer.
     */
    public void text(StringBuffer buffer);

}
