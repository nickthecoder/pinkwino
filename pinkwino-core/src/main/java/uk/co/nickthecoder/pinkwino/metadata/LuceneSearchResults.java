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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 * Contains a Lucene Hit object. Each Hit has a lucene Document, which contains
 * the metadata for each wiki page.
 */

public class LuceneSearchResults implements SearchResults
{
    
    private static final Logger _logger = LogManager.getLogger(LuceneSearchResults.class);

    private IndexSearcher _indexSearcher;

    private TopDocs _topDocs;

    private Collection<String> _keywords;

    public LuceneSearchResults(IndexSearcher indexSearcher, TopDocs topDocs)
    {
        _indexSearcher = indexSearcher;
        _topDocs = topDocs;
        _keywords = new ArrayList<String>(0);
    }

    public int getLength()
    {
        return _topDocs.scoreDocs.length;
    }

    public int length()
    {
        return _topDocs.scoreDocs.length;
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
            ScoreDoc scoreDoc = _topDocs.scoreDocs[n];
            Document doc = _indexSearcher.doc( scoreDoc.doc );
            return new LuceneSearchResult( doc, scoreDoc.score);
        } catch (Exception e) {
            _logger.error("LuceneSearchResults: Failed to get hit #" + n);
            e.printStackTrace();
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

