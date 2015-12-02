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

import uk.co.nickthecoder.pinkwino.parser.StandardRenderer;

/**
 * A piece of plain text. The only rendering necessary is to escape the text for
 * special html characters such as 'less than' symbols, and ampersands.
 */

public class PlainText implements Node, SummaryPart
{

    /**
     * The plain text
     */
    private String _text;

    public PlainText(String text)
    {
        _text = text;
    }

    /**
     * Get method for parameter {@link #_text}. The plain text
     */
    public String getText()
    {
        return _text;
    }

    /**
     * Set method for parameter {@link #_text}. The plain text
     */
    public void setText(String value)
    {
        _text = value;
    }

    public void render(StringBuffer buffer)
    {
        buffer.append(StandardRenderer.escapeText(getText()));
    }

    public void text(StringBuffer buffer)
    {
        buffer.append(getText());
    }

    public boolean isBlock()
    {
        return false;
    }

    public String toString()
    {
        return "TEXT:'" + _text + "'";
    }

}
