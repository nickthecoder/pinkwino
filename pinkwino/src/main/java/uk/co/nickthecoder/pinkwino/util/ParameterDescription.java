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

import java.util.HashMap;
import java.util.Map;

import uk.co.nickthecoder.pinkwino.Summarisable;

/**
 * Allows validation of an parameter
 */

public abstract class ParameterDescription implements Summarisable
{

    public static final String USAGE_GENERAL = "general";
    public static final String USAGE_STYLE = "style";
    public static final String USAGE_HTML = "html";

    private static Map<String, ParameterDescription> _parameterDescriptions;

    public static ParameterDescription find(String name)
    {
        if (_parameterDescriptions == null) {

            _parameterDescriptions = new HashMap<String, ParameterDescription>();

            registerParameterDescription("image:width", new IntParameterDescription("width", 1, Integer.MAX_VALUE,
                            false, "", USAGE_GENERAL));
            registerParameterDescription("image:height", new IntParameterDescription("height", 1, Integer.MAX_VALUE,
                            false, "", USAGE_GENERAL));

            registerParameterDescription("html:width", new IntParameterDescription("width", 1, Integer.MAX_VALUE,
                            false, "", USAGE_HTML));
            registerParameterDescription("html:height", new IntParameterDescription("height", 1, Integer.MAX_VALUE,
                            false, "", USAGE_HTML));
            registerParameterDescription(new SimpleParameterDescription("src", false, "", USAGE_HTML));

            registerParameterDescription(new RegexParameterDescription("width", "[0-9]+(%|px|em|pt)", false, "",
                            USAGE_STYLE));
            registerParameterDescription(new RegexParameterDescription("height", "[0-9]+(%|px|em|pt)", false, "",
                            USAGE_STYLE));
            registerParameterDescription(new RegexParameterDescription("float", "left|right|none", false, "",
                            USAGE_STYLE));
            registerParameterDescription(new RegexParameterDescription("text-align", "left|center|right|justify",
                            false, "", USAGE_STYLE));

            registerParameterDescription(new RegexParameterDescription("class", "[\\p{Alnum}_ -]+", false, "",
                            USAGE_HTML));

            registerParameterDescription(new RegexParameterDescription("cellspacing", "[0-9]+", false, "", USAGE_HTML));
            registerParameterDescription(new RegexParameterDescription("cellpadding", "[0-9]+", false, "", USAGE_HTML));

            registerParameterDescription(new RegexParameterDescription("align", "left|center|right|justify|char",
                            false, "", USAGE_HTML));
            registerParameterDescription(new RegexParameterDescription("valign", "top|middle|bottom|baseline", false,
                            "", USAGE_HTML));

            registerParameterDescription(new RegexParameterDescription("rowspan", "[0-9]+", false, "", USAGE_HTML));
            registerParameterDescription(new RegexParameterDescription("colspan", "[0-9]+", false, "", USAGE_HTML));

            registerParameterDescription(new RegexParameterDescription("namespace", "\\p{Alnum}*", false, "",
                            USAGE_GENERAL));
            registerParameterDescription(new SimpleParameterDescription("plugin", false, "", USAGE_GENERAL));
            registerParameterDescription(new SimpleParameterDescription("name", false, "", USAGE_GENERAL));
            registerParameterDescription(new SimpleParameterDescription("label", false, "", USAGE_GENERAL));
            registerParameterDescription(new SimpleParameterDescription("url", false, "", USAGE_GENERAL));
            registerParameterDescription(new RegexParameterDescription("static", "true|false", false, "", USAGE_GENERAL));
            registerParameterDescription(new RegexParameterDescription("paragraphs", "true|false", false, "",
                            USAGE_GENERAL));
            registerParameterDescription(new RegexParameterDescription("renderLink", "true|false", false, "",
                            USAGE_GENERAL));
            registerParameterDescription(new RegexParameterDescription("ignoreMedia", "true|false", false, "false",
                            USAGE_GENERAL));

            registerParameterDescription(new PageNameParameterDescription("page"));
            registerParameterDescription(new PageNameParameterDescription("log"));

        }
        return (ParameterDescription) _parameterDescriptions.get(name);
    }

    public static void registerParameterDescription(ParameterDescription ad)
    {
        _parameterDescriptions.put(ad.getName(), ad);
    }

    public static void registerParameterDescription(String key, ParameterDescription ad)
    {
        _parameterDescriptions.put(key, ad);
    }

    /**
     * The name of the parameter.
     */
    private String _name;

    private boolean _required;

    private String _defaultValue;

    /**
     * Can be used to determin HOW this parameter will be used, many wiki tags
     * use this field to automatically include values as either HTML attritubes,
     * or CSS styles. See WikiConstants.USAGE_HTML and
     * WikiConstants.USAGE_STYLE.
     * 
     * Many users of Parameters won't care about the usage (for example, most
     * plugins don't make use of this field).
     */
    private String _usage;

    public ParameterDescription(String name)
    {
        this(name, false);
    }

    public ParameterDescription(String name, boolean required)
    {
        this(name, required, "", USAGE_GENERAL);
    }

    public ParameterDescription(String name, boolean required, String usage)
    {
        this(name, required, "", usage);
    }

    public ParameterDescription(String name, String defaultValue, String usage)
    {
        this(name, true, defaultValue, usage);
    }

    public ParameterDescription(String name, boolean required, String defaultValue, String usage)
    {
        _name = name;
        _required = required;
        _defaultValue = defaultValue;
        _usage = usage;
    }

    public String getName()
    {
        return _name;
    }

    public String getUsage()
    {
        return _usage;
    }

    public boolean getRequired()
    {
        return _required;
    }

    public String getDefaultValue()
    {
        return _defaultValue;
    }

    public boolean hasDefaultValue()
    {
        if (_defaultValue == null) {
            return false;
        }
        return !"".equals(_defaultValue);
    }

    /**
     * If the requestedValue is allowed, then it returns the requestedValue in
     * the form of an Parameter, otherwise, it returns null.
     */
    public abstract Parameter allowValue(String requestedValue);

    /**
     * A quick summary of this parameter
     */
    public abstract String getSummary();

    public abstract ParameterDescription required();

    public abstract ParameterDescription defaultValue(String defaultValue);

    public String toString()
    {
        return getName() + " (" + getSummary() + "). required = " + getRequired() + " defaultValue = "
                        + getDefaultValue();
    }

}
