/* {{{

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

package uk.co.nickthecoder.pinkwino.media;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiName;

/**
 * Holds information about mime types, and file extensions
 */

public interface MediaManager
{
    public MimeType getMimeType(String mimeTypeString);

    public MimeType getMimeTypeByExtension(String extension);

    /**
     * Considers all dots within the name to resolve the mime type. For example
     * "hello.tar.gz" will be evaluated to the tar.gz mime type, whereas
     * 2007.01.01.png will be evaluated to the png mime type.
     */
    public MimeType getMimeTypeByName(String name);

    public MimeType getMimeType(WikiName name);

    public Collection<MimeType> getMimeTypes();

    public File getTempDirectory();

    /**
     * Called just before the media is streamed to the client. This allows for
     * arbitrary changes to be made to the stream and/or the page before it is
     * returned to the client.
     * 
     * In particular, this was designed to allow for image processing to take
     * place automatically. For example, if a related page "shadow" is
     * requested, then take the original image, apply a drop shadow, and return
     * that.
     * 
     * If an input stream is returned, then the MediaServlet will use that, if
     * null is returned, then the normal processing will take place (i.e. the
     * media is taken from the page's Storage.
     */
    public InputStream mediaPreprocess(Version version);

    /**
     * If the name has a known extension, then remove it, returning just the
     * part before the period. Otherwise return name unchanged.
     */
    public String stripKnownExtentions(String name);

    public MediaPreprocessor getMediaPreprocessor(String relation);

}
