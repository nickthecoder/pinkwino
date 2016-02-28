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

import java.util.Iterator;

import uk.co.nickthecoder.pinkwino.WikiConstants;
import uk.co.nickthecoder.pinkwino.parser.StandardRenderer;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Does some common work for many Nodes. AbstractNodes have a set of Parameters,
 * these parameters typically hold addition information from the wiki page
 * writer, such as the width of an element. For example, the table cell can be
 * started as follows :
 * 
 * <pre>
 {|( width="100%" )
 * 
 * <pre>
 In this example, everything inside the paratheses are parameters.
 */

public abstract class AbstractNode implements Node
{

    private Parameters _parameters;

    public AbstractNode()
    {
        _parameters = null;
    }

    public void text(StringBuffer buffer)
    {
        // do nothing
    }

    public void addParameter( Parameter parameter )
    {
        if (this._parameters == null) {
            this._parameters = new Parameters();
        }
        this._parameters.addParameter(parameter);
    }
    
    public void setParameters(Parameters parameters)
    {
        _parameters = parameters;
    }

    public Parameters getParameters()
    {
        return _parameters;
    }

    protected void renderParameters(StringBuffer buffer)
    {
        if (_parameters != null) {
            StringBuffer styleBuffer = null;

            for (Iterator<Parameter> i = _parameters.iterator(); i.hasNext();) {
                Parameter parameter = i.next();
                if (WikiConstants.USAGE_HTML.equals(parameter.getUsage())) {

                    buffer.append(" ").append(parameter.getName()).append("=\"");
                    buffer.append(StandardRenderer.escapeText(parameter.getValue())).append("\"");

                } else if (WikiConstants.USAGE_STYLE.equals(parameter.getUsage())) {

                    if (styleBuffer == null) {
                        styleBuffer = new StringBuffer();
                    }
                    styleBuffer.append(parameter.getName()).append(":").append(parameter.getValue()).append(";");

                }
            }
            if (styleBuffer != null) {
                buffer.append(" style=\"").append(styleBuffer).append("\"");
            }
        }
    }

    abstract public boolean isBlock();

    abstract public void render(StringBuffer buffer);

}
