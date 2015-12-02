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
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;

/**
 * Defines the syntax rules for the simple wiki syntax, such as bold italics etc
 */

public class SimpleWikiSyntax implements WikiSyntax
{

    private String _prefix;

    private String[] _terminators;

    private NodeFactory _nodeFactory;

    public SimpleWikiSyntax(String prefixSuffix, NodeFactory nodeFactory)
    {
        this(prefixSuffix, prefixSuffix, nodeFactory);
    }

    public SimpleWikiSyntax(String prefix, String suffix, NodeFactory nodeFactory)
    {
        _prefix = prefix;
        _nodeFactory = nodeFactory;
        _terminators = new String[1];
        _terminators[0] = suffix;
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public void processSyntax(Parser parser)
    {
        Node node = _nodeFactory.createNode();

        if (node instanceof ParentNode) {

            ParentNode parentNode = (ParentNode) _nodeFactory.createNode();
            parser.begin(parentNode);
            String terminator = parser.parseRemainder(_terminators);
            if (terminator == null) {
                parser.abandon(parentNode, _prefix);
            } else {
                parser.end(parentNode);
            }

        } else {

            parser.add(node);

        }
    }

}
