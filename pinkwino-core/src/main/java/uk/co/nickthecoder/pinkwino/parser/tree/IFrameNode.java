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

import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;

/**
*/

public class IFrameNode extends AbstractNode
{
    public IFrameNode(String url, int width, int height)
    {
        super();

        this.addParameter(new Parameter(ParameterDescription.find("html:width"), Integer.toString(width)));

        this.addParameter(new Parameter(ParameterDescription.find("html:height"), Integer.toString(height)));

        this.addParameter(new Parameter(ParameterDescription.find("src"), url));
    }

    public void render(StringBuffer buffer)
    {

        buffer.append("<iframe ");

        renderParameters(buffer);

        buffer.append("></iframe>");

    }

    public boolean isBlock()
    {
        return true;
    }

}
