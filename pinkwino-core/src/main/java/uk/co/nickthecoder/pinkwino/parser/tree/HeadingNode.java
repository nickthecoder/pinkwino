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

import uk.co.nickthecoder.pinkwino.Section;
import uk.co.nickthecoder.pinkwino.WikiEngine;

/**
 * Will translate into html's b tag.
 */

public class HeadingNode extends PlainParentNode
{

    private Section _section;

    public HeadingNode(Section section)
    {
        super(true);
        _section = section;
    }

    public void render(StringBuffer buffer)
    {
        String hTag = "h" + (_section.getLevel() + 1);

        buffer.append("<a name=\"section" + _section.getLinkName() + "\"></a>");

        buffer.append("<").append(hTag);
        renderParameters(buffer);
        buffer.append(">");
        super.render(buffer);
        buffer.append("</").append(hTag).append(">");

        // There are two ways that the edit section is not rendered.
        // If the UrlManager doesn't supply a url for the edit link.
        // If the wiki is in "static" mode, where the page is to look like a
        // static (non-editable).
        String url = _section.getEditUrl();
        if ((url != null) && (WikiEngine.instance().getAttributes().getAttribute("wiki_staticSite") == null)) {
            buffer.append("<div class=\"wiki_editSection wiki_editSection").append(_section.getLevel() + 1)
                            .append("\">");
            buffer.append("<a href=\"").append(url).append("\">");
            buffer.append("edit");
            buffer.append("</a>");
            buffer.append("</div>");
        }

    }

}
