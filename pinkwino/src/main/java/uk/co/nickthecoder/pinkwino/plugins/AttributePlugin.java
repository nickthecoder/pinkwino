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

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainText;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * Returns the value of a named WikiEngine attribute.
 * 
 * Attributes are set in the initialisation/configuration bean shell.
 */

public class AttributePlugin extends AbstractVisualPlugin
{

    public AttributePlugin()
    {
        this("attribute");
    }

    public AttributePlugin(String name)
    {
        super(name, BODY_TYPE_NONE);

        addParameterDescription(new RegexParameterDescription("name", ".*"));
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        String attributeName = parameters.getParameter("name").getValue();

        Object attribute = WikiEngine.instance().getAttributes().getAttribute(attributeName);

        if (attribute == null) {
            pluginSupport.add(new ErrorText("Attribute " + attributeName + " not found"));
        } else {
            pluginSupport.add(new PlainText(attribute.toString()));
        }

        return null;
    }

}

