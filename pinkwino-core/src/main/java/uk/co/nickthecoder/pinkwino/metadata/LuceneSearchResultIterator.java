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

package uk.co.nickthecoder.pinkwino.metadata;

import java.util.Iterator;

public class LuceneSearchResultIterator implements Iterator<SearchResult>
{

    private LuceneSearchResults _results;

    private int _nextIndex;

    public LuceneSearchResultIterator(LuceneSearchResults results)
    {
        _results = results;
        _nextIndex = 0;
    }

    @Override
    public boolean hasNext()
    {
        return _nextIndex < _results.length();
    }

    @Override
    public SearchResult next()
    {
        return _results.get(_nextIndex++);
    }

    @Override
    public void remove()
    {
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

}
