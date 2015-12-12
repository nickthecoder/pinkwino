package uk.co.nickthecoder.pinkwino.plugins;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.metadata.LuceneMetaData;
import uk.co.nickthecoder.pinkwino.metadata.LuceneSearchResults;
import uk.co.nickthecoder.pinkwino.metadata.MetaData;
import uk.co.nickthecoder.pinkwino.metadata.SearchResult;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class LucenePageInfoPlugin extends JspPlugin
{

    public LucenePageInfoPlugin()
    {
        this( "lucene", "lucenePageInfo.jsp" );
    }
    
    public LucenePageInfoPlugin(String name, String jspPath)
    {
        super(name, jspPath, JspPlugin.BODY_TYPE_NONE);
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        HttpServletRequest request = getRequest();
        String pageName = request.getParameter("pageName");
        if (pageName == null) {
            pageName = "";
        }
        WikiName wikiName = WikiEngine.instance().getWikiName(pageName);

        jspNode.setRequestAttribute("pageName", pageName);

        if ( (wikiName != null) && (wikiName.getExists())) {

            MetaData md = WikiEngine.instance().getMetaData();
            if (md instanceof LuceneMetaData) {
                LuceneMetaData lmd = (LuceneMetaData) md;
                
                try {
                    LuceneSearchResults lsr = lmd.search( wikiName );
                    Iterator<SearchResult> i = lsr.getIterator();
                    if ( i.hasNext()) {
                        SearchResult result = i.next();
                        jspNode.setRequestAttribute("result", result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (request.getParameter("reindex") != null) {
                    try {
                        lmd.onSave(wikiName.getWikiPage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}
