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

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;

public class IncludeNode extends AbstractNode
{

    private WikiPage _wikiPage;

    private String _editPosition;

    public IncludeNode(WikiPage wikiPage, String editPosition)
    {
        super();
        _wikiPage = wikiPage;
        _editPosition = editPosition;
    }

    public void render(StringBuffer buffer)
    {

        if (_editPosition.equals("top")) {
            renderEditLink(buffer);
        }

        buffer.append(_wikiPage.render());

        if (_editPosition.equals("bottom")) {
            renderEditLink(buffer);
        }
    }

    protected void renderEditLink(StringBuffer buffer)
    {
        String url = _wikiPage.getEditUrl();
        if ((url != null) && (WikiEngine.instance().getAttributes().getAttribute("wiki_staticSite") == null)) {

            buffer.append("<div class=\"wiki_editInclude\">").append("<a href=\"").append(_wikiPage.getEditUrl())
                            .append("\">").append("edit").append("</a>").append("</div>\n");
        }
    }

    public boolean isBlock()
    {
        return true;
    }

}
