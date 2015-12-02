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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

public class SimpleFileLocation implements FileLocation
{

    public static String SUFFIX = ".wiki";

    public static final String HISTORY_DIRECTORY = ".history";

    public static final String MEDIA_SUFFIX = ".media";

    public static final String MEDIA_DIRECTORY = "media";

    public static final String SLASH_REPLACEMENT = "__slash__";

    private File _baseDirectory;

    private File _mediaDirectory;

    private File _historyDirectory;

    public SimpleFileLocation(File baseDirectory)
    {
        _baseDirectory = baseDirectory;
        _mediaDirectory = new File(baseDirectory, MEDIA_DIRECTORY);
        _historyDirectory = new File(baseDirectory, HISTORY_DIRECTORY);
    }

    public File getFile(WikiName wikiName)
    {
        if (wikiName.getRelation() == null) {
            return new File(_baseDirectory, encodeName(wikiName.getTitle()) + SUFFIX);
        } else {
            return new File(_baseDirectory, encodeName(wikiName.getTitle()) + "." + encodeName(wikiName.getRelation()));
        }
    }

    public File getFile(WikiName wikiName, int versionNumber)
    {
        File dir = getHistoryDirectory(wikiName);

        if (wikiName.getRelation() == null) {
            return new File(dir, "" + versionNumber + SUFFIX);
        } else {
            return new File(dir, "" + versionNumber + encodeName(wikiName.getRelation()));
        }

    }

    public File getMediaFile(WikiName wikiName)
    {
        if (wikiName.getRelation() == null) {
            return new File(_mediaDirectory, encodeName(wikiName.getTitle()));
        } else {
            return new File(new File(_mediaDirectory, encodeName(wikiName.getRelation())),
                            encodeName(wikiName.getTitle()));
        }
    }

    public File getMediaFile(WikiName wikiName, int versionNumber)
    {
        File dir = getHistoryDirectory(wikiName);

        if (wikiName.getRelation() == null) {
            return new File(dir, "" + versionNumber + MEDIA_SUFFIX);
        } else {
            return new File(dir, encodeName(wikiName.getRelation()) + ":" + versionNumber + MEDIA_SUFFIX);
        }
    }

    /**
     * Replaces special characters with something else to ensure that the name
     * is a valid as part of a filename. Note, that this may cause two wiki
     * names to reference the same page.
     */
    public String encodeName(String name)
    {
        return name.replaceAll(File.separator, SLASH_REPLACEMENT);
    }

    /**
     * Given a partial filename, get the wiki name that it refers to.
     */
    public String decodeName(String name)
    {
        return name.replaceAll(SLASH_REPLACEMENT, File.separator);
    }

    public File getHistoryDirectory(WikiName wikiName)
    {
        return new File(_historyDirectory, encodeName(wikiName.getTitle()));
    }

    public List<WikiPage> relatedPages(WikiPage wikiPage)
    {

        if (wikiPage.getWikiName().getRelation() != null) {
            throw new RuntimeException("wikiPage must be the main page, not a related page.");
        }

        File dir = _baseDirectory;

        String[] filenames = dir.list();
        if (filenames == null) {
            return new ArrayList<WikiPage>(0);
        }

        String prefix = encodeName(wikiPage.getWikiName().getTitle()) + ".";
        int prefixLength = prefix.length();

        WikiEngine wikiEngine = WikiEngine.instance();

        ArrayList<WikiPage> results = new ArrayList<WikiPage>();

        for (int i = 0; i < filenames.length; i++) {
            if (filenames[i].startsWith(prefix)) {

                String relation = filenames[i].substring(prefixLength);
                if (!"wiki".equals(relation)) {
                    relation = decodeName(relation);
                    WikiPage relatedPage = wikiEngine.getWikiPage(wikiPage.getWikiName().getRelatedName(relation));
                    results.add(relatedPage);
                }
            }
        }

        return results;
    }

    public List<WikiPage> index(Namespace namespace)
    {
        File dir = _baseDirectory;

        File[] files = dir.listFiles();
        if (files == null) {
            return new ArrayList<WikiPage>(0);
        }

        ArrayList<WikiPage> results = new ArrayList<WikiPage>(files.length);

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String filename = file.getName();

            if (filename.endsWith(SUFFIX)) {
                String title = filename.substring(0, filename.length() - SUFFIX.length());
                title = decodeName(title);

                WikiName wikiName = new WikiName(namespace, title);
                WikiPage wikiPage = new WikiPage(wikiName);
                wikiPage.setExists(true);
                wikiPage.setLastModified(new Date(file.lastModified()));
                results.add(wikiPage);
            }
        }

        return results;
    }

    public String toString()
    {
        return _baseDirectory.toString();
    }
}
