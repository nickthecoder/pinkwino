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

public class SimpleParameterDescription extends ParameterDescription
{

    public SimpleParameterDescription(String name)
    {
        this(name, false, "", WikiConstants.USAGE_GENERAL);
    }

    public SimpleParameterDescription(String name, boolean required, String defaultValue, String usage)
    {
        super(name, required, defaultValue, usage);
    }

    public Parameter allowValue(String requestedValue)
    {
        return new Parameter(this, requestedValue);
    }

    public String getSummary()
    {
        return "";
    }

    public ParameterDescription required()
    {
        return new SimpleParameterDescription(getName(), true, getDefaultValue(), getUsage());
    }

    public ParameterDescription defaultValue(String defaultValue)
    {
        return new SimpleParameterDescription(getName(), getRequired(), defaultValue, getUsage());
    }

}
