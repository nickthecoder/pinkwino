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

import uk.co.nickthecoder.pinkwino.parser.tree.SimpleParentNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * A plugin that places its body within a html span. The css class can either be
 * specified when creating the plugin, or when it is used within a wiki page.
 */

public class SpanPlugin extends AbstractVisualPlugin
{

    private String _cssClass;

    public SpanPlugin(String name)
    {
        super(name, BODY_TYPE_WIKI);
        _cssClass = null;

        addParameterDescription(ParameterDescription.find("class").required());
    }

    public SpanPlugin(String name, String cssClass)
    {
        this(name);
        _cssClass = cssClass;
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {

        SimpleParentNode span = new SimpleParentNode("span", false);

        if (_cssClass != null) {
            Parameters spanParameters = new Parameters();
            spanParameters.addParameter(new Parameter(ParameterDescription.find("class"), _cssClass));
            span.setParameters(spanParameters);
        } else {
            span.setParameters(parameters);
        }

        pluginSupport.begin(span);

        return null;
    }

}
