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

import uk.co.nickthecoder.pinkwino.WikiConstants;

/**
 * Used to show syntax errors in wiki syntax
 */

public class ErrorText extends PlainText
{

    private static String PREFIX = "ERROR : <span class=\"" + WikiConstants.CSS_EXCEPTION + "\">";

    private static String SUFFIX = "</span>";

    private boolean _lineBreak;

    public ErrorText(String text)
    {
        this(text, true);
    }

    public ErrorText(String text, boolean lineBreak)
    {
        super(text);
        _lineBreak = lineBreak;
    }

    public void render(StringBuffer buffer)
    {
        buffer.append(PREFIX);
        super.render(buffer);
        buffer.append(SUFFIX);
        if (_lineBreak) {
            buffer.append("<br/>");
        }
    }

}
