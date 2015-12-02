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

import uk.co.nickthecoder.pinkwino.parser.ParametersParser;
import uk.co.nickthecoder.pinkwino.parser.ParametersParserResults;
import uk.co.nickthecoder.pinkwino.parser.Parser;
import uk.co.nickthecoder.pinkwino.parser.tree.AbstractNode;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;

/**
*/

public abstract class AbstractWikiLineSyntax implements WikiLineSyntax
{

    private String _prefix;

    private String _suffix;

    private ParameterDescriptions _parameterDescriptions;

    private ParametersParser _parametersParser;

    public AbstractWikiLineSyntax(String prefix)
    {
        this(prefix, null);
    }

    public AbstractWikiLineSyntax(String prefix, String suffix)
    {
        _prefix = prefix;
        _suffix = suffix;

        _parameterDescriptions = new ParameterDescriptions();
        _parametersParser = new ParametersParser("(", ")", _parameterDescriptions);
    }

    public int matchingPrefixLength(String line)
    {
        if (line.startsWith(_prefix)) {
            return _prefix.length();
        } else {
            return NO_MATCH;
        }
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public String getSuffix()
    {
        return _suffix;
    }

    public void addParameterDescription(ParameterDescription ad)
    {
        _parametersParser.getParameterDescriptions().addParameterDescription(ad);
    }

    public void parseParameters(Parser parser, AbstractNode node)
    {
        String line = parser.getLineRemainder();

        ParametersParserResults parseResults = _parametersParser.parseParameters(line);
        node.setParameters(parseResults.parameters);

        parser.ungetLineRemainder(line.substring(parseResults.endIndex));
    }

    public abstract void processSyntax(Parser parser);

}
