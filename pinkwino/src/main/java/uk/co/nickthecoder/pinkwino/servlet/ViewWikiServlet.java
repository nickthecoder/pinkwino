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

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.link.ViewHandler;

public class ViewWikiServlet extends ForwardingWikiServlet
{

    private static final long serialVersionUID = 1L;

    public ViewWikiServlet()
    {
        super();
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException
    {
        WikiName wikiName = getWikiName(request);
        Namespace namespace = wikiName.getNamespace();
        ViewHandler viewHandler = namespace.getViewHandler();

        if (viewHandler == null) {

            forward(request, response, "view");

        } else {
            boolean result;
            int versionNumber = getVersionNumber(request);

            if (versionNumber <= 0) {
                result = viewHandler.view(request, response, wikiName);
            } else {
                result = viewHandler.view(request, response, wikiName, versionNumber);
            }
            if (result == false) {
                super.forward(request, response);
            }
        }
    }
}
