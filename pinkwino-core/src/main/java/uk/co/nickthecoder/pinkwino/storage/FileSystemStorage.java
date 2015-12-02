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

package uk.co.nickthecoder.pinkwino.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Stores wiki pages in the filesystem
 */

public class FileSystemStorage implements WikiStorage
{
    protected static Logger _logger = LogManager.getLogger(FileSystemStorage.class);

    public static String SUFFIX = ".wiki";

    private FileLocation _fileLocation;

    public FileSystemStorage(File baseDirectory)
    {
        _fileLocation = new SimpleFileLocation(baseDirectory);
    }

    public FileSystemStorage(FileLocation fileLocation)
    {
        _fileLocation = fileLocation;
    }

    public FileLocation getFileLocation()
    {
        return _fileLocation;
    }

    protected File getFile(WikiName wikiName)
    {
        return _fileLocation.getFile(wikiName);
    }

    protected File getMediaFile(WikiName wikiName)
    {
        return _fileLocation.getMediaFile(wikiName);
    }

    public boolean exists(WikiPage wikiPage)
    {
        WikiName wikiName = wikiPage.getWikiName();
        File file = getFile(wikiName);

        return file.exists();
    }

    public void loadCurrentVersion(WikiPage wikiPage)
    {
        WikiName wikiName = wikiPage.getWikiName();
        File file = getFile(wikiName);

        if (file.exists()) {
            Version version = new Version(wikiPage);
            version.setVersionNumber(-1);
            version.setContent(loadContent(file));
            version.setDate(new Date(file.lastModified()));

            try {
                version.setHasMedia(getMediaFile(wikiName).exists());
            } catch (Exception e) {
                // do nothing - there isn't a media files aren't supported, so
                // leave the boolean as false
            }

            wikiPage.setExists(true);
            wikiPage.setCurrentVersion(version);
        } else {
            wikiPage.setCurrentVersion(new Version(wikiPage));
            wikiPage.getCurrentVersion().setContent("");
        }
    }

    protected String loadContent(File file)
    {
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                            getCharacterSet()));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                buffer.append(line).append("\n");
            }
            reader.close();

            return buffer.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load wiki page from file : " + file);
        }

    }

    /**
     * This WikiStorage does not support versioning, so the loadVersions always
     * loads just the current version (ie the list is size 1).
     */
    public void loadVersions(WikiPage wikiPage)
    {
        ArrayList<Version> list = new ArrayList<Version>();
        list.add(wikiPage.getCurrentVersion());

        wikiPage.setVersions(list);
    }

    /**
     * This WikiStorage does not support versioning, so this always throws an
     * exception.
     */
    public void loadVersion(Version version)
    {
        throw new RuntimeException("FileSystemStorage.loadVersion not supported");
    }

    public void save(WikiPage wikiPage, String markup, File mediaSource)
    {

        Version newVersion = new Version(wikiPage);
        newVersion.setContent(markup);
        wikiPage.setCurrentVersion(newVersion);

        try {

            File file = getFile(wikiPage.getWikiName());
            file.getParentFile().mkdirs();
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), getCharacterSet());
            writer.write(markup);
            writer.close();
            // newVersion.setContent( markup );

            if (mediaSource != null) {
                File mediaDestination = getMediaFile(wikiPage.getWikiName());
                mediaDestination.getParentFile().mkdirs();
                uk.co.nickthecoder.pinkwino.util.FileUtil.rename(mediaSource, mediaDestination);
            }

        } catch (Exception e) {
            _logger.error("save failed : " + e);
            throw new RuntimeException("Failed to save the wiki page to disk.");
        }
    }

    protected String getCharacterSet()
    {
        return "UTF-8";
    }

    public List<WikiPage> index(Namespace namespace)
    {
        return _fileLocation.index(namespace);
    }

    public List<WikiPage> relatedPages(WikiPage wikiPage)
    {
        if (wikiPage.getWikiName().getRelation() != null) {
            throw new RuntimeException("wikiPage must have a relation of null");
        }

        return _fileLocation.relatedPages(wikiPage);
    }

    public List<WikiPage> recentChanges(Namespace namespace, Date since)
    {
        List<WikiPage> all = index(namespace);
        List<WikiPage> results = new ArrayList<WikiPage>(100);

        for (Iterator<WikiPage> i = all.iterator(); i.hasNext();) {
            WikiPage wikiPage = i.next();

            if (since.before(wikiPage.getLastModified())) {
                results.add(wikiPage);
            }
        }

        return results;
    }
    public void delete(WikiPage wikiPage)
    {
        WikiName wikiName = wikiPage.getWikiName();

        File file = getFile(wikiName);
        _logger.info("Deleting wiki file : " + file);
        file.delete();

        File media = getMediaFile(wikiName);
        _logger.info("Deleting media file : " + media);
        media.delete();
    }

    public InputStream getMedia(Version version) throws IOException
    {
        return new FileInputStream(getMediaFile(version.getWikiPage().getWikiName()));
    }

    // TODO This only ever gets the most recent version of a media file.
    public File getMediaFile(Version version)
    {
        return getMediaFile(version.getWikiPage().getWikiName());
    }
    public boolean isReadOnly()
    {
        return false;
    }

    public String toString()
    {
        return "FileSystem : " + _fileLocation;
    }    
}

