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
import uk.co.nickthecoder.pinkwino.parser.tree.Table;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;

public class TableBeginSyntax extends AbstractWikiLineSyntax
{

    public TableBeginSyntax(String prefix)
    {
        super(prefix);

        addParameterDescription(ParameterDescription.find("class").defaultValue("wiki_table"));
        addParameterDescription(ParameterDescription.find("height"));
        addParameterDescription(ParameterDescription.find("width"));
        addParameterDescription(ParameterDescription.find("cellspacing"));
        addParameterDescription(ParameterDescription.find("cellpadding"));

    }

    public void processSyntax(Parser parser)
    {
        Table table = new Table();
        parser.begin(table);

        parseParameters(parser, table);
    }

}
