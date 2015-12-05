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

public class SimpleParentNode extends PlainParentNode implements NodeFactory, Cloneable, SummaryPart
{
    private String _tagName;
    
    private String _cssClass;

    public SimpleParentNode(String tagName, boolean isBlock)
    {
        this( tagName, isBlock, null );
    }
    
    public SimpleParentNode(String tagName, boolean isBlock, String cssClass)
    {
        super(isBlock);
        _tagName = tagName;
        _cssClass = cssClass;
    }

    public String getTagName()
    {
        return _tagName;
    }

    public void render(StringBuffer buffer)
    {
        buffer.append("<").append(_tagName);
        renderParameters(buffer);
        if (_cssClass != null) {
            buffer.append( " class=\"" ).append( _cssClass ).append("\"");
        }
        buffer.append(">");
        super.render(buffer);
        buffer.append("</").append(_tagName).append(">");
    }

    public Node createNode()
    {
        try {
            return (Node) this.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
