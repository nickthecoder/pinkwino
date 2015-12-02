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

/**
 * Gives details for a given plugin.
 */
public class PluginPlugin extends JspPlugin
{

    public PluginPlugin()
    {
        this("plugin", "plugin.jsp");
    }

    public PluginPlugin(String name, String jspPath)
    {
        super(name, jspPath, BODY_TYPE_NONE);

        addParameterDescription(ParameterDescription.find("plugin").required());
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        Parameter pluginParameter = parameters.getParameter("plugin");
        if (pluginParameter == null) {
            return new ErrorText("plugin=???");
        }

        Plugin plugin = WikiEngine.instance().getPluginManager().getPlugin(pluginParameter.getValue());
        if (plugin == null) {
            return new ErrorText("plugin=???");
        }

        jspNode.setRequestAttribute("plugin", plugin);

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}
