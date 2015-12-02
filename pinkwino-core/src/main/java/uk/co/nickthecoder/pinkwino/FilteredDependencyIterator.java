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

package uk.co.nickthecoder.pinkwino;

import java.util.Iterator;

/**
 * Returns dependencies of a given type. This uses a one step lookahead of an
 * iteration of Dependency objects
 */

public class FilteredDependencyIterator implements Iterator<WikiName>
{

    private Iterator<Dependency> _dependencies;

    private int _requiredType;

    private Dependency _next;

    public FilteredDependencyIterator(Iterator<Dependency> dependencies, int requiredType)
    {
        _dependencies = dependencies;
        _requiredType = requiredType;
        skip();
    }

    public WikiName next()
    {
        Dependency result = _next;

        skip();

        return result.getWikiName();
    }

    public boolean hasNext()
    {
        return _next != null;
    }

    public void remove()
    {
        throw new RuntimeException("remove not supported");
    }

    /**
     * A bean version of hadNext - Useful for JSP pages.
     */
    public boolean getHasNext()
    {
        return hasNext();
    }

    public WikiName nextWikiName()
    {
        return (WikiName) next();
    }

    protected final void skip()
    {
        while (_dependencies.hasNext()) {
            _next = _dependencies.next();
            if (matches(_next)) {
                return;
            }
        }

        _next = null;
    }

    protected boolean matches(Dependency dep)
    {
        return dep.getType() == _requiredType;
    }

}
