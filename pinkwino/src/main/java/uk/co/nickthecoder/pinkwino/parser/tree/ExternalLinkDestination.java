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

package uk.co.nickthecoder.pinkwino.parser.tree;

/**
 * Links to an external web page
 */

public class ExternalLinkDestination implements LinkDestination
{

    private String _href;

    private String _cssClass;

    public ExternalLinkDestination(String href)
    {
        this(href, null);
    }

    public ExternalLinkDestination(String href, String cssClass)
    {
        _href = href;
        _cssClass = cssClass;
    }

    public String getHref()
    {
        return _href;
    }

    public String getText()
    {
        return _href;
    }

    public String getCssClass()
    {
        return _cssClass;
    }

}
