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
import java.util.Date;
import java.util.List;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * A convienence class for wrapping the WikiStorage interface. All methods are
 * simply offloaded the the inner wiki storage.
 */
public abstract class WrappedStorage implements WikiStorage
{

    private WikiStorage _storage;

    public WrappedStorage(WikiStorage storage)
    {
        _storage = storage;
    }

    public boolean exists(WikiPage wikiPage)
    {
        return _storage.exists(wikiPage);
    }

    public void loadCurrentVersion(WikiPage wikiPage)
    {
        _storage.loadCurrentVersion(wikiPage);
    }

    public void loadVersions(WikiPage wikiPage)
    {
        _storage.loadVersions(wikiPage);
    }

    public void loadVersion(Version version)
    {
        _storage.loadVersion(version);
    }

    public List<WikiPage> index(Namespace namespace)
    {
        return _storage.index(namespace);
    }

    public List<WikiPage> relatedPages(WikiPage wikiPage)
    {
        return _storage.relatedPages(wikiPage);
    }

    public List<WikiPage> recentChanges(Namespace namespace, Date since)
    {
        return _storage.recentChanges(namespace, since);
    }

    public void save(WikiPage wikiPage, String markup, File mediaFile)
    {
        _storage.save(wikiPage, markup, mediaFile);
    }

    public void delete(WikiPage wikiPage)
    {
        _storage.delete(wikiPage);
    }

    public boolean isReadOnly()
    {
        return _storage.isReadOnly();
    }

    public InputStream getMedia(Version version) throws IOException
    {
        return _storage.getMedia(version);
    }

    public File getMediaFile(Version version) throws IOException
    {
        return _storage.getMediaFile(version);
    }

}

