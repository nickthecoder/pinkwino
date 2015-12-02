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

import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Used when you don't want to store metadata for a wiki. You won't be able to
 * search, or get back-links, or list dependents, but the wiki will still work.
 * This is the default MetaData, but a more complete wiki should use
 * LuceneMetaData, or another implementation of your own creation.
 */

public class NullMetaData implements MetaData
{

    private static SearchResults _nullSearchResults = new NullSearchResults();

    public NullMetaData()
    {
    }

    /**
     * Returns an empty search results
     */
    public SearchResults getBackLinks(WikiName wikiName)
    {
        return _nullSearchResults;
    }

    /**
     * Returns an empty search results
     */
    public SearchResults getDependents(WikiName wikiName)
    {
        return _nullSearchResults;
    }

    /**
     * Does nothing
     */
    public void update(WikiPage wikiPage)
    {
    }

    /**
     * Does nothing
     */
    public void remove(WikiName wikiName)
    {
    }

    public SearchResults search(String searchString)
    {
        return _nullSearchResults;
    }

    public String toString()
    {
        return "NullMetaData";
    }

}
