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

import uk.co.nickthecoder.pinkwino.WikiConstants;

/**
 * Defines the name, and allowable values for an parameter
 */

public class IntParameterDescription extends ParameterDescription
{

    private int _maximum;

    private int _minimum;

    public IntParameterDescription(String name)
    {
        this(name, false, "0", WikiConstants.USAGE_GENERAL);
    }

    public IntParameterDescription(String name, boolean required, String defaultValue, String usage)
    {
        this(name, Integer.MIN_VALUE, Integer.MAX_VALUE, required, defaultValue, usage);
    }

    public IntParameterDescription(String name, int min, int max, boolean required, String defaultValue, String usage)
    {
        super(name, required, defaultValue, usage);
        _minimum = min;
        _maximum = max;
    }

    public Parameter allowValue(String requestedValue)
    {
        try {

            int value = Integer.parseInt(requestedValue);
            if ((value >= _minimum) && (value <= _maximum)) {
                return new IntParameter(this, value);
            }

        } catch (Exception e) {
            // Do nothing
        }

        return null;
    }

    public String getSummary()
    {
        return "int(" + _minimum + "..." + _maximum;
    }

    public ParameterDescription required()
    {
        return new IntParameterDescription(getName(), _minimum, _maximum, true, getDefaultValue(), getUsage());
    }

    public ParameterDescription defaultValue(String defaultValue)
    {
        return new IntParameterDescription(getName(), _minimum, _maximum, getRequired(), defaultValue, getUsage());
    }

    public IntParameterDescription minimum(int min)
    {
        return new IntParameterDescription(getName(), min, _maximum, getRequired(), getDefaultValue(), getUsage());
    }

    public IntParameterDescription maximum(int max)
    {
        return new IntParameterDescription(getName(), _minimum, max, getRequired(), getDefaultValue(), getUsage());
    }

    public IntParameterDescription range(int min, int max)
    {
        return new IntParameterDescription(getName(), min, max, getRequired(), getDefaultValue(), getUsage());
    }

}
