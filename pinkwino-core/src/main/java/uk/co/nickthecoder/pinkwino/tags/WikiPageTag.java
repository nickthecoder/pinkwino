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

package uk.co.nickthecoder.pinkwino.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Adds a WikiPage into the request scope
 */

public class WikiPageTag extends TagSupport implements TryCatchFinally
{

    private static final long serialVersionUID = 1L;

    /**
     * The name of the namespace
     */
    private String _namespace;

    /**
     * The title of the page
     */
    private String _title;

    private String _relation;

    /**
     * The name of the request attribute to save the WikiPage under. If not
     * specified, then <i>wikiPage</i> is used.
     */
    private String _wikiPageVar;

    private Object _previousWikiPageVarValue;

    public WikiPageTag()
    {
        _title = null;
        _namespace = null;
        _relation = null;
        _wikiPageVar = "wikiPage";
    }

    public String getNamespace()
    {
        return _namespace;
    }

    public void setNamespace(String value)
    {
        _namespace = value;
    }

    public String getRelation()
    {
        return _relation;
    }

    public void setRelation(String value)
    {
        _relation = value;
    }

    public String getTitle()
    {
        return _title;
    }

    public void setTitle(String value)
    {
        _title = value;
    }

    public void setWikiPageVar(String value)
    {
        _wikiPageVar = value;
    }

    public String getWikiPageVar()
    {
        return _wikiPageVar;
    }

    /**
     * Returns the WikiPage for the specified page id. If the page does not
     * exist, then null is NOT returned, instead, a WikiPage instance is
     * created, which has the correct title, but no content.
     */
    public WikiPage getWikiPage()
    {
        WikiEngine engine = WikiEngine.instance();
        WikiName wikiName = WikiName.create(_namespace, _title, _relation);

        return engine.getWikiPage(wikiName);
    }

    public int doStartTag() throws JspException
    {
        WikiContext.begin((HttpServletRequest) pageContext.getRequest(),
                        (HttpServletResponse) pageContext.getResponse());
        _previousWikiPageVarValue = pageContext.getAttribute(_wikiPageVar);

        WikiPage wikiPage = getWikiPage();

        pageContext.setAttribute(_wikiPageVar, wikiPage);

        return EVAL_BODY_INCLUDE;
    }

    public void doCatch(Throwable throwable)
    {
    }

    public void doFinally()
    {
        pageContext.setAttribute(_wikiPageVar, _previousWikiPageVarValue);

        WikiContext.end((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
    }

}

