/* {{{ GPL

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

package uk.co.nickthecoder.pinkwino.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Organises the wiki text so that it lives in the same directory as the object
 * it refers to. This was created so that I could create wiki text for each
 * album within my music collection. I want the wiki text to live within the
 * same directory as the music itslef (so that the wiki text is backed up with
 * the music, and if I rename a directory, the wiki text automatically follows
 * the music)
 */

public class HierarchicalFileLocation implements FileLocation
{

    public static String DEFAULT_DIRECTORY_NAME = ".wiki";

    public static String SUFFIX = ".wiki";

    public static final String DEFAULT_HISTORY_DIRECTORY = ".wiki/wikiHistory";

    private File _baseDirectory;

    private String _wikiDirectoryName;

    private String _historyDirectoryName;

    public HierarchicalFileLocation(File baseDirectory)
    {
        this(baseDirectory, DEFAULT_DIRECTORY_NAME, DEFAULT_HISTORY_DIRECTORY);
    }

    public HierarchicalFileLocation(File baseDirectory, String wikiDirectoryName, String historyDirectoryName)
    {
        _baseDirectory = baseDirectory;
        _wikiDirectoryName = wikiDirectoryName;
        _historyDirectoryName = historyDirectoryName;
    }

    public File getFile(WikiName wikiName)
    {
        File fake = new File(wikiName.getTitle() + SUFFIX);
        String name = fake.getName();
        String parent = fake.getParent();

        File result = new File(new File(new File(_baseDirectory, parent), _wikiDirectoryName), name);

        return result;
    }

    public File getFile(WikiName wikiName, int versionNumber)
    {
        File dir = getHistoryDirectory(wikiName);

        return new File(dir, "" + versionNumber + SUFFIX);
    }

    public File getMediaFile(WikiName wikiName)
    {
        throw new RuntimeException("HierarchicalFileLocations cannot store media files");
    }

    public File getMediaFile(WikiName wikiName, int versionNumber)
    {
        throw new RuntimeException("HierarchicalFileLocations cannot store media files");
    }

    public File getHistoryDirectory(WikiName wikiName)
    {
        File fake = new File(wikiName.getTitle());
        String name = fake.getName();
        String parent = fake.getParent();

        File result = new File(new File(new File(_baseDirectory, parent), _historyDirectoryName), name);
        return result;
    }

    /**
     * Related pages are not allowed, so this always returns an empty list.
     */
    public List<WikiPage> relatedPages(WikiPage wikiPage)
    {
        return new ArrayList<WikiPage>(0);
    }

    public List<WikiPage> index(Namespace namespace)
    {
        throw new RuntimeException("Cannot index this namespace");
    }

}
