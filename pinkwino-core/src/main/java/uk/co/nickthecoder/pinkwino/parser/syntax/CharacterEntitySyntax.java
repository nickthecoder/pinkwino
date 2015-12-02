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
import uk.co.nickthecoder.pinkwino.parser.RemainderResult;
import uk.co.nickthecoder.pinkwino.parser.tree.CharacterEntity;

/**
 * Defines the wiki syntax for links
 */

public class CharacterEntitySyntax implements WikiSyntax
{

    protected static Logger _logger = LogManager.getLogger(CharacterEntitySyntax.class);

    private static String CE_PREFIX = "&";

    private static String CE_SUFFIX = ";";

    private static String[] _terminators = { CE_SUFFIX };

    public CharacterEntitySyntax()
    {
    }

    public String getPrefix()
    {
        return CE_PREFIX;
    }

    public void processSyntax(Parser parser)
    {
        RemainderResult results = parser.getRemainder(_terminators, false);

        if (results.getTerminator() == null) {
            parser.unget(results.getText());
            parser.unget(CE_PREFIX);

        } else {
            try {
                parser.add(new CharacterEntity(results.getText()));
            } catch (Exception e) {
                parser.unget(results.getTerminator());
                parser.unget(results.getText());
                parser.unget(CE_PREFIX);
            }
        }
    }

}
