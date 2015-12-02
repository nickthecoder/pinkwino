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

package uk.co.nickthecoder.pinkwino.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Used by NullMetaData to return zero items for its search results.
 */

public class NullSearchResults implements SearchResults
{

    private static List<SearchResult> emptyResults = new ArrayList<SearchResult>();

    private static List<String> emptyKeywords = new ArrayList<String>();

    public NullSearchResults()
    {
    }

    public int getLength()
    {
        return 0;
    }

    public int length()
    {
        return 0;
    }

    /** Returns an iteration of SearchResult objects */
    public Iterator<SearchResult> iterator()
    {
        return emptyResults.iterator();
    }

    public Iterator<SearchResult> getIterator()
    {
        return iterator();
    }

    public SearchResult get(int n)
    {
        return null;
    }

    public Collection<String> getKeywords()
    {
        return emptyKeywords;
    }

    public void setKeywords(Collection<String> keywords)
    {
    }

    public String toString()
    {
        return super.toString();
    }

}
