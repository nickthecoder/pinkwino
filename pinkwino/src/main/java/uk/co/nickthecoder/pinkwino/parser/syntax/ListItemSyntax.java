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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.parser.Parser;
import uk.co.nickthecoder.pinkwino.parser.tree.ListItem;
import uk.co.nickthecoder.pinkwino.parser.tree.ListNode;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;

/**
 * Creates a html hr tag
 */

public class ListItemSyntax implements WikiLineSyntax
{

    protected static Logger _logger = LogManager.getLogger(ListItemSyntax.class);

    private boolean _ordered;

    private int _level;

    private String _prefix;

    public ListItemSyntax(boolean ordered, int level, String prefix)
    {
        _ordered = ordered;
        _level = level;
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
        }

        return NO_MATCH;
    }

    public void processSyntax(Parser parser)
    {
        ParentNode parent = parser.getParentNode();
        int currentLevel = 0;

        if (parent instanceof ListItem) {
            ListItem listItem = (ListItem) parent;

            if (listItem.getLevel() >= _level) {
                parser.end(listItem);
            }

            if (listItem.getLevel() > _level) {
                // We should have a listNode now.
                if (parser.getParentNode() instanceof ListNode) {
                    parser.end(parser.getParentNode());
                } else {
                    _logger.error("Expected listnode, but found : " + parser.getParentNode());
                }
                processSyntax(parser);
                return;

            } else {

                currentLevel = listItem.getLevel();
            }

        }

        // At this point, we may need to add additional levels of ListNodes

        for (int i = currentLevel + 1; i <= _level; i++) {
            parser.begin(new ListNode(i, _ordered));
            if (i < _level) {
                parser.begin(new ListItem(i));
            }
        }

        // Now we should be at the correct level.

        ListItem listItem = new ListItem(_level);
        parser.begin(listItem);
    }

}
