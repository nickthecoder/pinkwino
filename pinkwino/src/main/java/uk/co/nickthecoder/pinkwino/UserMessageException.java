/* {{{ GPL
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
}}} */

package uk.co.nickthecoder.pinkwino;

/**
 * An exception who's message can be displlayed to the end user. If I ever get
 * round to doing I18N, this is the first do.
 */

public class UserMessageException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public UserMessageException(String message)
    {
        super(message);
    }

    public UserMessageException(Exception e)
    {
        super(e);
    }

}

