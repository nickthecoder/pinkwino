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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * GET requests are forwarded as usual, POST requests are handled here.
 */

public class RenameWikiServlet extends ForwardingWikiServlet
{
    private static final long serialVersionUID = 1L;

    public RenameWikiServlet()
    {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (ensureSecurity(request, response, "You are not authorised to edit this page")) {

            super.doGet(request, response);

        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException
    {

        try {

            if (request.getParameter("rename") != null) {

                String newName = request.getParameter("newName");
                WikiPage oldWikiPage = getWikiPage(request);
                WikiPage newWikiPage = WikiEngine.instance().getWikiPage(newName);

                if (newWikiPage.getExists()) {
                    String message = "That page already exists.";
                    request.setAttribute("message", message);
                    forward(request, response);
                    return;
                }

                // All is well, lets do the rename.

                String templateText = WikiEngine.instance().getMessage("renamed (template)");
                String renamedMessage = templateText.replaceAll("\\$\\{newName\\}", newName);

                WikiEngine wikiEngine = WikiEngine.instance();

                wikiEngine.save(newWikiPage, oldWikiPage.getCurrentVersion().getContent(), null);
                wikiEngine.save(oldWikiPage, renamedMessage, null);

                response.sendRedirect(newWikiPage.getViewUrl());
                return;

            } else if (request.getParameter("cancel") != null) {

                redirectToPageOrReferrer(request, response);

            } else {

                throw new RuntimeException("RenameWikiServlet : Unknown action");

            }

        } catch (Exception e) {

            error(request, response, e);

        }

    }

}
