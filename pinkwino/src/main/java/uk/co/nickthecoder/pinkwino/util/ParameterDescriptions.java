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

package uk.co.nickthecoder.pinkwino.util;

import java.util.HashMap;
import java.util.Iterator;

public class ParameterDescriptions
{

    private HashMap<String, ParameterDescription> _parameterDescriptions;

    /**
     * If true, then unknown parameters are allowed, and mock
     * parameterDescriptions are created to describe them. This was put in place
     * to allow the template plugin to have named parameters, which are
     * uknowable at compile time.
     */
    private boolean _allowUnknown;

    public ParameterDescriptions()
    {
        this(false);
    }

    public ParameterDescriptions(boolean allowUnknown)
    {
        _parameterDescriptions = new HashMap<String, ParameterDescription>();
        _allowUnknown = allowUnknown;
    }

    public void addParameterDescription(ParameterDescription ad)
    {
        _parameterDescriptions.put(ad.getName(), ad);
    }

    public ParameterDescription getParameterDescription(String name)
    {
        return (ParameterDescription) _parameterDescriptions.get(name);
    }

    public Iterator<ParameterDescription> getParameterDescriptions()
    {
        return iterator();
    }

    public Iterator<ParameterDescription> iterator()
    {
        return _parameterDescriptions.values().iterator();
    }

    public int size()
    {
        return _parameterDescriptions.size();
    }

    public boolean getAllowUnknown()
    {
        return _allowUnknown;
    }

    public void setAllowUnknown(boolean value)
    {
        _allowUnknown = value;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ParameterDescriptions: ");
        for (Iterator<ParameterDescription> i = iterator(); i.hasNext();) {
            ParameterDescription pd = i.next();
            buffer.append(pd.getName());
        }
        return buffer.toString();
    }

}
