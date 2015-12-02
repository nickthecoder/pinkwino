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
 * Controls the persistance and retrieval of wiki pages.
 */
public interface WikiStorage
{

    public boolean exists(WikiPage wikiPage);

    public void loadCurrentVersion(WikiPage wikiPage);

    public void save(WikiPage wikiPage, String markup, File mediaFile);

    public void loadVersions(WikiPage wikiPage);

    public void loadVersion(Version version);

    /**
     * Return a list of WikiPage objects, all pages within the namespace are to
     * be included. Note, this is the list of wiki pages, if a namespace is an
     * extenstion of an external resource (such as a person database), then at
     * the moment there is no advice as to whether to include *all* entities, or
     * only those that do have a wiki page.
     */
    public List<WikiPage> index(Namespace namespace);

    /**
     * Returns a list of all related WikiPage objects for the given wiki page.
     * WikiPage must have a relation of null.
     */
    public List<WikiPage> relatedPages(WikiPage wikPage);

    /**
     * Returns the list of WikiPage objects, that have been changed recently.
     */
    public List<WikiPage> recentChanges(Namespace namespace, Date since);

    public InputStream getMedia(Version version) throws IOException;

    public File getMediaFile(Version version) throws IOException;

    public void delete(WikiPage wikiPage);

    public boolean isReadOnly();

}
