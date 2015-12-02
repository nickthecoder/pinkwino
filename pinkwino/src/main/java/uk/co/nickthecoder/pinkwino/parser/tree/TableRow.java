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

public class TableRow extends AbstractParentNode
{

    public TableRow()
    {
        super();
    }

    public void render(StringBuffer buffer)
    {
        buffer.append("<tr");
        renderParameters(buffer);
        buffer.append(">");
        super.render(buffer);
        buffer.append("</tr>");
    }

    public void add(Node node)
    {
        if (node instanceof TableCell) {
            super.add(node);
        }
    }

    public boolean isBlock()
    {
        return true;
    }

}

