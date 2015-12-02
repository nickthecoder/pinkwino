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
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.webwidgets.util.MultipartHelper;
import uk.co.nickthecoder.webwidgets.util.MultipartServletRequestWrapper;

/**
 * GET requests are forwarded as usual, POST requests are handled here. NOTE on
 * post, we will be using multipart, because if the page contains an attachment,
 * then this servlet needs to handle the upload of the file.
 */

public class EditWikiServlet extends ForwardingWikiServlet
{

    private static final long serialVersionUID = 1L;

    protected static Logger _logger = LogManager.getLogger(EditWikiServlet.class);

    public static int maximumContentLength = 4 * 1024 * 1024;

    public EditWikiServlet()
    {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (ensureSecurity(request, response, "You are not authorised to edit this page")) {

            Version version = getVersion(request);
            String markup = version.getContent();

            try {
                String section = request.getParameter("section");
                markup = version.getSection(section);
            } catch (Exception e) {
                // Do nothing
            }

            request.setAttribute("markup", markup);
            forward(request, response, "edit");

        }
    }

    protected void doPost(HttpServletRequest unwrappedRequest, HttpServletResponse response) throws ServletException,
                    IOException
    {

        MultipartHelper multipart = new MultipartHelper();
        multipart.setMaximumContentLength(EditWikiServlet.maximumContentLength);
        multipart.setCharacterEncoding(WikiEngine.instance().getCharacterSet());

        multipart.setOutputDirectory(WikiEngine.instance().getMediaManager().getTempDirectory());
        HttpServletRequest request = new MultipartServletRequestWrapper(unwrappedRequest, multipart);

        try {

            multipart.go(request);

            if (request.getParameter("preview") != null) {

                Version version = new Version(getWikiPage(request));
                version.setContent(request.getParameter("markup"));
                request.setAttribute("version", version);

                forward(request, response, "preview");

            } else if (request.getParameter("save") != null) {

                String sectionId = request.getParameter("section");
                String markup = request.getParameter("markup");
                WikiPage wikiPage = getWikiPage(request);

                if ((sectionId == null) || (sectionId.length() == 0)) {
                    File file = null;

                    if (multipart.getFile("media") != null) {
                        file = multipart.getFile("media").getFile();
                    }
                    WikiEngine.instance().save(wikiPage, markup, file);
                } else {
                    WikiEngine.instance().saveSection(wikiPage, sectionId, markup);
                }

                redirectToPageOrReferrer(request, response);

            } else if (request.getParameter("cancel") != null) {

                redirectToPageOrReferrer(request, response);

            } else {

                _logger.error("Unknown action. Throwing");
                throw new RuntimeException("EditWikiServlet : Unknown action");

            }

        } catch (Exception e) {

            error(request, response, e);

        } finally {

            // Delete all of the temp files created during the upload process.
            for (Iterator<MultipartHelper.FileInfo> i = multipart.getFiles().values().iterator(); i.hasNext();) {
                MultipartHelper.FileInfo fileInfo = (MultipartHelper.FileInfo) i.next();

                if (fileInfo.getFile() != null) {
                    fileInfo.getFile().delete();
                }
            }

        }

    }

}
