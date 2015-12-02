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


public class Parameter implements Comparable<Parameter>
{

    public ParameterDescription _parameterDescription;

    private String _value;

    public Parameter(String parameterName, String value)
    {
        this(new SimpleParameterDescription(parameterName), value);
    }

    public Parameter(ParameterDescription parameterDescription, String value)
    {
        _parameterDescription = parameterDescription;
        _value = value;
    }

    public ParameterDescription getParameterDescription()
    {
        return _parameterDescription;
    }

    public String getName()
    {
        return _parameterDescription.getName();
    }

    public String getValue()
    {
        return _value;
    }

    public int getIntValue()
    {
        return Integer.parseInt(_value);
    }

    public String getUsage()
    {
        return _parameterDescription.getUsage();
    }

    public int compareTo(Parameter other)
    {
        return getName().compareTo(other.getName());
    }

    public String toString()
    {
        return getParameterDescription().getName() + " = " + getValue();
    }

}

