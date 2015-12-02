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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
*/

public class CompoundAttributes implements Attributes
{

    public List<Attributes> _compound;

    public CompoundAttributes()
    {
        _compound = new ArrayList<Attributes>();
    }

    public void add(Attributes attributes)
    {
        _compound.add(attributes);
    }

    public void setAttribute(String name, Object value)
    {
        ((Attributes) _compound.get(0)).setAttribute(name, value);
    }

    public Object getAttribute(String name)
    {
        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            Object value = attributes.getAttribute(name);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    public Object get(Object key)
    {
        return getAttribute((String) key);
    }

    public void clear()
    {
        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            attributes.clear();
        }
    }

    public boolean containsKey(Object key)
    {
        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            if (attributes.containsKey(key)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsValue(Object value)
    {
        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            if (attributes.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    public Set<Map.Entry<String, Object>> entrySet()
    {
        Set<Map.Entry<String, Object>> result = new HashSet<Map.Entry<String, Object>>();
        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = (Attributes) i.next();

            result.addAll(attributes.entrySet());
        }

        return result;
    }

    public boolean equals(Object o)
    {
        if (o instanceof Attributes) {
            return entrySet().equals(((Attributes) o).entrySet());
        }

        return false;
    }

    public boolean isEmpty()
    {
        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            if (!attributes.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public Set<String> keySet()
    {
        Set<String> keys = new HashSet<String>();
        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            keys.addAll(attributes.keySet());
        }

        return keys;
    }

    public Object put(String key, Object value)
    {
        Object result = get(key);

        setAttribute(key, value);

        return result;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m)
    {
        _compound.get(0).putAll(m);
    }

    @Override
    public Object remove(Object value)
    {
        Object result = null;

        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            result = attributes.remove(value);
        }

        return result;
    }

    public int size()
    {
        int size = 0;

        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            size += attributes.size();
        }
        return size;
    }

    public Collection<Object> values()
    {
        ArrayList<Object> result = new ArrayList<Object>();

        for (Iterator<Attributes> i = _compound.iterator(); i.hasNext();) {
            Attributes attributes = i.next();

            result.addAll(attributes.values());
        }

        return result;
    }

}
