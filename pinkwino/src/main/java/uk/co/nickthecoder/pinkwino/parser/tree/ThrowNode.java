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

import uk.co.nickthecoder.pinkwino.parser.NodeFactory;

/**
 * Deliberately throws a Runtime exception during rendering. This class is used
 * to test the robustness of the rendering engine.
 */

public class ThrowNode implements Node, NodeFactory
{

    public ThrowNode()
    {
    }

    public void render(StringBuffer buffer)
    {
        throw new RuntimeException("ThrowNode.render");
    }

    public void text(StringBuffer buffer)
    {
        throw new RuntimeException("ThrowNode.text");
    }

    public boolean isBlock()
    {
        return false;
    }

    public Node createNode()
    {
        return this;
    }

}
