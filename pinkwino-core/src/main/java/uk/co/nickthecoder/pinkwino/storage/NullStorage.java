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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * A fake storage used for all unknown namespaces. This storage cannot load or
 * save anything.
 */

public class NullStorage implements WikiStorage
{
    private static List<WikiPage> emptyWikiPages = new ArrayList<WikiPage>();

    public NullStorage()
    {
    }

    public boolean exists(WikiPage wikiPage)
    {
        return false;
    }

    public void loadCurrentVersion(WikiPage wikiPage)
    {
        Version version = new Version(wikiPage);
        wikiPage.setCurrentVersion(version);
    }

    public void save(WikiPage wikiPage, String markup, File mediaFile)
    {
        // do nothing
    }

    public void loadVersions(WikiPage wikiPage)
    {
        List<Version> list = new ArrayList<Version>();
        list.add(wikiPage.getCurrentVersion());

        wikiPage.setVersions(list);
    }

    public void loadVersion(Version version)
    {
        version.setContent("");
    }

    public List<WikiPage> index(Namespace namespace)
    {
        return emptyWikiPages;
    }

    // {{{ relatedPages
    public List<WikiPage> relatedPages(WikiPage wikPage)
    {
        return emptyWikiPages;
    }

    // }}}

    public List<WikiPage> recentChanges(Namespace namespace, Date since)
    {
        return emptyWikiPages;
    }

    public InputStream getMedia(Version version) throws IOException
    {
        return null;
    }

    public File getMediaFile(Version version) throws IOException
    {
        return null;
    }

    public void delete(WikiPage wikiPage)
    {
        // do nothing
    }

    public boolean isReadOnly()
    {
        return true;
    }

    public String toString()
    {
        return "NullStorage";
    }

}

