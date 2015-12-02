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

package uk.co.nickthecoder.pinkwino.link;

/**
 * Holds information about a single type of external url. For example, "http:",
 * is a link the world wide web, and may incude a icon of the world next to
 * links from wiki pages to show that it is an external link. Similarly
 * "mailto:" may use an envelope icon.
 * 
 * This can also be used for other types of external links. For example
 * "google:" can be used to search for images, so in a wiki page [google:tree]
 * will link to the google search results for "tree". We could use a google icon
 * next to these types of links.
 * 
 * Information about the icons aren't stored here, instead, we have a css class,
 * and that css class may may an image.
 */

public class ExternalLinkType implements Comparable<ExternalLinkType>
{

    private String _prefix;

    private String _template;

    private String _cssClass;

    private boolean _escape;

    public ExternalLinkType(String prefix, String template, boolean escape)
    {
        this(prefix, template, escape, null);
    }

    public ExternalLinkType(String prefix, String template, boolean escape, String cssClass)
    {
        this._prefix = prefix;
        this._template = template;
        this._escape = escape;
        this._cssClass = cssClass;
    }

    public String getPrefix()
    {
        return _prefix;
    }

    public String getTemplate()
    {
        return _template;
    }

    public boolean getEscape()
    {
        return _escape;
    }

    /**
     * Can be null
     */
    public String getCssClass()
    {
        return _cssClass;
    }

    public String resolve(String remainder)
    {
        if (_escape) {
            return _template.replaceAll("%1", uk.co.nickthecoder.webwidgets.util.TagUtil.encodeUrl(remainder));
        } else {
            return _template.replaceAll("%1", uk.co.nickthecoder.webwidgets.util.TagUtil.safeText(remainder));
        }
    }

    public int compareTo(ExternalLinkType other)
    {
        return _prefix.compareTo(other._prefix);
    }

}
