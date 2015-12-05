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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import uk.co.nickthecoder.pinkwino.Dependency;
import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Uses the Lucene search engine to store the meta data.
 */

public class LuceneMetaData implements MetaData
{
    protected static Logger _logger = LogManager.getLogger(LuceneMetaData.class);

    private Analyzer _analyzer;

    private Directory _directory;

    private Searcher _searcher;

    static String saveField(String value)
    {
        if (value == null) {
            return "";
        }
        return value;
    }

    static String loadField(String value)
    {
        if (value.length() == 0) {
            return null;
        }
        return value;
    }

    public LuceneMetaData(File directoryName) throws IOException
    {
        _analyzer = new StandardAnalyzer();
        _directory = FSDirectory.getDirectory(directoryName);
        _searcher = null;
    }

    public void rebuild()
    {
        List<WikiPage> pages = WikiEngine.instance().getPages();

        for (Iterator<WikiPage> i = pages.iterator(); i.hasNext();) {
            WikiPage wikiPage = i.next();

            try {
                update(wikiPage);
            } catch (Exception e) {
            }
        }
    }

    /**
     * Rebuilds the meta data in a new thread. Designed to be called from the
     * wiki's config bsh script.
     */
    public void rebuildAsync()
    {
        Thread thread = (new Thread()
        {

            public void run()
            {
                try {
                    WikiContext.begin(null, null);
                    rebuild();
                } finally {
                    WikiContext.end(null, null);
                }
            }
        });

        thread.start();
    }

    public Analyzer getAnalyzer()
    {
        return _analyzer;
    }

    public Directory getDirectory()
    {
        return _directory;
    }

    public Searcher getSearcher()
    {
        if (_searcher == null) {
            try {
                _searcher = new IndexSearcher(_directory);
            } catch (Exception e) {
                System.err.println("Failed to open the index searcher");
                e.printStackTrace();
            }
        }
        return _searcher;
    }

    private void reopen()
    {
        _searcher = null;
    }

    /**
     * Lists all wiki pages which link to the given page. This does NOT include
     * wiki pages which are *dependant* on the given page, so, for example, if
     * page Foo include page Bar, then Foo will NOT be returned. See also
     * getBackDependencies.
     */
    public SearchResults getBackLinks(WikiName wikiName) throws IOException
    {
        TermQuery backLinksQuery = new TermQuery(new Term("link", wikiName.getFormatted()));

        Hits hits = getSearcher().search(backLinksQuery);

        return new LuceneSearchResults(hits);
    }

    /**
     * Lists all wiki pages which depend on the given page. i.e. all wiki pages
     * which *include* the given page, and all wiki pages who's image is used
     * within the given page etc.
     * 
     * Note, only 1 level of dependencies are returned. So if A depends on B and
     * B depends on C, then C will NOT be returned as a dependency of A.
     */
    public SearchResults getDependents(WikiName wikiName) throws IOException
    {
        TermQuery backLinksQuery = new TermQuery(new Term("dependency", wikiName.getFormatted()));

        Hits hits = getSearcher().search(backLinksQuery);

        return new LuceneSearchResults(hits);
    }

    /** Update the meta data in lucene for the given page */
    public void update(WikiPage wikiPage) throws IOException
    {

        Document document = new Document();
        WikiName wikiName = wikiPage.getWikiName();

        document.add(new Field("wikiName", saveField(wikiName.getFormatted()), Field.Store.YES,
                        Field.Index.UN_TOKENIZED));
        document.add(new Field("namespace", saveField(wikiName.getNamespace().getName()), Field.Store.YES,
                        Field.Index.UN_TOKENIZED));
        document.add(new Field("title", saveField(wikiName.getTitle()), Field.Store.YES, Field.Index.UN_TOKENIZED));
        document.add(new Field("relation", saveField(wikiName.getRelation()), Field.Store.YES, Field.Index.UN_TOKENIZED));
        document.add(new Field("content", saveField(wikiPage.getCurrentVersion().getContent()), Field.Store.YES,
                        Field.Index.TOKENIZED));

        for (Iterator<Dependency> i = wikiPage.getCurrentVersion().getWikiDocument().getDependencies().iterator(); i
                        .hasNext();) {

            Dependency dependency = i.next();

            if (dependency.isLink()) {

                document.add(new Field("link", dependency.getWikiName().getFormatted(), Field.Store.YES,
                                Field.Index.UN_TOKENIZED));

            } else {

                document.add(new Field("dependency", dependency.getWikiName().getFormatted(), Field.Store.YES,
                                Field.Index.UN_TOKENIZED));

            }
        }

        IndexWriter writer = new IndexWriter(getDirectory(), getAnalyzer());
        writer.updateDocument(getWikiNameTerm(wikiName), document, getAnalyzer());
        writer.close();
        reopen();

    }

    /** Remove the page from lucene */
    public void remove(WikiName wikiName) throws IOException
    {

        IndexWriter writer = new IndexWriter(getDirectory(), getAnalyzer());
        writer.deleteDocuments(getWikiNameTerm(wikiName));
        writer.close();

    }

    public SearchResults search(String searchString) throws Exception
    {
        Query query = createSearchQuery(searchString);

        Hits hits = getSearcher().search(query);

        return new LuceneSearchResults(hits);
    }

    private Query createSearchQuery(String searchString) throws Exception
    {
        BooleanQuery query = new BooleanQuery();

        StringTokenizer st = new StringTokenizer(searchString);
        while (st.hasMoreTokens()) {

            // Words prefixed with "-" are NOT included in the search results.
            String word = st.nextToken();
            BooleanClause.Occur include = BooleanClause.Occur.MUST;
            if (word.startsWith("-")) {
                include = BooleanClause.Occur.MUST_NOT;
                word = word.substring(1);
            }

            Reader reader = new StringReader(word);
            TokenStream ts = getAnalyzer().tokenStream(null, reader);

            Token token = ts.next();
            while (token != null) {
                Term term = new Term("content", new String(token.termBuffer(), 0, token.termLength()));
                query.add(new TermQuery(term), include);

                token = ts.next();
            }
        }

        return query;
    }

    public Term getWikiNameTerm(WikiName wikiName)
    {
        return new Term("wikiName", wikiName.getFormatted());
    }

    public void debugInfo(WikiName wikiName)
    {
        try {
            System.out.println("LuceneMetaData debug info for : " + wikiName);
            TermQuery query = new TermQuery(getWikiNameTerm(wikiName));

            Hits hits = getSearcher().search(query);

            for (Iterator<Hit> i = hits.iterator(); i.hasNext();) {
                Hit hit = i.next();
                Document document = hit.getDocument();

                List<Field> fields = document.getFields();
                for (Iterator<Field> fi = fields.iterator(); fi.hasNext();) {
                    Field field = (Field) fi.next();
                    System.out.println(field.name() + " = " + field.stringValue());
                }
                System.out.println();
            }
            System.out.println("End LuceneMetaData debug info for : " + wikiName);

        } catch (Exception e) {
        }
    }

    public String toString()
    {
        return super.toString();
    }

}
