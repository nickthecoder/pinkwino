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
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Parameters
{

    public HashMap<String, Parameter> _parameters;
    public SortedSet<Parameter> _sortedParameters;

    public Parameters()
    {
        _parameters = new HashMap<String, Parameter>();
        _sortedParameters = new TreeSet<Parameter>();
    }

    public Parameters(Parameters parameters)
    {
        this();
        for (Iterator<Parameter> i = parameters.iterator(); i.hasNext();) {
            Parameter p = (Parameter) i.next();
            addParameter(p);
        }
    }

    public void addParameter(Parameter parameter)
    {
        Parameter previous = (Parameter) _parameters.get(parameter.getName());
        if (previous != null) {
            _parameters.remove(previous);
            _sortedParameters.remove(previous);
        }
        _parameters.put(parameter.getName(), parameter);
        _sortedParameters.add(parameter);
    }

    public Parameter getParameter(String name)
    {
        return (Parameter) _parameters.get(name);
    }

    public Iterator<Parameter> iterator()
    {
        return _sortedParameters.iterator();
    }

    public HashMap<String, Parameter> getHashMap()
    {
        return _parameters;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        for (Iterator<Parameter> i = iterator(); i.hasNext();) {
            Parameter parameter = i.next();

            buffer.append("(").append(parameter).append(") ");
        }

        return buffer.toString();
    }

}
