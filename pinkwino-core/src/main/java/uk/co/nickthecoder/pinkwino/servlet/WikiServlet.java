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

// {{{ imports
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.UserMessageException;
import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.link.UrlManager;
import uk.co.nickthecoder.pinkwino.media.MimeType;

public class WikiServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    public static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occured.";

    protected static Logger _logger = LogManager.getLogger(WikiServlet.class);

    public WikiServlet()
    {
        super();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    java.io.IOException
    {
        try {
            WikiContext.begin(request, response);

            try {
                WikiPage wikiPage = getWikiPage(request);
                WikiContext.getWikiContext().setWikiPage(wikiPage);

                request.setAttribute("wikiEngine", WikiEngine.instance());
                request.setAttribute("wikiPage", getWikiPage(request));
                request.setAttribute("version", getVersion(request));

            } catch (Exception e) {
            }

            super.service(request, response);

        } finally {

            WikiContext.end(request, response);

        }
    }

    public String getPagePath(HttpServletRequest request) throws ServletException
    {
        String path = request.getPathInfo();

        if ((path == null) || (path.length() < 2)) {

            path = request.getParameter("page");

        } else {
            // remove the leading "/", and decode
            try {
                path = URLDecoder.decode(path.substring(1), "UTF-8");
            } catch (Exception e) {
                // Never
            }
        }

        return path;
    }

    public WikiName getWikiName(HttpServletRequest request)
    {
        WikiEngine wikiEngine = WikiEngine.instance();
        String pagePath = null;

        try {
            pagePath = getPagePath(request);
        } catch (Exception e) {
            System.err.println("WikiServlet: A problem getting page name, using default. " + e);
        }

        if (pagePath == null) {
            pagePath = wikiEngine.getDefaultPageName();
        }

        WikiName wikiName = wikiEngine.getUrlManager().decodePageParameter(pagePath);

        return wikiName;
    }

    public WikiPage getWikiPage(HttpServletRequest request) throws ServletException
    {
        return WikiEngine.instance().getWikiPage(getWikiName(request));
    }

    public int getVersionNumber(HttpServletRequest request)
    {
        String versionString = request.getParameter("version");

        try {
            return Integer.parseInt(versionString);
        } catch (Exception e) {
            // Do nothing.
        }
        return -1;

    }

    public Version getVersion(HttpServletRequest request) throws ServletException
    {
        WikiPage wikiPage = getWikiPage(request);
        int versionNumber = getVersionNumber(request);

        Version version;
        if (versionNumber < 0) {
            version = wikiPage.getCurrentVersion();
        } else {
            version = wikiPage.getVersion(versionNumber);
        }

        return version;
    }

    protected void error(HttpServletRequest request, HttpServletResponse response, Exception e)
    {
        try {

            if (e instanceof UserMessageException) {

                request.setAttribute("message", e.getMessage());
                forward(request, response, "error");

            } else {

                request.setAttribute("message", UNEXPECTED_ERROR_MESSAGE);
                forward(request, response, "error");
                _logger.error("Unexpected exception");
                _logger.error(e);
                e.printStackTrace();
            }

        } catch (Exception e2) {
            _logger.error("An exception occurred while trying to handle a previous exception nicely!");
            _logger.error(e2);
            e.printStackTrace();
        }
    }

    /**
     * Find the jsp page that is responsible for handling this request, and then
     * forwards to that page. Mostly, the jsp page is determined by the
     * viewType, which is "view", "edit" etc. In which case, it will get this
     * information from servlet's Init parameter (which are defined in tomcat's
     * web.xml file).
     * 
     * However, these can be overriden, depending on the mime type for the page.
     * For example, if the mime type is an image, then instead of seeing the
     * standard "view", an aternative can be displayed, which includes a html
     * img for the image as well as the wiki text associated with the image.
     * 
     * This was implemented to let me use the wiki storage to hold non-wiki
     * objects - in particular stickflick animations. In this case, I wanted the
     * "edit" page to have nothing to do with wiki markup, and instead use a
     * web-based GUI editor to edit the animation. The animation was saved as
     * xml into the wiki's storage.
     * 
     * viewtype is used in two ways. The first is to look up
     */
    protected void forward(HttpServletRequest request, HttpServletResponse response, String viewType)
                    throws ServletException, IOException
    {

        String page = null;

        try {
            if (viewType != null) {
                WikiName wikiName = getWikiName(request);
                MimeType mimeType = wikiName.getMimeType();

                if (mimeType != null) {
                    page = mimeType.getView(viewType);
                }
            }
        } catch (Exception e) {
            // Do nothing
        }

        if (page == null) {
            if (viewType == null) {
                page = getServletConfig().getInitParameter("view");
            } else {
                page = getServletConfig().getInitParameter("view." + viewType);
            }
        }

        if (page == null) {
            page = getServletConfig().getInitParameter("view");
        }

        response.setCharacterEncoding(getCharacterEncoding());

        RequestDispatcher rd = getServletContext().getRequestDispatcher(page);
        rd.forward(request, response);
    }

    protected String getCharacterEncoding()
    {
        return WikiEngine.instance().getCharacterSet();
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException
    {
        forward(request, response, null);
    }

    protected void redirectReferrer(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException
    {
        String referrer = request.getParameter("referrer");
        if (referrer == null) {
            redirectHome(response);
        } else {
            response.sendRedirect(referrer);
        }
    }

    protected void redirectHome(HttpServletResponse response) throws IOException
    {
        WikiEngine wikiEngine = WikiEngine.instance();
        WikiPage wikiPage = wikiEngine.getDefaultWikiPage();

        String url = wikiEngine.getUrlManager().getUrl(UrlManager.URL_TYPE_VIEW, wikiPage);

        response.sendRedirect(url);
    }

    protected boolean ensureSecurity(HttpServletRequest request, HttpServletResponse response, String message)
    {
        try {

            WikiEngine wikiEngine = WikiEngine.instance();
            WikiPage wikiPage = getWikiPage(request);

            if (wikiEngine.canEdit(wikiPage)) {

                return true;

            } else {

                if (wikiEngine.getAuthenticationManager().isLoggedIn()) {

                    request.setAttribute("message", message);
                    forward(request, response, "notAuthorised");

                } else {

                    String loginUrl = WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_LOGIN);

                    response.sendRedirect(loginUrl);

                }

            }

        } catch (Exception e) {
            error(request, response, e);
        }

        return false;
    }

    protected void redirectToPageOrReferrer(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        WikiPage wikiPage = getWikiPage(request);

        if ((request.getParameter("referrer") == null) || (request.getParameter("referrer").equals(""))) {
            response.sendRedirect(wikiPage.getViewUrl());
        } else {
            response.sendRedirect((String) request.getParameter("referrer"));
        }
    }

}
