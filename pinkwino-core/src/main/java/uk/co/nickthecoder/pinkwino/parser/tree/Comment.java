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
 * Will not be rendered.
 */

public class Comment extends AbstractParentNode implements NodeFactory
{

    public Comment()
    {
        super();
    }

    @Override
    public void render(StringBuffer buffer)
    {
        // Do nothing
    }

    @Override
    public boolean isBlock()
    {
        return false;
    }

    @Override
    public Node createNode()
    {
        return this;
    }

}
