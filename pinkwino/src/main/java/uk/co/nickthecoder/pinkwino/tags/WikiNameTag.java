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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;

/**
 * Displays wiki page name suitable for a heading or html title tag. If the
 * namespace is default then it is omitted, likewise with the relation part.
 */

public class WikiNameTag extends TagSupport
{

    private static final long serialVersionUID = 1L;
    public WikiName _wikiName;

    public WikiNameTag()
    {
        _wikiName = null;
    }

    public void setWikiName(WikiName wikiName)
    {
        _wikiName = wikiName;
    }

    public WikiName getWikiName()
    {
        return _wikiName;
    }

    public int doStartTag() throws JspException
    {
        try {

            String namespace = _wikiName.getNamespace().getName();
            String title = _wikiName.getTitle();
            String relation = _wikiName.getRelation();

            JspWriter out = pageContext.getOut();

            if (_wikiName.getNamespace() != WikiEngine.instance().getDefaultNamespace()) {
                uk.co.nickthecoder.webwidgets.util.TagUtil.printSafeText(out, namespace);
                out.print(" / ");
            }

            uk.co.nickthecoder.webwidgets.util.TagUtil.printSafeText(out, title);

            if (relation != null) {
                out.print(" (");
                uk.co.nickthecoder.webwidgets.util.TagUtil.printSafeText(out, relation);
                out.print(")");
            }

            return SKIP_BODY;

        } catch (IOException e) {
            e.printStackTrace();
            throw new JspException("Unexpected IO Exception.");
        }

    }

}

