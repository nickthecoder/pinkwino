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

package uk.co.nickthecoder.pinkwino.parser;

/**
 * Holds the results of Parser.getRemainder
 */

public class RemainderResult
{

    /**
     * The terminator that was found, or null if none was found
     */
    private String _terminator;

    /**
     * The text found, not including the terminator
     */
    private String _text;

    public RemainderResult(String text, String terminator)
    {
        _text = text;
        _terminator = terminator;
    }

    /**
     * Get method for parameter {@link #_terminator}.
     */
    public String getTerminator()
    {
        return _terminator;
    }

    /**
     * Set method for parameter {@link #_terminator}.
     */
    public void setTerminator(String value)
    {
        _terminator = value;
    }

    /**
     * Get method for parameter {@link #_text}.
     */
    public String getText()
    {
        return _text;
    }

    /**
     * Set method for parameter {@link #_text}.
     */
    public void setText(String value)
    {
        _text = value;
    }

}
