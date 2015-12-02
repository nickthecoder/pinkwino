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
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;

/**
*/

public class TableCellSyntax extends AbstractWikiLineSyntax
{

    private boolean _heading;

    private String[] _terminators;

    /**
     * If this is a heading cell, then the alternate will be a normal cell.
     * Likewise, if this is a normal cell, then the alternate is a heading cell.
     * See setAlternate().
     */
    private TableCellSyntax _alternate;

    public TableCellSyntax(String prefix, boolean heading)
    {
        super(prefix);
        _heading = heading;
        _terminators = new String[] { prefix };

        addParameterDescription(ParameterDescription.find("class"));
        addParameterDescription(ParameterDescription.find("width"));
        addParameterDescription(ParameterDescription.find("height"));
        addParameterDescription(ParameterDescription.find("align"));
        addParameterDescription(ParameterDescription.find("valign"));
        addParameterDescription(ParameterDescription.find("rowspan"));
        addParameterDescription(ParameterDescription.find("colspan"));
    }

    /**
     * The heading cell needs to know about the normal cells, and likewise, the
     * noraml cells need to know about the heading cells. Why? Because of the
     * way table cells can be squashed together on one line :
     * 
     * <Pre>
     *       {|
     *       | hello | world || heading | cell | another heading
     *       |}
     * </pre>
     * 
     * In the above example, the "world" cell will be terminated by the
     * beginning of the "heading" cell.
     */
    public void setAlternate(TableCellSyntax alternate)
    {
        _alternate = alternate;
        String otherTerminator = alternate.getPrefix();

        if (otherTerminator.length() > getPrefix().length()) {
            _terminators = new String[] { otherTerminator, getPrefix() };
        } else {
            _terminators = new String[] { getPrefix(), otherTerminator };
        }
    }

    public int matchingPrefixLength(String line)
    {
        if (line.startsWith(getPrefix())) {
            return getPrefix().length();
        } else {
            return NO_MATCH;
        }
    }

    public void processSyntax(Parser parser)
    {
        TableCell cell = new TableCell(_heading);

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
            parser.begin(cell);

        } else if (parent instanceof TableRow) {

            parser.begin(cell);

        } else if (parent instanceof Table) {

            parser.begin(new TableRow());
            parser.begin(cell);

        } else {

            parser.add(new ErrorText("(cell) " + getPrefix()));

        }

        parseParameters(parser, cell);

        String terminator = parser.parseLineRemainder(_terminators);

        if (terminator == null) {

            // do nothing

        } else if (getPrefix().equals(terminator)) {

            // We've found a table cell
            processSyntax(parser);

        } else if ((_alternate != null) && (_alternate.getPrefix().equals(terminator))) {

            // We have found the other type of table cell
            _alternate.processSyntax(parser);

        } else {
            // Hmm should never get here.
            throw new RuntimeException("TableCellSyntax is broken");
        }

    }

}
