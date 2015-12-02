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

/**
 * Any piece of wiki syntax
 */

public interface Node
{

    /**
     * Render the node into html. If this node has inner nodes, then you these
     * inner nodes are also renderer, and are done so using the renderer passed
     * to this method. i.e. the inner nodes should NOT have their render methods
     * called directly. This is so that a custom renderer can intercept and
     * alter the standard behavior.
     */
    public void render(StringBuffer buffer);

    /**
     * Is this Node a block element, such as a heading, or is it an inline
     * element such as bold text.
     */
    public boolean isBlock();

    /**
     * Append into the buffer, the text that is held by this node. It will
     * generally be the wiki syntax without the syntax bits. For example
     * __hello__ is a Bold node, and its text is just hello (without the
     * underscores).
     */
    public void text(StringBuffer buffer);

}

