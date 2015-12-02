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

package uk.co.nickthecoder.pinkwino.servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * GET requests are forwarded as usual, POST requests are handled here.
 */
public class DeleteWikiServlet extends ForwardingWikiServlet
{

    private static final long serialVersionUID = 1L;

    protected static Logger _logger = LogManager.getLogger(ForwardingWikiServlet.class);

    public DeleteWikiServlet()
    {
        super();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Now deleting a page has different requirements to editing a page, so
        // this test no longer applies
        // if ( ensureSecurity( request, response,
        // "You are not authorised to delete this page." ) ) {

        WikiEngine wikiEngine = WikiEngine.instance();
        WikiPage wikiPage = getWikiPage(request);

        if (wikiEngine.canDelete(wikiPage)) {
            forward(request, response, "delete");
        } else {
            _logger.info("Not authorised to delete wiki page " + wikiPage);
            forward(request, response, "notAuthorised");
        }

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException
    {
        try {

            WikiEngine wikiEngine = WikiEngine.instance();
            WikiPage wikiPage = getWikiPage(request);

            if (!wikiEngine.canDelete(wikiPage)) {
                throw new Exception("Illegal attempt to delete the wiki page " + wikiPage);
            }

            if (request.getParameter("delete") != null) {

                if (wikiPage.getWikiName().getRelation() == null) {
                    for (Iterator<WikiPage> i = wikiPage.getRelatedPages().iterator(); i.hasNext();) {

                        WikiPage relatedPage = i.next();
                        _logger.info("Deleting related page : " + relatedPage.getWikiName());
                        WikiEngine.instance().delete(relatedPage);

                    }
                }

                _logger.info("Deleting page : " + wikiPage.getWikiName());
                WikiEngine.instance().delete(wikiPage);

                redirectToPageOrReferrer(request, response);

            } else if (request.getParameter("cancel") != null) {

                redirectToPageOrReferrer(request, response);

            } else {

                throw new RuntimeException("Delete Wiki : unknown command");

            }

        } catch (Exception e) {

            error(request, response, e);

        }
    }

}
