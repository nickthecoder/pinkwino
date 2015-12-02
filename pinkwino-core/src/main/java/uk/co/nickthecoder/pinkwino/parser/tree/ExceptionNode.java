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
 * Allows the details of a thrown exception to be reported inside the rendered
 * wiki page. This implementation encloses the exception in html comments, so
 * can't be seen by the end user.
 */

public class ExceptionNode extends ErrorText
{

    private Throwable _throwable;

    /**
     * Determines if the stack trace is show, or just the exception
     */
    private boolean _showStackTrace;

    /**
     * The throwable's toString will be visible to the user.
     */
    public ExceptionNode(Throwable throwable)
    {
        this(throwable, false);
    }

    public ExceptionNode(Throwable throwable, boolean showStackTrace)
    {
        this(throwable, throwable.toString(), showStackTrace);
    }

    /**
     * Unlike the other constructors, this lets you choose the part of the
     * rendered output visible to the client.
     */
    public ExceptionNode(Throwable throwable, String message, boolean showStackTrace)
    {
        super(throwable.toString());

        _throwable = throwable;
        _showStackTrace = showStackTrace;

        // throwable.printStackTrace();
    }

    public boolean isBlock()
    {
        return false;
    }

    public void render(StringBuffer buffer)
    {
        super.render(buffer);

        if (_showStackTrace) {
            buffer.append("<pre>");
        } else {
            buffer.append("<!--");
        }

        StackTraceElement[] elements = _throwable.getStackTrace();

        for (int i = 0; i < elements.length; i++) {
            buffer.append(StandardRenderer.escapeText(elements[i].toString()));
            buffer.append("\n");
        }

        if (_showStackTrace) {
            buffer.append("</pre>");
        } else {
            buffer.append("-->");
        }
    }

    public void text(StringBuffer buffer)
    {
        // Do nothing
    }

}
