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

import uk.co.nickthecoder.pinkwino.Version;

/**
 * GET requests are forwarded as usual, POST requests are handled here. NOTE on
 * post, we will be using multipart, because if the page contains an attachment,
 * then this servlet needs to handle the upload of the file.
 */

public class RawWikiServlet extends WikiServlet
{

    private static final long serialVersionUID = 1L;

    public RawWikiServlet()
    {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Version version = getVersion(request);

        if (version.getWikiPage().getWikiName().isText()) {

            if (version.getWikiPage().getWikiName().getMimeType().isSafe()) {
                response.setHeader("Content-Type", version.getWikiPage().getWikiName().getMimeType().getMimeType());
            } else {
                response.setHeader("Content-Type", "text/plain");
            }

        } else {
            response.setHeader("Content-Type", "text/plain");
        }

        response.setCharacterEncoding(getCharacterEncoding());

        try {

            PrintWriter out = response.getWriter();

            out.print(version.getContent());
            out.flush();

        } catch (Exception e) {
            error(request, response, e);
        }
    }

}
