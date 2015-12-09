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

import uk.co.nickthecoder.pinkwino.parser.NodeFactory;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.SimpleParameterDescription;

/**
 * Will translate into html's b tag.
 */
public class Span extends AbstractParentNode implements NodeFactory, SummaryPart
{

    public static Span createStyle(String styleName, String styleValue)
    {
        ParameterDescription stylePD = new SimpleParameterDescription(styleName, false, "",
                        ParameterDescription.USAGE_STYLE);
        Parameter parameter = new Parameter(stylePD, styleValue);

        Span span = new Span();
        Parameters parameters = new Parameters();
        parameters.addParameter(parameter);
        span.setParameters(parameters);

        return span;
    }

    public static Span createClass(String classValue)
    {
        ParameterDescription classPD = ParameterDescription.find("class");
        Parameter parameter = new Parameter(classPD, classValue);

        Span span = new Span();
        Parameters parameters = new Parameters();
        parameters.addParameter(parameter);
        span.setParameters(parameters);

        return span;
    }

    public Span()
    {
        super();
    }

    @Override
    public void render(StringBuffer buffer)
    {
        buffer.append("<span");
        renderParameters(buffer);
        buffer.append(">");
        super.render(buffer);
        buffer.append("</span>");
    }

    @Override
    public boolean isBlock()
    {
        return false;
    }

    @Override
    public Node createNode()
    {
        AbstractNode node = new Span();
        node.setParameters(new Parameters(getParameters()));

        return node;
    }

    @Override
    public String toString()
    {
        return "Span " + getParameters();
    }

}
