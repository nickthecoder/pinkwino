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
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * Renders a google map, with optional directions from place to place.
 */

public class GoogleMapPlugin extends JspPlugin
{

    public ParameterDescription KEY_PARAMETER_DESCRIPTION = new RegexParameterDescription("key", ".*");

    public GoogleMapPlugin()
    {
        this("googleMap");
    }

    public GoogleMapPlugin(String name)
    {
        super(name, "googleMap.jsp", BODY_TYPE_WIKI);

        addParameterDescription(new RegexParameterDescription("longitude", ".*"));
        addParameterDescription(new RegexParameterDescription("latitude", ".*"));
        addParameterDescription(new RegexParameterDescription("zoom", "[0-9]*").defaultValue("15"));
        addParameterDescription(new RegexParameterDescription("target", ".*"));
        addParameterDescription(new RegexParameterDescription("start", ".*"));
        addParameterDescription(new RegexParameterDescription("startLongitude", ".*"));
        addParameterDescription(new RegexParameterDescription("startLatitude", ".*"));
        addParameterDescription(KEY_PARAMETER_DESCRIPTION);
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        Parameter keyParameter = parameters.getParameter("key");
        if ((keyParameter == null) || ("".equals(keyParameter.getValue()))) {

            String key = (String) WikiEngine.instance().getAttributes().getAttribute("googleMap_key");

            if (key == null) {
                return new ErrorText("Google map key not specified");
            } else {
                parameters.addParameter(new Parameter(KEY_PARAMETER_DESCRIPTION, key));
            }
        }

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}
