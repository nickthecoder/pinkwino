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

import uk.co.nickthecoder.pinkwino.parser.tree.AutoParagraphBlock;
import uk.co.nickthecoder.pinkwino.parser.tree.SimpleParentNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * A plugin that places its body within a html div. The css class can either be
 * specified when creating the plugin, or when it is used within a wiki page.
 * 
 * Can be used instead of the built-in "{{[" markup.
 * 
 * Useful with commonly used css classes such as a class to float right/left.
 * 
 * The body can optional ignore paragraphs (i.e. no html p tags will be
 * created), using the "paragraphs" parameter.
 */

public class DivPlugin extends AbstractVisualPlugin
{

    private String _cssClass;

    public DivPlugin(String name)
    {
        super(name, BODY_TYPE_WIKI);
        _cssClass = null;

        addParameterDescription(ParameterDescription.find("class").required());
        addParameterDescription(ParameterDescription.find("paragraphs").defaultValue("true"));
    }

    public DivPlugin(String name, String cssClass)
    {
        this(name, cssClass, true);
    }

    public DivPlugin(String name, String cssClass, boolean paragraphs)
    {
        super(name, BODY_TYPE_WIKI);
        _cssClass = cssClass;

        addParameterDescription(ParameterDescription.find("paragraphs").defaultValue("" + paragraphs));
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {

        SimpleParentNode div = new SimpleParentNode("div", true);

        if (_cssClass != null) {
            div.addParameter(new Parameter(ParameterDescription.find("class"), _cssClass));
        } else {
            div.setParameters(parameters);
        }

        pluginSupport.begin(div);
        if (parameters.getParameter("paragraphs").getValue().equals("true")) {
            pluginSupport.begin(new AutoParagraphBlock());
        }

        return null;
    }

}
