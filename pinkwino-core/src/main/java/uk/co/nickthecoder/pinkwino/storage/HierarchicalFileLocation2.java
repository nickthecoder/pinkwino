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
 * This is very similar to HierarchicalFileLocation, the difference is that
 * HierarchicalFileLoation is designed such that each wiki name relates to a
 * given FILE, and the wiki comments were placed in a hidden directory. With
 * HierarchicalFileLocation2, each wiki name relates to a DIRECTORY, and the
 * wiki comments are placed directly in that directory (so they are easy to see
 * when exploring the directory)
 */

public class HierarchicalFileLocation2 implements FileLocation
{

    public static final String DEFAULT_HISTORY_DIRECTORY = ".wiki/wikiHistory";

    public static final String DEFAULT_WIKI_FILE_NAME = "comments.wiki";

    public static String SUFFIX = ".wiki";

    /**
     * The name of the wiki page
     */
    private String _wikiFileName;

    /**
     * Where to place the history data
     */
    private String _historyDirectoryName;

    private File _baseDirectory;

    public HierarchicalFileLocation2(File baseDirectory)
    {
        this(baseDirectory, DEFAULT_WIKI_FILE_NAME, DEFAULT_HISTORY_DIRECTORY);
    }

    public HierarchicalFileLocation2(File baseDirectory, String wikiFileName, String historyDirectoryName)
    {
        _baseDirectory = baseDirectory;
        _wikiFileName = wikiFileName;
        _historyDirectoryName = historyDirectoryName;
    }

    public File getFile(WikiName wikiName)
    {

        File result = new File(new File(_baseDirectory, wikiName.getTitle()), _wikiFileName);

        return result;
    }

    public File getFile(WikiName wikiName, int versionNumber)
    {
        File dir = getHistoryDirectory(wikiName);
        File result = new File(dir, "" + versionNumber + SUFFIX);

        return result;
    }

    public File getMediaFile(WikiName wikiName)
    {
        throw new RuntimeException("HierarchicalFileLocation2 cannot store media files");
    }

    public File getMediaFile(WikiName wikiName, int versionNumber)
    {
        throw new RuntimeException("HierarchicalFileLocation2 cannot store media files");
    }

    public File getHistoryDirectory(WikiName wikiName)
    {
        File result = new File(new File(_baseDirectory, wikiName.getTitle()), _historyDirectoryName);
        return result;
    }

    /**
     * Related pages are not allowed, so this always returns an empty list.
     */
    public List<WikiPage> relatedPages(WikiPage wikiPage)
    {
        return new ArrayList<WikiPage>(0);
    }

    @Override
    public List<WikiPage> index(Namespace namespace)
    {
        throw new RuntimeException("Cannot index this namespace");
    }
    
}

