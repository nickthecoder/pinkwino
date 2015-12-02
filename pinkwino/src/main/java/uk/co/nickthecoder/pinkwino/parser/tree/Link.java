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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.parser.StandardRenderer;

/**
 * Creates a html a tag
 */

public class Link extends AbstractParentNode implements SummaryPart
{

    protected static Logger _logger = LogManager.getLogger(Link.class);

    /**
     * Either a wiki link, or an external link
     */
    private LinkDestination _destination;

    public Link()
    {
        super();
    }

    /**
     * Get method for parameter {@link #_destination}. Either a wiki link, or an
     * external link
     */
    public LinkDestination getDestination()
    {
        return _destination;
    }

    /**
     * Set method for parameter {@link #_destination}. Either a wiki link, or an
     * external link
     */
    public void setDestination(LinkDestination value)
    {
        _destination = value;
    }

    public void render(StringBuffer buffer)
    {
        String href = getHref();

        if (href == null) {

            ErrorText error = new ErrorText("[ Bad Link: " + _destination + " ] ");
            error.render(buffer);
            super.render(buffer);

        } else {

            buffer.append("<a");
            String cssClass = getCssClass();

            if (cssClass != null) {
                StandardRenderer.parameter(buffer, "class", cssClass);
            }

            StandardRenderer.unescapedParameter(buffer, "href", getHref());
            buffer.append(">");
            super.render(buffer);
            buffer.append("</a>");

        }
    }

    public boolean isBlock()
    {
        return false;
    }

    public String getHref()
    {
        return _destination.getHref();
    }

    public String getCssClass()
    {
        return _destination.getCssClass();
    }

}
