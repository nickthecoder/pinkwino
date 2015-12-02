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

import uk.co.nickthecoder.pinkwino.parser.StandardRenderer;

/**
 * Renders as a html br tag.
 */

public class ImageNode extends AbstractNode
{

    private String _href;

    private String _alt;

    public ImageNode(String href, String alt)
    {
        super();
        _href = href;
        _alt = alt;
    }

    public void render(StringBuffer buffer)
    {
        buffer.append("<img");
        StandardRenderer.parameter(buffer, "src", _href);
        StandardRenderer.parameter(buffer, "alt", _alt == null ? "" : _alt);
        buffer.append("/>");
    }

    public boolean isBlock()
    {
        return false;
    }

}
