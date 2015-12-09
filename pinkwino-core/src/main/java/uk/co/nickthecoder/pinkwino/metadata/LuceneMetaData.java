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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;

import uk.co.nickthecoder.pinkwino.Dependency;
import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.WikiPageListener;

/**
 * Uses the Lucene search engine to store the meta data.
 */

public class LuceneMetaData implements MetaData, WikiPageListener
{
    protected static Logger _logger = LogManager.getLogger(LuceneMetaData.class);

    private Analyzer _analyzer;

    private Directory _directory;

    private int _openWriterCount = 0;

    private IndexWriter _writer;

    private DirectoryReader _reader;

    private IndexSearcher _indexSearcher;

    
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

    private synchronized void openWriter() throws IOException
    {
        if (_writer == null) {
            if (_openWriterCount != 0) {
                _logger.error("Open count should be zero when writer is closed");
            }
            IndexWriterConfig config = new IndexWriterConfig(_analyzer);
            _writer = new IndexWriter(_directory, config);
            _openWriterCount++;
        }
    }

    private synchronized void closeWriter() throws CorruptIndexException, IOException
    {
        if (_openWriterCount <= 0) {
            _logger.error("Closed the IndexWriter without opening it");
        }

        _openWriterCount--;
        if (_openWriterCount == 0) {
            if (_writer == null) {
                _logger.error("Attempted to close the IndexWriter without opening it");
            } else {
                _writer.close();
                _writer = null;
            }
        }
    }

    public LuceneMetaData(File directoryName) throws IOException
    {
        this(directoryName, new EnglishAnalyzer() );
    }
    
    public LuceneMetaData(File directoryName, Analyzer analyzer) throws IOException
    {
        _analyzer = analyzer;
        _directory = new NIOFSDirectory(Paths.get(directoryName.getPath()));
    }

    public void rebuild() throws CorruptIndexException, LockObtainFailedException, IOException
    {
        List<WikiPage> pages = WikiEngine.instance().getPages();

        openWriter();
        try {
            for (WikiPage wikiPage : pages) {
                try {
                    update(wikiPage.getWikiName(), createDocument(wikiPage));
                } catch (Exception e) {
                    _logger.error("Failed to update page " + wikiPage.getWikiName().getFormatted());
                }
            }
        } finally {
            closeWriter();
        }

    }

    /**
     * Rebuilds the meta data in a new thread.
     */
    public void rebuildAsync()
    {
        Thread thread = (new Thread()
        {
            @Override
            public void run()
            {
                try {
                    WikiContext.begin(null, null);
                    rebuild();
                } catch (Exception e) {
                    _logger.error("Failed to rebuild Lucene index. " + e);
                    e.printStackTrace();
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

    private IndexSearcher createIndexSearcher() throws IOException
    {
        if (_reader == null) {
            _reader = DirectoryReader.open(_directory);
        } else {
            DirectoryReader reader = DirectoryReader.openIfChanged(_reader);
            if (reader != null) {
                _reader = reader;
                _indexSearcher = null;
            }
        }

        if (_indexSearcher == null) {
            _indexSearcher = new IndexSearcher(_reader);
        }

        return _indexSearcher;
    }

    @Override
    public SearchResults search(String searchString) throws Exception
    {
        LuceneTextSearch lts = new LuceneTextSearch( searchString );
        
        LuceneSearchResults results = search( lts.query );
        results.keywords = lts.keywords;

        return results;
    }

    public LuceneSearchResults search(Query query) throws IOException
    {
        IndexSearcher indexSearcher = createIndexSearcher();

        TopDocs topDocs = query(indexSearcher, query, 1, 100);
        return new LuceneSearchResults(this, indexSearcher, topDocs);
    }

    private TopDocs query(IndexSearcher searcher, Query q, int pageNumber, int hitsPerPage) throws IOException
    {
        TopScoreDocCollector collector = TopScoreDocCollector.create(pageNumber * hitsPerPage);
        searcher.search(q, collector);
        return collector.topDocs((pageNumber - 1) * hitsPerPage, hitsPerPage);
    }

    
    /**
     * Lists all wiki pages which link to the given page. This does NOT include
     * wiki pages which are *dependant* on the given page, so, for example, if
     * page Foo include page Bar, then Foo will NOT be returned. See also
     * getBackDependencies.
     */
    @Override
    public SearchResults getBackLinks(WikiName wikiName) throws IOException
    {
        TermQuery query = new TermQuery(new Term("link", wikiName.getFormatted()));
        return search(query);
    }


    public String analyzeWord( String word )
    {
        Reader reader = new StringReader(word);
        TokenStream tokenStream = null;
        try {
            tokenStream = _analyzer.tokenStream("content", reader);
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                return charTermAttribute.toString();
            }
        } catch (Exception e) {
            _logger.error( "Failed to filter a keyword. " + e );
        } finally {
            try {
                if (tokenStream != null) {
                    tokenStream.end();
                    tokenStream.close();
                }
                reader.close();
            } catch (Exception e) {
                // Do nothing
                _logger.error("Failed to close during analyzeWord " + e);
            }
        }
        return null;
    }
    
    /**
     * Builds a Lucene Query from a string. A search string is parsed into individual keywords, and
     * a query built based on those words.
     */
    public class LuceneTextSearch
    {
        public List<String> keywords;
        
        public Query query;

        public LuceneTextSearch( String queryString )
        {
            keywords = new ArrayList<String>();
            BooleanQuery.Builder builder = new BooleanQuery.Builder();

            StringTokenizer st = new StringTokenizer(queryString);
            while (st.hasMoreTokens()) {

                BooleanClause.Occur occur = BooleanClause.Occur.SHOULD;
                String word = st.nextToken();

                if (word.startsWith("-")) {
                    occur = BooleanClause.Occur.MUST_NOT;
                    word = word.substring(1);
                } else if (word.startsWith("+")) {
                    occur = BooleanClause.Occur.MUST;
                    word = word.substring(1);
                } else if (word.startsWith("?")) {
                    occur = BooleanClause.Occur.SHOULD;
                    word = word.substring(1);
                }

                _logger.trace("Query word : '" + word + "'");

                String keyword = analyzeWord( word );
                if ( keyword != null ) {
                    _logger.trace("Query term : '" + keyword + "'");
                    keywords.add( keyword );
                        
                    builder.add(new TermQuery(new Term("content", keyword)), occur);

                    if (occur != BooleanClause.Occur.MUST) {
                        builder.add(new TermQuery(new Term("title", keyword)), occur);
                    }
                }
            }
            query = builder.build();

        }

    }


    public Term getWikiNameTerm(WikiName wikiName)
    {
        return new Term("wikiName", wikiName.getFormatted());
    }

    /**
     * Lists all wiki pages which depend on the given page. i.e. all wiki pages
     * which *include* the given page, and all wiki pages who's image is used
     * within the given page etc.
     *
     * Note, only 1 level of dependencies are returned. So if A depends on B and
     * B depends on C, then C will NOT be returned as a dependency of A.
     */
    @Override
    public SearchResults getDependents(WikiName wikiName) throws IOException
    {
        TermQuery query = new TermQuery(new Term("dependency", wikiName.getFormatted()));
        return search(query);
    }

    @Override
    public void onSave(WikiPage wikiPage) throws Exception
    {
        openWriter();
        try {
            update(wikiPage.getWikiName(), createDocument(wikiPage));
        } finally {
            closeWriter();
        }
    }

    protected Document createDocument(WikiPage wikiPage)
    {
        Document document = new Document();
        WikiName wikiName = wikiPage.getWikiName();

        String content = wikiPage.getCurrentVersion().getPlainContent();
        
        document.add(new StringField("wikiName", saveField(wikiName.getFormatted()), Field.Store.YES));
        document.add(new StringField("namespace", saveField(wikiName.getNamespace().getName()), Field.Store.YES));
        document.add(new TextField("title", saveField(wikiName.getTitle()), Field.Store.YES));
        document.add(new StringField("relation", saveField(wikiName.getRelation()), Field.Store.YES));
        document.add(new TextField("content", saveField(content), Field.Store.YES));
        document.add(new LongField("lastUpdated", wikiPage.getCurrentVersion().getDate().getTime(), Field.Store.YES));
        document.add(new LongField("lastIndexed", new Date().getTime(), Field.Store.YES));
        
        Collection<Dependency> dependencies = wikiPage.getCurrentVersion().getWikiDocument().getDependencies();
        for (Dependency dependency : dependencies) {

            if (dependency.isLink()) {

                _logger.trace("Adding link : " + dependency.getWikiName());
                document.add(new StringField("link", dependency.getWikiName().getFormatted(), Field.Store.YES));

            } else {
                _logger.trace("Adding dependency : " + dependency.getWikiName());
                document.add(new StringField("dependency", dependency.getWikiName().getFormatted(), Field.Store.YES));

            }
        }
        return document;
    }

    @Override
    public void onDelete(WikiPage wikiPage) throws Exception
    {
        openWriter();
        try {
            delete(wikiPage.getWikiName());
        } finally {
            closeWriter();
        }

    }

    protected void update(WikiName wikiName, Document document) throws CorruptIndexException, IOException
    {
        _writer.updateDocument(getWikiNameTerm(wikiName), document);
    }

    protected void delete(WikiName wikiName) throws CorruptIndexException, IOException
    {
        _writer.deleteDocuments(getWikiNameTerm(wikiName));
    }

    /**
     * Removes all documents from the index who's lastUpdate is smaller than
     * <code>time</code>.
     *
     * @param lastUpdate
     *            Based on the number of milliseconds since January 1, 1970,
     *            00:00:00 GMT. See {@link java.util.Date#time()}
     */
    protected void purge(long time)
    {
        try {
            Query query = NumericRangeQuery.newLongRange("lastIndexed", 0l, time - 1, true, true);
            _writer.deleteDocuments(query);

        } catch (Exception e) {
            e.printStackTrace();
            _logger.error("Failed to purge. " + e);
        }
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    
}
