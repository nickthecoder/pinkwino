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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Holds information about mime types, and file extensions
 */

public class StandardMediaManager implements MediaManager
{
    public static File DEFAULT_TEMP_DIRECTORY = new File("/tmp/");

    protected static Logger _logger = LogManager.getLogger(StandardMediaManager.class);

    /**
     * Key = filename extension (without the ".") - String
     */
    private HashMap<String, MimeType> _extensions;

    /**
     * Key = mime-type - String
     */
    private HashMap<String, MimeType> _mimeTypes;

    /**
     * The directory to store the files while they are being uploaded.
     */
    private File _tempDirectory;

    /**
     * A Map of MediaProcessor objects keyed by a String, which is the related
     * page name which activates the MediaPreprocessor.
     */
    private Map<String, MediaPreprocessor> _mediaPreprocessors;

    public StandardMediaManager()
    {
        _mimeTypes = new HashMap<String, MimeType>();
        _extensions = new HashMap<String, MimeType>();
        _tempDirectory = DEFAULT_TEMP_DIRECTORY;
        _mediaPreprocessors = new HashMap<String, MediaPreprocessor>();
    }

    public void addMimeType(MimeType mimeType)
    {
        _mimeTypes.put(mimeType.getMimeType(), mimeType);
        for (Iterator<String> i = mimeType.getExtensions().iterator(); i.hasNext();) {
            String extension = i.next();
            _extensions.put(extension, mimeType);
        }
    }

    public MimeType getMimeType(String mimeTypeString)
    {
        return (MimeType) _mimeTypes.get(mimeTypeString);
    }

    /**
     * Consider both the relation and the page title when deciding the MimeType.
     * A related page may contain different a different type of media to the
     * parent page, for example, a thumbnail or an svg could be a png file, not
     * an svg file.
     */
    public MimeType getMimeType(WikiName name)
    {

        if (name.getRelation() != null) {
            MediaPreprocessor mp = getMediaPreprocessor(name.getRelation());
            if (mp != null) {
                MimeType result = mp.getMimeType();
                if (result != null) {
                    return result;
                }
            }
        }

        return getMimeTypeByName(name.getTitle());
    }

    public MimeType getMimeTypeByExtension(String extension)
    {
        return (MimeType) _extensions.get(extension);
    }

    /**
     * Considers all dots within the name to resolve the mime type. For example
     * "hello.tar.gz" will be evaluated to the tar.gz mime type, whereas
     * 2007.01.01.png will be evaluated to the png mime type.
     */
    public MimeType getMimeTypeByName(String name)
    {
        for (int i = name.indexOf(".", 0); i >= 0; i = name.indexOf(".", i + 1)) {
            String extension = name.substring(i + 1);

            if (getMimeTypeByExtension(extension) != null) {
                return getMimeTypeByExtension(extension);
            }
        }

        // Not found
        return null;
    }

    /**
     * If the name has a known extention, then remove it, returning just the
     * part before the period. Otherwise return name unchanged.
     */
    public String stripKnownExtentions(String name)
    {
        for (int i = name.indexOf(".", 0); i >= 0; i = name.indexOf(".", i + 1)) {
            String extension = name.substring(i + 1);

            if (getMimeTypeByExtension(extension) != null) {
                return name.substring(0, i);
            }
        }

        // Not found
        return name;
    }

    public void setTempDirectory(File value)
    {
        _tempDirectory = value;
    }

    public File getTempDirectory()
    {
        return _tempDirectory;
    }

    public void addMediaPreprocessor(String name, MediaPreprocessor mediaPreprocessor)
    {
        _mediaPreprocessors.put(name, mediaPreprocessor);
    }

    public MediaPreprocessor getMediaPreprocessor(String relation)
    {
        return _mediaPreprocessors.get(relation);
    }

    public InputStream mediaPreprocess(Version version)
    {
        WikiName wikiName = version.getWikiPage().getWikiName();
        MediaPreprocessor mp = wikiName.getMediaPreprocessor();
        if (mp == null) {
            return null;
        } else {
            Parameters parameters = wikiName.getRelationParameters();
            return mp.process(version, parameters);
        }
    }

    public Collection<MimeType> getMimeTypes()
    {
        return _mimeTypes.values();
    }

}
