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

import org.apache.lucene.search.Hits;

/**
 * Contains a Lucene Hit object. Each Hit has a lucene Document, which contains
 * the metadata for each wiki page.
 */

public class LuceneSearchResults implements SearchResults
{

    private Hits _hits;

    private Collection<String> _keywords;

    public LuceneSearchResults(Hits hits)
    {
        _hits = hits;
        _keywords = new ArrayList<String>(0);
    }

    public int getLength()
    {
        return _hits.length();
    }

    public int length()
    {
        return _hits.length();
    }

    /** Returns an iteration of SearchResult objects */
    public LuceneSearchResultIterator iterator()
    {
        return new LuceneSearchResultIterator(this);
    }

    public LuceneSearchResultIterator getIterator()
    {
        return iterator();
    }

    public SearchResult get(int n)
    {
        try {
            return new LuceneSearchResult(_hits.doc(n), _hits.score(n));
        } catch (Exception e) {
            System.err.println("LuceneSearchResults: Failed to get hit #" + n);
            return null;
        }
    }

    // MORE - is this used or needed?
    public Collection<String> getKeywords()
    {
        return _keywords;
    }

    public void setKeywords(Collection<String> keywords)
    {
        _keywords = keywords;
    }

    public String toString()
    {
        return super.toString();
    }

}

