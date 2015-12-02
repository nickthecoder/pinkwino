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

import java.util.Date;
import java.util.List;

import uk.co.nickthecoder.pinkwino.link.ViewHandler;
import uk.co.nickthecoder.pinkwino.storage.WikiStorage;

/**
 * A namespace has a set of pages associated with it, and each namespace can
 * have its own rules.
 */

public class Namespace
{
    public static final String DEFAULT_NAME = "default";

    /**
     * Does the namespace exists
     */
    private boolean _exists;

    /**
     * The object in charge of persisting the wiki pages within this namespace
     */
    private WikiStorage _storage;

    /**
     * The name of this namespace. The default namespace has a name of "default"
     */
    private String _name;

    /**
     * The name of the index page for this namespace. The default value is
     * "index"
     */
    public WikiName _indexName;

    /**
     * The name of the main page for this namespace. The default value is "home"
     */
    public WikiName _homeName;

    /**
     * If this namespace is a "special" one, where the wiki text is only a small
     * part of the real page (i.e. pinkwino is acting as a wiki component,
     * rather than a wiki application), then this will be responsible for
     * handling requests to the wiki pages.
     */
    public ViewHandler _viewHandler;

    public Namespace(String name)
    {
        this(name, "Home", "Index");
    }

    public Namespace(String name, String homePage, String indexName)
    {
        _name = name;
        _exists = true;

        _indexName = new WikiName(this, indexName);
        _homeName = new WikiName(this, homePage);
        _viewHandler = null;
    }

    /**
     * Get method for attribute {@link #_exists}. Does the namespace exists
     */
    public boolean getExists()
    {
        return _exists;
    }

    /**
     * Set method for attribute {@link #_exists}. Does the namespace exists
     */
    public void setExists(boolean value)
    {
        _exists = value;
    }

    /**
     * Get method for attribute {@link #_storage}. The object in charge of
     * persisting the wiki pages within this namespace
     */
    public WikiStorage getStorage()
    {
        return _storage;
    }

    /**
     * Set method for attribute {@link #_storage}. The object in charge of
     * persisting the wiki pages within this namespace
     */
    public void setStorage(WikiStorage value)
    {
        _storage = value;
    }

    /**
     * Get method for attribute {@link #_name}. The name of this namespace. The
     * default namespace has a name of "default"
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Set method for attribute {@link #_name}. The name of this namespace. The
     * default namespace has a name of "default"
     */
    public void setName(String value)
    {
        _name = value;
    }

    /**
     * Set method for attribute {@link #_indexName}.
     */
    public void setIndexName(WikiName value)
    {
        _indexName = value;
    }

    /**
     * Get method for attribute {@link #_indexName}.
     */
    public WikiName getIndexName()
    {
        return _indexName;
    }

    /**
     * Set method for attribute {@link #_homeName}.
     */
    public void setHomeName(WikiName value)
    {
        _homeName = value;
    }

    /**
     * Get method for attribute {@link #_homeName}.
     */
    public WikiName getHomeName()
    {
        return _homeName;
    }

    public void setViewHandler(ViewHandler value)
    {
        _viewHandler = value;
    }

    public ViewHandler getViewHandler()
    {
        return _viewHandler;
    }

    public List<WikiPage> getPages()
    {
        return _storage.index(this);
    }

    public List<WikiPage> getRecentChanges(Date since)
    {
        return _storage.recentChanges(this, since);
    }

    public String toString()
    {
        return "(" + getName() + ")";
    }

}
