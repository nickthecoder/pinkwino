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
 * A html ul or ol, with a set of ListItem ( html li) children.
 */

public class ListNode extends SimpleParentNode implements Unterminated
{
    private int _level;

    private boolean _ordered;

    public ListNode(int level, boolean ordered)
    {
        super(ordered ? "ol" : "ul", true);
        _level = level;
        _ordered = ordered;
    }

    public void add(Node node)
    {
        if (!(node instanceof ListItem)) {
            throw new RuntimeException("ListNodes can only contain ListItem nodes");
        }
        super.add( node );
    }

    public int getLevel()
    {
        return _level;
    }

    public boolean getOrdered()
    {
        return _ordered;
    }

}
