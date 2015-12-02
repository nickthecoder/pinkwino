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

package uk.co.nickthecoder.pinkwino.parser;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.util.PageNameParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * Parses a line of text for a set of name=value
 */

public class ParametersParser
{

    protected static Logger _logger = LogManager.getLogger(ParametersParser.class);

    private ParameterDescriptions _parameterDescriptions;

    private String _prefix;

    private String _suffix;

    private char _quote;

    /**
     * Used to add the correct namespace to page parameters. If null, then the
     * namespace won't be automatically added.
     */
    private Namespace _namespace;

    public ParametersParser(String prefix, String suffix, ParameterDescriptions parameterDescriptions)
    {
        this(prefix, suffix, '"', parameterDescriptions, null);
    }

    public ParametersParser(String prefix, String suffix, ParameterDescriptions parameterDescriptions,
                    Namespace namespace)
    {
        this(prefix, suffix, '"', parameterDescriptions, namespace);
    }

    public ParametersParser(String prefix, String suffix, char quote, ParameterDescriptions parameterDescriptions,
                    Namespace namespace)
    {
        _parameterDescriptions = parameterDescriptions;
        _prefix = prefix;
        _suffix = suffix;
        _quote = quote;
        _namespace = namespace;
    }

    public void setPrefix(String value)
    {
        _prefix = value;
    }

    public void setSuffix(String value)
    {
        _suffix = value;
    }

    public ParameterDescriptions getParameterDescriptions()
    {
        return _parameterDescriptions;
    }

    public ParameterDescription getParameterDescription(String name)
    {
        return _parameterDescriptions.getParameterDescription(name);
    }

    public void defaultValues(ParametersParserResults results)
    {
        // Add the defaults - these may get overridden later.
        for (Iterator<ParameterDescription> i = _parameterDescriptions.iterator(); i.hasNext();) {
            ParameterDescription ad = i.next();
            if (!"".equals(ad.getDefaultValue())) {
                String value = ad.getDefaultValue();
                results.parameters.addParameter(new Parameter(ad, value));
            }
        }
    }

    public ParametersParserResults parseParameters(String string)
    {
        int index = 0;
        ParametersParserResults results = new ParametersParserResults();
        defaultValues(results);

        // Check for the prefix
        if (_prefix != null) {
            if (!string.startsWith(_prefix)) {
                checkRequiredParameters(results);
                return results;
            } else {
                index = _prefix.length();
            }
        }

        // Skip white space
        for (; index < string.length(); index++) {
            if (!Character.isWhitespace(string.charAt(index))) {
                break;
            }
        }

        while (index < string.length()) {

            // Has the terminator been found?
            if ((_suffix != null) && (string.startsWith(_suffix, index))) {
                results.endIndex = index + _suffix.length();
                results.terminator = _suffix;
                checkRequiredParameters(results);
                return results;
            }

            int equalsIndex = string.indexOf("=", index);

            // no equals sign, so no more parameters.
            if (equalsIndex == -1) {
                break;
            }

            // Got the parameter's name
            String name = string.substring(index, equalsIndex);

            // Skip white space
            for (index = equalsIndex + 1; index < string.length(); index++) {
                if (!Character.isWhitespace(string.charAt(index))) {
                    break;
                }
            }

            // No quote, so its not a parameter value.
            if ((index >= string.length()) || (string.charAt(index) != _quote)) {
                break;
            }

            int closeQuoteIndex = string.indexOf(_quote, index + 1);

            if (closeQuoteIndex == -1) {
                break;
            }

            String value = string.substring(index + 1, closeQuoteIndex);

            // Skip white space
            for (index = closeQuoteIndex + 1; index < string.length(); index++) {
                if (!Character.isWhitespace(string.charAt(index))) {
                    break;
                }
            }

            // We have the name and value, now to validate it...
            ParameterDescription parameterDescription = getParameterDescription(name);
            if (parameterDescription == null) {

                if (_parameterDescriptions.getAllowUnknown()) {
                    parameterDescription = new RegexParameterDescription(name, ".*");
                } else {
                    results.illegalParameters.addParameterDescription(new RegexParameterDescription(name, ".*"));
                    continue;
                }

            }

            // Ensure that the correct namespace is used if none is given.
            if ((parameterDescription instanceof PageNameParameterDescription)) {
                WikiName wikiName = WikiEngine.instance().getWikiNameFormat().parse(_namespace, value);
                value = wikiName.getFormatted();
            }

            Parameter parameter = parameterDescription.allowValue(value);
            if (parameter == null) {
                results.illegalParameters.addParameterDescription(parameterDescription);
                continue;
            }

            // It is valid
            results.parameters.addParameter(parameter);

            results.endIndex = index;
        }

        checkRequiredParameters(results);
        return results;
    }

    // }}}

    private void checkRequiredParameters(ParametersParserResults results)
    {

        for (Iterator<ParameterDescription> i = _parameterDescriptions.iterator(); i.hasNext();) {
            ParameterDescription ad = i.next();

            if (ad.getRequired()) {
                Parameter parameter = results.parameters.getParameter(ad.getName());
                if ((parameter == null) || ("".equals(parameter.getValue()))) {
                    results.illegalParameters.addParameterDescription(ad);
                }
            }
            /*
             * else if ( ad.hasDefaultValue() ) { if (
             * results.parameters.getParameter( ad.getName() ) == null ) {
             * Parameter parameter = new Parameter( ad, ad.getDefaultValue() );
             * results.parameters.addParameter( parameter ); } }
             */
        }
    }

    // -------------------- [[Test / Debug]] --------------------

}

// ---------- End Of Class ParametersParser ----------

