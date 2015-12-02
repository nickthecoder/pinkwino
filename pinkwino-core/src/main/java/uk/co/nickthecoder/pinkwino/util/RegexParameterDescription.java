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

package uk.co.nickthecoder.pinkwino.util;

import java.util.regex.Pattern;

import uk.co.nickthecoder.pinkwino.WikiConstants;

/**
 * Defines the name, and allowable values for an parameter
 */
public class RegexParameterDescription extends ParameterDescription
{

    private Pattern _pattern;

    public RegexParameterDescription(String name, String regex)
    {
        this(name, regex, false, "", WikiConstants.USAGE_GENERAL);
    }

    public RegexParameterDescription(String name, String regex, boolean required, String defaultValue, String usage)
    {
        this(name, Pattern.compile(regex), required, defaultValue, usage);
    }

    public RegexParameterDescription(String name, Pattern pattern, boolean required, String defaultValue, String usage)
    {
        super(name, required, defaultValue, usage);
        _pattern = pattern;
    }

    public Parameter allowValue(String requestedValue)
    {
        if (_pattern.matcher(requestedValue).matches()) {
            return new Parameter(this, requestedValue);
        } else {
            return null;
        }
    }

    public String getSummary()
    {
        return "regex : " + _pattern.pattern();
    }

    public ParameterDescription required()
    {
        return new RegexParameterDescription(getName(), _pattern, true, getDefaultValue(), getUsage());
    }

    public ParameterDescription defaultValue(String defaultValue)
    {
        return new RegexParameterDescription(getName(), _pattern, getRequired(), defaultValue, getUsage());
    }

}
