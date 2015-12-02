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

package uk.co.nickthecoder.pinkwino;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.link.UrlManager;
import uk.co.nickthecoder.pinkwino.metadata.NullSearchResults;
import uk.co.nickthecoder.pinkwino.metadata.SearchResults;
import uk.co.nickthecoder.pinkwino.storage.WikiStorage;

/**
 * Holds all information about a single wiki page, including its current and all
 * previous versions. Note. Much of the details within this class may be lazily
 * loaded.
 */

public class WikiPage implements Comparable<WikiPage>
{

    protected static Logger _logger = LogManager.getLogger(WikiPage.class);

    /**
     * The date of the most recent version. Evaluated lazily, if this is not
     * set, then when it is requested, then I will ask the storage for the
     * current version, and get the date from there.
     */
    private Date _lastModified;

    /**
     * The name of this wiki page
     */
    private WikiName _wikiName;

    /**
     * The latest version for this wiki page. Note, this is lazily loaded, so
     * will be null until getCurrentContent is called.
     */
    private Version _currentVersion;

    /**
     * The object responsible for persisting, and reading data for this
     * WikiPage. The data for the wiki page is loaded lazily, so it is useful
     * for the wiki page to know who to ask to read the data on demand.
     */
    private WikiStorage _storage;

    /**
     * A List of Version objects
     */
    private List<Version> _versions;

    /**
     * Does the wiki page exist yet?
     */
    private Boolean _exists;

    /**
     * A collection of WikiPage objects keyed by their relation. Only used by
     * the main wiki page, not its related pages.
     */
    private HashMap<String, WikiPage> _relatedPages;

    public static WikiPage find(String wikiPageName)
    {
        return WikiEngine.instance().getWikiPage(wikiPageName);
    }

    public WikiPage(WikiName wikiName)
    {
        _storage = wikiName.getNamespace().getStorage();
        _wikiName = wikiName;
        _versions = null;
        _lastModified = null;
    }

    /**
     * Get method for attribute {@link #_lastModified}. The date of the most
     * recent version
     */
    public Date getLastModified()
    {
        if (_lastModified == null) {
            _lastModified = getCurrentVersion().getDate();
        }
        return _lastModified;
    }

    /**
     * Set method for attribute {@link #_lastModified}. The date of the most
     * recent version
     */
    public void setLastModified(Date value)
    {
        _lastModified = value;
    }

    /**
     * Get method for attribute {@link #_wikiName}. The name of this wiki page
     */
    public WikiName getWikiName()
    {
        return _wikiName;
    }

    public WikiStorage getWikiStorage()
    {
        return _storage;
    }

    /**
     * Set method for attribute {@link #_wikiName}. The name of this wiki page
     */
    public void setWikiName(WikiName value)
    {
        _wikiName = value;
    }

    public Namespace getNamespace()
    {
        return getWikiName().getNamespace();
    }

    private void ensureCurrentVersion()
    {
        if (_currentVersion == null) {
            _storage.loadCurrentVersion(this);
        }
    }

    public Version getCurrentVersion()
    {
        ensureCurrentVersion();
        return _currentVersion;
    }

    public void setCurrentVersion(Version value)
    {
        _currentVersion = value;
    }

    private void ensureVersions()
    {
        if (_versions == null) {
            _versions = new ArrayList<Version>();
            _storage.loadVersions(this);
        }
    }

    public List<Version> getVersions()
    {
        ensureVersions();
        return _versions;
    }

    public boolean getHasVersions()
    {
        ensureVersions();
        return _versions.size() > 1;
    }

    public Version getVersion(int versionNumber)
    {
        for (Iterator<Version> i = getVersions().iterator(); i.hasNext();) {
            Version version = i.next();
            if (version.getVersionNumber() == versionNumber) {
                return version;
            }
        }
        return null;
    }

    /**
     * Called by the WikiStorage, when after the set of versions has been read
     * from the database.
     */
    public void setVersions(List<Version> versions)
    {
        _versions = versions;
    }

    public boolean getExists()
    {
        if (_exists == null) {
            _exists = new Boolean(_storage.exists(this));
        }

        return _exists.booleanValue();
    }

    public void setExists(boolean value)
    {
        _exists = value ? Boolean.TRUE : Boolean.FALSE;
    }

    public String getViewUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_VIEW, this);
    }

    public String getRawUrl()
    {
        return getCurrentVersion().getRawUrl();
    }

    public String getEditUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_EDIT, this);
    }

    public String getDiffUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_DIFF, this);
    }

    public String getInfoUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_INFO, this);
    }

    public String getMediaUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_MEDIA, this);
    }

    public String getDeleteUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_DELETE, this);
    }

    public String getRenameUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_RENAME, this);
    }

    public String getSecurityUrl()
    {
        return getRelatedPage(WikiConstants.RELATION_SECURITY).getViewUrl();
    }

    public WikiEngine getWikiEngine()
    {
        return WikiEngine.instance();
    }

    public String render()
    {
        return WikiEngine.instance().getRenderer().render(getCurrentVersion());
    }

    public WikiPage getRelatedPage(String relation)
    {
        if (getWikiName().getRelation() == null) {

            if (relation == null) {
                return this;
            }

            ensureRelatedPages();

            WikiPage wikiPage = (WikiPage) _relatedPages.get(relation);

            if (wikiPage == null) {
                // Not found, create a non-existant page.
                return WikiEngine.instance().getWikiPage(getWikiName().getRelatedName(relation));
            }

            return wikiPage;

        } else {
            return getMainPage().getRelatedPage(relation);
        }

    }

    /**
     * Ensures that _relatedPages is not null. This allows for lazy evaluation
     * of the list of related pages.
     */
    private void ensureRelatedPages()
    {
        if (_relatedPages == null) {
            // Get the relations from storage (lazy evaluation)
            List<WikiPage> relatedPages = _storage.relatedPages(this);
            _relatedPages = new HashMap<String, WikiPage>();
            for (Iterator<WikiPage> i = relatedPages.iterator(); i.hasNext();) {
                WikiPage wikiPage = i.next();
                _relatedPages.put(wikiPage.getWikiName().getRelation(), wikiPage);
            }
        }
    }

    public Collection<WikiPage> getRelatedPages()
    {
        // Only the main page holds the list, so defer to that, if this is
        // itself a
        // related list.
        if (getWikiName().getRelation() != null) {
            return getMainPage().getRelatedPages();
        }

        ensureRelatedPages();
        return _relatedPages.values();
    }

    public WikiPage getMainPage()
    {
        if (getWikiName().getRelation() == null) {
            return this;
        } else {
            return WikiEngine.instance().getWikiPage(getWikiName().getRelatedName(null));
        }
    }

    public boolean canEdit()
    {
        WikiEngine engine = WikiEngine.instance();

        uk.co.nickthecoder.pinkwino.security.AuthenticationManager authentication = engine.getAuthenticationManager();
        uk.co.nickthecoder.pinkwino.security.AuthorisationManager authorisation = engine.getAuthorisationManager();

        if ((authentication == null) || (authorisation == null)) {
            return true;
        }

        uk.co.nickthecoder.pinkwino.security.User user = authentication.getUser();
        return authorisation.canEdit(user, this);

    }

    public SearchResults getDependents()
    {
        try {
            return WikiEngine.instance().getMetaData().getDependents(getWikiName());
        } catch (Exception e) {
            e.printStackTrace();
            return new NullSearchResults();
        }
    }

    public SearchResults getBackLinks()
    {
        try {
            return WikiEngine.instance().getMetaData().getBackLinks(getWikiName());
        } catch (Exception e) {
            e.printStackTrace();
            return new NullSearchResults();
        }
    }

    public int compareTo(WikiPage other)
    {
        return getWikiName().compareTo(other.getWikiName());
    }

    public String toString()
    {
        return "WikiPage(" + getWikiName().toString() + ")";
    }

}
