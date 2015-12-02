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
 * A sequence of wiki items, with the added feature of automatic paragraph
 * creation. If a non-block node is added at the beginning (or after a block),
 * then a paragraph is added to this group. If a non-block node is added after
 * another non-block node, then it is added to the same paragraph as before (so
 * the number of direct children of this parent does not increase).
 */

public class AutoParagraphBlock extends AbstractParentNode
{

    public SimpleParentNode _paragraph;

    public AutoParagraphBlock()
    {
        super();
    }

    public boolean isBlock()
    {
        return true;
    }

    public void add(Node node)
    {
        if (node.isBlock()) {

            _paragraph = null;
            super.add(node);

        } else {

            if (_paragraph == null) {
                _paragraph = new SimpleParentNode("p", true);
                super.add(_paragraph);
            }
            _paragraph.add(node);

        }
    }

    public void endParagraph()
    {
        _paragraph = null;
    }

}
