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
 * Deliberately throws a Runtime exception during parsing. This class is used to
 * test the robustness of the parser.
 */

public class ThrowSyntax implements WikiSyntax
{

    private String _prefix;

    public ThrowSyntax(String prefix)
    {
        _prefix = prefix;
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public void processSyntax(Parser parser)
    {
        throw new RuntimeException("ThrowSyntax");
    }

}
