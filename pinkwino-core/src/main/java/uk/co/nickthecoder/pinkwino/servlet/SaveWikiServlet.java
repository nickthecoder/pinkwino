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
package uk.co.nickthecoder.pinkwino.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Only POST requests are handled here. Unlike the edit servlet, this does NOT
 * handle multi-part forms, so it can't be used to save attachments. This
 * servlet was designed to be used in a ajax framework.
 */

public class SaveWikiServlet extends WikiServlet
{

    private static final long serialVersionUID = 1L;
    protected static Logger _logger = LogManager.getLogger(SaveWikiServlet.class);

    public SaveWikiServlet()
    {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException
    {

        try {

            String data = request.getParameter("data");
            WikiPage wikiPage = getWikiPage(request);
            WikiEngine.instance().save(wikiPage, data, null);

            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println("OK");

        } catch (Exception e) {

            response.setContentType("text/plain");
            response.setStatus(404);
            PrintWriter out = response.getWriter();
            out.println("FAIL");

        }

    }

}
