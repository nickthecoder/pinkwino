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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Intercepts the getParameter method, and adds a new parameter "page", which
 * will be the wiki page name. Used by serlvets, so that the page parameters can
 * be expressed as either a normal parameter, or as path info.
 */

public class PageNameRequestWrapper extends HttpServletRequestWrapper
{

    private HttpServletRequest _request;

    private String _pagePath;

    // TODO Remove this class when I'm sure its no longer needed.
    private PageNameRequestWrapper(HttpServletRequest request, String pagePath)
    {
        super(request);
        _pagePath = pagePath;
        _request = request;
    }

    /**
     * If the page name is specified using path info, then this hides that fact
     * ie its as if there IS a parameter called "page", with the appropriate
     * value.
     */
    public String getParameter(String name)
    {
        if (name.equals("page")) {
            return _pagePath;
        }

        return super.getParameter(name);
    }

    /**
     * I want to know the URL of the page that the user requested, not the jsp
     * page that renders the page. This is important for the breadcrumbs to
     * work.
     */
    public StringBuffer getRequestURL()
    {
        return _request.getRequestURL();

    }

}
