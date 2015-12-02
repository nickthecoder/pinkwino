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

/**
 * Defines the syntax for wiki syntax which can only appear at the beginning of
 * a line
 */

public interface WikiLineSyntax
{
    public static int NO_MATCH = -1;

    /**
     * This is not functional, it is only to help debugging, and to show on the
     * system status page.
     */
    public String getPrefix();

    /**
     * If the line doesn't match, then return -1, if the line does match, then
     * return the length of this suffix which matches, so that it can be
     * consumed by the parser before it calls the processSyntax method.
     */
    public int matchingPrefixLength(String line);

    /**
     * At this point, the beginning of the line is know to match, and has
     * already been consumed. The parser is now ready to parse the inner
     * contents of this line.
     */
    public void processSyntax(Parser parser);

}
