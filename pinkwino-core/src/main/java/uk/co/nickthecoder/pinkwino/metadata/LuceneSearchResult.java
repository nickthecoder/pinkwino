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

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.webwidgets.filter.Filter;
import uk.co.nickthecoder.webwidgets.util.SummaryMaker;

public class LuceneSearchResult implements SearchResult
{
    protected static Logger _logger = LogManager.getLogger(LuceneSearchResult.class);

    private Document _document;

    private float _score;
    
    private WikiName _wikiName;

    private LuceneSearchResults _searchResults;
    
    public LuceneSearchResult(Document document, float score, LuceneSearchResults searchResults )
    {
        _document = document;
        _score = score;
        _searchResults = searchResults;
    }

    public String getNamespaceName()
    {
        return LuceneMetaData.loadField(_document.get("namespace"));
    }

    public String getTitle()
    {
        return LuceneMetaData.loadField(_document.get("title"));
    }

    public String getRelation()
    {
        return LuceneMetaData.loadField(_document.get("relation"));
    }

    public String getContent()
    {
        String result = LuceneMetaData.loadField(_document.get("content")); 
        return (result == null) ? "" : result;
    }
    
    public Date getLastUpdated()
    {
        IndexableField result = _document.getField("lastUpdated");
        Number time = result.numericValue();
        
        return new Date( time == null ? 0 : time.longValue());
    }

    @Override
    public float getScore()
    {
        return _score;
    }

    @Override
    public WikiName getWikiName()
    {
        if (_wikiName == null) {
            _wikiName = new WikiName(WikiEngine.instance().getNamespace(getNamespaceName()), getTitle(), getRelation());
        }

        return _wikiName;
    }

    @Override
    public SummaryMaker getSummary()
    {
        return new SummaryMaker( getContent(), new MyKeywordFilter() );
    }

    @Override
    public String toString()
    {
        return "LuceneSearchResult : " + getWikiName() + " = " + getScore();
    }

    
    class MyKeywordFilter implements Filter<String>
    {
        @Override
        public boolean accept(String word)
        {
            String aword = _searchResults._lmd.analyzeWord( word );
            for ( String keyword: _searchResults.keywords ) {
                if ( keyword.equals(aword) ) {
                    return true;
                }
            }
            return false;
        }
        
    }
}
