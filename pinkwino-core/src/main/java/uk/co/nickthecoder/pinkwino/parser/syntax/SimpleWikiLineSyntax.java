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

import uk.co.nickthecoder.pinkwino.parser.NodeFactory;
import uk.co.nickthecoder.pinkwino.parser.Parser;
import uk.co.nickthecoder.pinkwino.parser.RemainderResult;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainText;

/**
 * Defines the syntax for the headings
 */

public class SimpleWikiLineSyntax implements WikiLineSyntax
{

    private NodeFactory _factory;

    private String _prefix;

    private String[] _terminators;

    private boolean _singleLine;

    public SimpleWikiLineSyntax(String prefix, NodeFactory nodeFactory)
    {
        this(prefix, null, nodeFactory, true);
    }

    public SimpleWikiLineSyntax(String prefix, String suffix, NodeFactory nodeFactory, boolean singleLine)
    {
        _prefix = prefix;
        _singleLine = singleLine;

        if (suffix == null) {
            _terminators = Parser.NO_TERMINATORS;
            _singleLine = true;
        } else {
            _terminators = new String[1];
            _terminators[0] = suffix;
        }

        _factory = nodeFactory;
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
        ParentNode parentNode = (ParentNode) _factory.createNode();
        parser.begin(parentNode);

        if (_singleLine) {
            parser.parseLineRemainder(_terminators);
        } else {
            //parser.parseRemainder(_terminators);
            RemainderResult results = parser.getRemainder(_terminators, true);

            if (results.getTerminator() == null) {
                parser.abandon(parentNode, getPrefix());
                parser.add(new ErrorText("Block " + results.getText()));
            } else {
                parentNode.add(new PlainText(results.getText().trim()));
                parser.end(parentNode);
            }

        }

        parser.end(parentNode);

    }

}
