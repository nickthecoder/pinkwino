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

package uk.co.nickthecoder.pinkwino.plugins;

import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * Gives details for a given plugin.
 */
public class BoxPlugin extends JspPlugin
{

    public BoxPlugin()
    {
        this("box", "box.jsp");
    }

    public BoxPlugin(String name, String jspPath)
    {
        super(name, jspPath, BODY_TYPE_WIKI);

        addParameterDescription(new RegexParameterDescription("title", ".*"));

        addParameterDescription(ParameterDescription.find("width").defaultValue("100%"));

        addParameterDescription(ParameterDescription.find("class").defaultValue("wiki_box"));
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        // MORE - why is this needed?
        jspNode.setRequestAttribute("className", parameters.getParameter("class"));

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}

