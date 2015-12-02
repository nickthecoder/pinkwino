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

import uk.co.nickthecoder.pinkwino.Section;
import uk.co.nickthecoder.pinkwino.parser.Parser;
import uk.co.nickthecoder.pinkwino.parser.tree.HeadingNode;

/**
 * Defines the syntax for the headings
 */

public class HeadingSyntax implements WikiLineSyntax
{

    private String _prefix;

    private String[] _terminators;

    private int _level;

    public HeadingSyntax(int level, String prefix)
    {
        this(level, prefix, null);
    }

    public HeadingSyntax(int level, String prefix, String terminator)
    {
        _prefix = prefix;
        _level = level;

        if (terminator == null) {
            _terminators = Parser.NO_TERMINATORS;
        } else {
            _terminators = new String[1];
            _terminators[0] = terminator;
        }

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
        Section section = new Section(_level, "");
        section.setStartLineNumber(parser.getLineNumber());

        Section previousSection = parser.getLatestSection();
        while (previousSection.getLevel() >= _level) {
            previousSection.setEndLineNumber(parser.getLineNumber() - 1);
            previousSection = previousSection.getParent();
        }
        previousSection.addSubsection(section);
        parser.setLatestSection(section);

        // End the previous section if required

        HeadingNode heading = new HeadingNode(section);
        parser.begin(heading);

        if (_terminators == Parser.NO_TERMINATORS) {
            parser.parseLineRemainder(_terminators);
        } else {
            parser.parseRemainder(_terminators);
        }

        parser.end(heading);

        String title = heading.getText();
        section.setTitle(title);

    }

}
