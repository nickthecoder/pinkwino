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

package uk.co.nickthecoder.pinkwino.parser.syntax;

import uk.co.nickthecoder.pinkwino.parser.Parser;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Table;
import uk.co.nickthecoder.pinkwino.parser.tree.TableCell;
import uk.co.nickthecoder.pinkwino.parser.tree.TableRow;
import uk.co.nickthecoder.pinkwino.parser.tree.Unterminated;

public class TableEndSyntax implements WikiLineSyntax
{

    private String _prefix;

    public TableEndSyntax(String prefix)
    {
        _prefix = prefix;
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public int matchingPrefixLength(String line)
    {
        if (line.startsWith(_prefix)) {
            return _prefix.length();
        } else {
            return NO_MATCH;
        }
    }

    public void processSyntax(Parser parser)
    {
        /*
         * Terminate all unterminated tags, such as the ListItem and ListNode
         * (they are the only ones right now)
         */
        while (parser.getParentNode() instanceof Unterminated) {
            parser.end(parser.getParentNode());
        }

        ParentNode parent = parser.getParentNode();

        if (parent instanceof TableCell) {
            parser.end(parent);
            parent = parser.getParentNode();
        }

        if (parent instanceof TableRow) {
            parser.end(parent);
            parent = parser.getParentNode();
        }

        if (parent instanceof Table) {
            parser.end(parent);

        } else {
            // The parent is wrong, lets flag the error, and give up.
            parser.add(new ErrorText(_prefix + " (table)"));
        }

        return;
    }

}
