/* {{{ GLP

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

import uk.co.nickthecoder.pinkwino.link.UrlManager;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;

/**
 * A version of a wiki page, holds the actual content as well as meta data such
 * as the date, and version number.
 */

public class Version implements Comparable<Version>
{

    private WikiPage _wikiPage;

    private WikiDocument _wikiDocument;

    /**
     * versionNumber
     */
    private int _versionNumber;

    /**
     * The date this version was created
     */
    private Date _date;

    /**
     * The wiki syntax
     */
    private String _content;

    /**
     * Does this verison have a media file associated with it
     */
    private boolean _hasMedia;

    public Version(WikiPage wikiPage)
    {
        _wikiPage = wikiPage;
        _versionNumber = -1;
        _content = null;
        _date = new Date();
    }

    public WikiPage getWikiPage()
    {
        return _wikiPage;
    }

    public WikiDocument getWikiDocument()
    {
        if (_wikiDocument == null) {
            WikiEngine.instance().getRenderer().parse(this);
        }

        return _wikiDocument;
    }

    public String render()
    {
        return WikiEngine.instance().getRenderer().render(this);
    }

    /** Same as render, but useful in a jsp context */
    public String getRendered()
    {
        return render();
    }

    /**
     * Parses the wiki markup to find just the text.
     * @return The content of the wiki page stripped of the wiki markup.
     */
    public String getPlainContent()
    {
        WikiDocument wikiDocument = getWikiDocument();
        Node rootNode = wikiDocument.getRootNode();

        StringBuffer buffer = new StringBuffer();
        rootNode.text(buffer);
        return buffer.toString().replaceAll("\\s\\s*", " ");
    }

    public void setWikiDocument(WikiDocument value)
    {
        _wikiDocument = value;
    }

    /**
     * Get method for attribute {@link #_versionNumber}.
     */
    public int getVersionNumber()
    {
        return _versionNumber;
    }

    /**
     * Set method for attribute {@link #_versionNumber}.
     */
    public void setVersionNumber(int value)
    {
        _versionNumber = value;
    }

    /**
     * Get method for attribute {@link #_date}. The date this version was
     * created
     */
    public Date getDate()
    {
        return _date;
    }

    /**
     * Set method for attribute {@link #_date}. The date this version was
     * created
     */
    public void setDate(Date value)
    {
        _date = value;
    }

    public boolean getHasMedia()
    {
        return _hasMedia;
    }

    public void setHasMedia(boolean value)
    {
        _hasMedia = value;
    }

    public boolean isImage()
    {
        if (_hasMedia) {
            return getWikiPage().getWikiName().isImage();
        } else {
            return false;
        }
    }

    public String getViewUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_VIEW, this);
    }

    public String getRevertUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_REVERT, this);
    }

    public String getSaveUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_SAVE, this);
    }

    public String getRawUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_RAW, this);
    }

    public String getMediaUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_MEDIA, this);
    }

    private void ensureContent()
    {
        if (_content == null) {
            getWikiPage().getWikiStorage().loadVersion(this);
        }
    }

    /**
     * Get method for attribute {@link #_content}. The wiki syntax
     */
    public String getContent()
    {
        ensureContent();

        return _content;
    }

    /**
     * Gets a single section of markup
     */
    public String getSection(String sectionId)
    {
        ensureContent();
        WikiDocument wikiDocument = WikiEngine.instance().getRenderer().parse(this);

        Section section = wikiDocument.getSection(sectionId);
        if (section == null) {
            return "";
        }

        return section.getContent();
    }

    /**
     * Set method for attribute {@link #_content}. The wiki syntax
     */
    public void setContent(String value)
    {
        _content = value;
    }

    public int compareTo(Version version)
    {
        if (version == null) {
            return -1;
        }
        return getVersionNumber() - version.getVersionNumber();
    }

}
