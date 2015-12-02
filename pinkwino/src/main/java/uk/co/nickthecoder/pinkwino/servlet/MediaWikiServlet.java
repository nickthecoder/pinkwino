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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.media.MimeType;

public class MediaWikiServlet extends WikiServlet
{

    private static final long serialVersionUID = 1L;

    protected static Logger _logger = LogManager.getLogger(MediaWikiServlet.class);

    public MediaWikiServlet()
    {
        super();
    }

    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException
    {
        super.doHead(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Version version = getVersion(request);

        WikiPage wikiPage = version.getWikiPage();
        WikiName wikiName = wikiPage.getWikiName();
        MimeType mimeType = wikiName.getMimeType();

        if (mimeType == null) {
            response.setContentType("application/octet-stream");
        } else {
            response.setContentType(mimeType.getMimeType());
        }

        streamMedia(response, version);

    }

    protected void streamMedia(HttpServletResponse response, Version version) throws IOException
    {
        InputStream from = null;
        OutputStream to = null;

        Date lastModified = version.getDate();
        String filename = version.getWikiPage().getWikiName().getTitle();

        try {

            response.setDateHeader("Last-Modified", lastModified.getTime());
            response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");

            from = WikiEngine.instance().getMediaManager().mediaPreprocess(version);
            if (from == null) {

                if (version.getWikiPage().getExists() && (version.getHasMedia())) {
                    from = version.getWikiPage().getWikiStorage().getMedia(version);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }

            to = response.getOutputStream();

            byte[] buffer = new byte[40960];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytesRead); // write
            }

        } finally {

            if (from != null) {
                from.close();
            }
            if (to != null) {
                to.flush();
                // to.close();
            }
        }

    }

}
