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

package uk.co.nickthecoder.pinkwino.plugins;

import javax.servlet.http.HttpServletRequest;

import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.metadata.SearchResults;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class SearchPlugin extends JspPlugin
{

    public SearchPlugin()
    {
        this("search", "search.jsp");
    }

    public SearchPlugin(String name, String jspPath)
    {
        super(name, jspPath, BODY_TYPE_WIKI);
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        HttpServletRequest request = getRequest();
        String searchString = request.getParameter("search");

        jspNode.setRequestAttribute("search_url", wikiDocument.getWikiPage().getViewUrl());

        if (searchString != null) {

            try {

                SearchResults results = WikiEngine.instance().getMetaData().search(searchString);

                jspNode.setRequestAttribute("searchResults", results);

            } catch (Exception e) {
                System.err.println("MetaData.search() failed.");
                e.printStackTrace();
            }

        }

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}
