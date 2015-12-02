/* {{{ GPL

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

}}} */

package uk.co.nickthecoder.pinkwino.parser.syntax;

// {{{ imports
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.parser.ParametersParser;
import uk.co.nickthecoder.pinkwino.parser.ParametersParserResults;
import uk.co.nickthecoder.pinkwino.parser.Parser;
import uk.co.nickthecoder.pinkwino.parser.RemainderResult;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.plugins.VisualPlugin;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Defines the syntax rules for the simple wiki syntax, such as bold italics etc
 */

public class PluginSyntax implements WikiSyntax
{

    protected static Logger _logger = LogManager.getLogger(PluginSyntax.class);

    private String _prefix;

    private String _suffix;

    private String[] _terminators;

    public PluginSyntax(String prefix, String suffix)
    {
        _prefix = prefix;
        _suffix = suffix;

        _terminators = new String[1];
        _terminators[0] = suffix;
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public void processSyntax(Parser parser)
    {
        try {
            processSyntax2(parser);
        } catch (RuntimeException e) {
            throw e;
        }

    }

    public void processSyntax2(Parser parser)
    {
        String line = parser.getLineRemainder().trim();

        String pluginName = null;
        Parameters parameters = null;

        // Read the name of the plugin, and any parameters
        int i = 0;
        for (; i < line.length(); i++) {
            char c = line.charAt(i);

            if ((c == _suffix.charAt(0)) || Character.isWhitespace(c) || (c == '(')) {

                // found the end of the name
                pluginName = line.substring(0, i);
                break;

            }
        }

        // The plugin name could be the entire rest of the line.
        if (pluginName == null) {
            if (line.indexOf(_suffix) == -1) {
                pluginName = line;
                i = line.length();
            } else {
                pluginName = line.substring(0, line.indexOf(_suffix));
                i = line.indexOf(_suffix);
            }
        }

        // At this point, we have the plugin name, and i is at the end of that
        // name.
        VisualPlugin plugin = WikiEngine.instance().getPluginManager().getVisualPlugin(pluginName);

        // Skip whitespace
        for (; i < line.length(); i++) {
            char c = line.charAt(i);
            if (!Character.isWhitespace(c)) {
                break;
            }
        }

        ParametersParserResults parseResults = null;

        if (i < line.length() && line.charAt(i) == '(') {

            // Found the beginning of parameters

            ParametersParser parametersParser = createParametersParser(parser, plugin);

            String remainder = line.substring(i);
            parseResults = parametersParser.parseParameters(remainder);
            parameters = parseResults.parameters;

            parser.ungetLineRemainder(remainder.substring(parseResults.endIndex));

        } else if ((plugin != null) && (plugin.getBodyType() == VisualPlugin.BODY_TYPE_NONE)) {

            // We have a bodyless plugin, so we can read the parameters till the
            // closing }}

            ParametersParser parametersParser = createParametersParser(parser, plugin);
            parametersParser.setPrefix(null);
            parametersParser.setSuffix(_suffix);

            String remainder = line.substring(i);
            parseResults = parametersParser.parseParameters(remainder);
            int end = _suffix.equals(parseResults.terminator) ? parseResults.endIndex - 2 : parseResults.endIndex;

            parameters = parseResults.parameters;

            parser.ungetLineRemainder(remainder.substring(end));

        } else {

            // No parameters were specified, but default values still need to be
            // done.

            ParametersParser parametersParser = createParametersParser(parser, plugin);
            parseResults = parametersParser.parseParameters("");
            parameters = parseResults.parameters;

            parser.ungetLineRemainder(line.substring(i));
        }

        if (plugin == null) {
            parser.add(new ErrorText("Plugin " + pluginName + " not found"));
            return;
        }

        // Report bad/missing parameters.
        boolean hasErrors = false;
        for (Iterator<ParameterDescription> adi = parseResults.illegalParameters.iterator(); adi.hasNext();) {
            ParameterDescription ad = adi.next();
            parser.add(new ErrorText("Bad/missing parameter : " + ad.getName()));
            hasErrors = true;
        }

        // Remember the current parent node, so we can end all descendants to
        // return back to
        // this node in the tree. This allows the plugins not to have to close
        // their ParentNodes,
        // and it also ensures that rogue plugins do as little mess as possible.
        ParentNode oldParent = parser.getParentNode();

        Object state = null;
        if (!hasErrors) {
            state = plugin.doBegin(parser, parameters);
        }

        if (plugin.getBodyType() == VisualPlugin.BODY_TYPE_WIKI) {

            parser.parseRemainder(_terminators);

        } else if (plugin.getBodyType() == VisualPlugin.BODY_TYPE_TEXT) {

            RemainderResult remainderResult = parser.getRemainder(_terminators, true);

            if (!hasErrors) {
                plugin.bodyText(parser, parameters, remainderResult.getText(), state);
            }

        } else {

            RemainderResult remainderResult = parser.getRemainder(_terminators, true);

            // Any non-whitespace text is an error, as there is no way to render
            // it.
            if (remainderResult.getText().trim().length() > 0) {
                parser.add(new ErrorText("unexpected text : " + remainderResult.getText()));
            }

        }

        // Auto close any parentnodes that have been created.
        for (ParentNode pn = parser.getParentNode(); pn != oldParent; pn = parser.getParentNode()) {
            parser.end(pn);
        }

        if (!hasErrors) {
            plugin.doEnd(parser, parameters, state);
        }

    }

    protected ParametersParser createParametersParser(Parser parser, VisualPlugin plugin)
    {
        Version version = parser.getVersion();
        Namespace namespace = version.getWikiPage().getWikiName().getNamespace();
        if (plugin == null) {
            return new ParametersParser("(", ")", new ParameterDescriptions(), namespace);
        } else {
            return new ParametersParser("(", ")", plugin.getParameterDescriptions(), namespace);
        }
    }

}
