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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Adds versioning to the FileSystemStorage
 */
public class VersioningFileSystemStorage extends FileSystemStorage
{

    protected static Logger _logger = LogManager.getLogger(VersioningFileSystemStorage.class);

    public VersioningFileSystemStorage(File baseDirectory)
    {
        super(baseDirectory);
    }

    public VersioningFileSystemStorage(FileLocation fileLocation)
    {
        super(fileLocation);
    }

    protected File getFile(WikiName wikiName, int versionNumber)
    {
        return getFileLocation().getFile(wikiName, versionNumber);
    }

    protected File getMediaFile(WikiName wikiName, int versionNumber)
    {
        return getFileLocation().getMediaFile(wikiName, versionNumber);
    }

    public void loadVersions(WikiPage wikiPage)
    {
        File dir = getFileLocation().getHistoryDirectory(wikiPage.getWikiName());

        File[] files = dir.listFiles();
        Version[] versions = new Version[(files == null) ? 0 : files.length];

        int largestVersionNumber = 0;
        int versionIndex = 0;

        for (int i = 0; i < versions.length; i++) {
            String name = files[i].getName();

            if (name.endsWith(SUFFIX)) {

                String versionString = name.substring(0, name.length() - SUFFIX.length());
                try {

                    int versionNumber = Integer.parseInt(versionString);
                    Version version = new Version(wikiPage);
                    version.setVersionNumber(versionNumber);
                    version.setDate(getDate(files[i]));
                    try {
                        version.setHasMedia(getMediaFile(wikiPage.getWikiName(), versionNumber).exists());
                    } catch (Exception e) {
                    }

                    if (versionNumber > largestVersionNumber) {
                        largestVersionNumber = versionNumber;
                    }

                    versions[versionIndex++] = version;

                } catch (Exception e) {
                    // Do nothing
                    _logger.warn("VersioningFileSystemStorage: Error reading version : " + name);
                    _logger.warn(e);
                }
            }
        }

        Arrays.sort(versions, 0, versionIndex);
        // Create the final list, added from the array to the list.
        List<Version> versionList = new ArrayList<Version>(versionIndex);
        for (int a = 0; a < versionIndex; a++) {
            versionList.add(versions[a]);
        }
        wikiPage.setVersions(versionList);

        // Ensure that the current version is fully loaded, because this class
        // doesn't
        // handle lazy reading of the current page if its done through the list
        // of versions (I'm a lazy programmer!)
        wikiPage.getCurrentVersion().getContent();

        wikiPage.getCurrentVersion().setVersionNumber(largestVersionNumber + 1);
        versionList.add(wikiPage.getCurrentVersion());

    }

    protected Date getDate(File file)
    {
        long lastMod = file.lastModified();
        return new Date(lastMod);
    }

    public void loadVersion(Version version)
    {
        File file;
        if (version.getVersionNumber() == -1) {
            file = getFile(version.getWikiPage().getWikiName());
        } else {
            file = getFile(version.getWikiPage().getWikiName(), version.getVersionNumber());
        }

        if (file.exists()) {
            version.setContent(loadContent(file));
            version.setDate(new Date(file.lastModified()));
        } else {
            version.setContent("");
        }
    }

    public void save(WikiPage wikiPage, String markup, File mediaSource)
    {
        List<Version> versions = wikiPage.getVersions();

        int currentVersionNumber = 0;
        if (versions.size() > 0) {

            Version currentVersion = (Version) versions.get(versions.size() - 1);
            currentVersionNumber = currentVersion.getVersionNumber();

            File currentFile = getFile(wikiPage.getWikiName());
            File moveTo = getFile(wikiPage.getWikiName(), currentVersion.getVersionNumber());
            moveTo.getParentFile().mkdirs();

            currentFile.renameTo(moveTo);

            if (mediaSource != null) {
                File currentMediaFile = getMediaFile(wikiPage.getWikiName());
                File moveMediaTo = getMediaFile(wikiPage.getWikiName(), currentVersion.getVersionNumber());
                moveMediaTo.getParentFile().mkdirs();

                currentMediaFile.renameTo(moveMediaTo);
            }
        }

        super.save(wikiPage, markup, mediaSource);

        wikiPage.getCurrentVersion().setVersionNumber(currentVersionNumber + 1);
    }

    public void delete(WikiPage wikiPage)
    {
        try {
            File dir = getFileLocation().getHistoryDirectory(wikiPage.getWikiName());

            if (dir.exists()) {
                deleteDirectory(dir);
            }

            super.delete(wikiPage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDirectory(File directory) throws IOException
    {
        File canonical = directory.getCanonicalFile();
        deleteCanonicalDirectory(canonical);
    }

    private void deleteCanonicalDirectory(File directory) throws IOException
    {
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory() && file.getCanonicalPath().equals(file)) {
                deleteCanonicalDirectory(file);

            } else {
                if (!file.delete()) {
                    throw new IOException("Failed to delete " + file);
                }
            }

        }

        if (!directory.delete()) {
            throw new IOException("Failed to delete " + directory);
        }
    }

    public InputStream getMedia(Version version) throws IOException
    {
        if (version == version.getWikiPage().getCurrentVersion()) {
            return super.getMedia(version);
        } else {
            return new FileInputStream(getMediaFile(version.getWikiPage().getWikiName(), version.getVersionNumber()));
        }
    }

    public String toString()
    {
        return "Versioning" + super.toString();
    }
}

