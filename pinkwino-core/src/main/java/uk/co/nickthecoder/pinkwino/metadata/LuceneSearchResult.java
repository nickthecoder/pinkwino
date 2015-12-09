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

import org.apache.lucene.document.Document;

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;


public class LuceneSearchResult implements SearchResult
{    
    private Document _document;

    private float _score;

    private WikiName _wikiName;

    public LuceneSearchResult(Document document, float score)
    {
        _document = document;
        _score = score;
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

    public float getScore()
    {
        return _score;
    }

    public WikiName getWikiName()
    {
        if (_wikiName == null) {
            _wikiName = new WikiName(WikiEngine.instance().getNamespace(getNamespaceName()), getTitle(), getRelation());
        }

        return _wikiName;
    }

    public String toString()
    {
        return "LuceneSearchResult : " + getWikiName() + " = " + getScore();
    }

}
