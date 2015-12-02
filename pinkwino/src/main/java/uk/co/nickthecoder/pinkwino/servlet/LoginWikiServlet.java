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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiEngine;

/**
 * GET requests are forwarded as usual, POST requests are handled here.
 */
public class LoginWikiServlet extends ForwardingWikiServlet
{

    private static final long serialVersionUID = 1L;

    protected static Logger _logger = LogManager.getLogger(LoginWikiServlet.class);

    public LoginWikiServlet()
    {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException
    {
        try {

            if (request.getParameter("ok") != null) {

                String userName = request.getParameter("userName");
                String password = request.getParameter("password");

                _logger.info("Logging in as " + userName);

                WikiEngine wikiEngine = WikiEngine.instance();
                if (wikiEngine.getAuthenticationManager().login(userName, password)) {

                    _logger.info("Password OK for user : " + userName);
                    redirectReferrer(request, response);

                } else {

                    _logger.info("Password INCORRECT for user : " + userName);

                    request.setAttribute("message", "Incorrect user name password combination.");
                    forward(request, response);

                }

            } else if (request.getParameter("cancel") != null) {

                // Redirect to the referer page.
                redirectReferrer(request, response);

            } else {

                _logger.error("neither ok nor cancel pressed");
                throw new RuntimeException("Login Wiki : unknown command");

            }

        } catch (Exception e) {

            error(request, response, e);

        }
    }

}
