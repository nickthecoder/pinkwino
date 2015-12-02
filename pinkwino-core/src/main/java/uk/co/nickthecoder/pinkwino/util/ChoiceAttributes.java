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

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public class ChoiceAttributes implements Attributes
{
    private Choice _choice;

    private Attributes _trueAttributes;

    private Attributes _falseAttributes;

    public ChoiceAttributes(Choice choice, Attributes trueAttributes, Attributes falseAttributes)
    {
        _choice = choice;
        _trueAttributes = trueAttributes;
        _falseAttributes = falseAttributes;
    }

    public Attributes getChoiceAttributes()
    {
        if (_choice.choice()) {
            return _trueAttributes;
        } else {
            return _falseAttributes;
        }
    }

    public void setAttribute(String name, Object value)
    {
        getChoiceAttributes().setAttribute(name, value);
    }

    public Object getAttribute(String name)
    {
        return getChoiceAttributes().getAttribute(name);
    }

    public Object get(Object key)
    {
        return getChoiceAttributes().get(key);
    }

    public void clear()
    {
        _trueAttributes.clear();
        _falseAttributes.clear();
    }

    public boolean containsKey(Object key)
    {
        return getChoiceAttributes().containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return getChoiceAttributes().containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet()
    {
        return getChoiceAttributes().entrySet();
    }
    
    public boolean equals(Object o)
    {
        if (o instanceof ChoiceAttributes) {
            return equals((ChoiceAttributes) o);
        } else {
            return false;
        }
    }

    public boolean equals(ChoiceAttributes o)
    {
        return _trueAttributes.equals(o._trueAttributes) && _falseAttributes.equals(o._falseAttributes);
    }

    public boolean isEmpty()
    {
        return getChoiceAttributes().isEmpty();
    }

    public Set<String> keySet()
    {
        return getChoiceAttributes().keySet();
    }

    public Object remove(Object key)
    {
        return getChoiceAttributes().remove(key);
    }

    public int size()
    {
        return getChoiceAttributes().size();
    }

    @Override
    public Object put(String key, Object value)
    {
        return getChoiceAttributes().put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m)
    {
        getChoiceAttributes().putAll(m);
    }

    @Override
    public Collection<Object> values()
    {
        return getChoiceAttributes().values();
    }


}
// ---------- End Of Class ChoiceAttributes ----------

