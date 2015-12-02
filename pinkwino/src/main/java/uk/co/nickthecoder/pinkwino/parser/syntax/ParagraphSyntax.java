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
import uk.co.nickthecoder.pinkwino.parser.tree.EndNode;

/**
 * Defines the syntax for the gap between paragraphs
 */

public class ParagraphSyntax implements WikiLineSyntax
{

    public ParagraphSyntax()
    {
    }

    public String getPrefix()
    {
        return "";
    }

    public int matchingPrefixLength(String line)
    {
        if (line.trim().length() == 0) {
            return line.length();
        }

        return NO_MATCH;
    }

    public void processSyntax(Parser parser)
    {
        parser.add(new EndNode());
    }

}
