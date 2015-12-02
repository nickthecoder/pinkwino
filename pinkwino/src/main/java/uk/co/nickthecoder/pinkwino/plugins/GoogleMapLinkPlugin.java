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
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * Renders a google map, with optional directions from place to place.
 */
public class GoogleMapLinkPlugin extends JspPlugin
{

    public GoogleMapLinkPlugin()
    {
        this("googleMapLink");
    }

    public GoogleMapLinkPlugin(String name)
    {
        super(name, "googleMapLink.jsp", BODY_TYPE_NONE);

        addParameterDescription(new RegexParameterDescription("label", ".*"));
        addParameterDescription(new RegexParameterDescription("longitude", ".*"));
        addParameterDescription(new RegexParameterDescription("latitude", ".*"));
        addParameterDescription(new RegexParameterDescription("zoom", "[0-9]*").defaultValue("15"));
        addParameterDescription(new RegexParameterDescription("target", ".*"));
        addParameterDescription(new RegexParameterDescription("start", ".*"));
        addParameterDescription(new RegexParameterDescription("startLongitude", ".*"));
        addParameterDescription(new RegexParameterDescription("startLatitude", ".*"));
        setBlock(false);
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}

