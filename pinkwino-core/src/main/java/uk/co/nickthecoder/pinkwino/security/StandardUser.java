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

package uk.co.nickthecoder.pinkwino.security;

public class StandardUser implements User
{
    private String _userName;

    private String _email;

    private String _encodedPassword;

    public StandardUser(String userName, String email, String encodedPassword)
    {
        _userName = userName;
        _email = email;
        _encodedPassword = encodedPassword;
    }

    public String getUserName()
    {
        return _userName;
    }

    public String getEmail()
    {
        return _email;
    }

    public String getEncodedPassword()
    {
        return _encodedPassword;
    }

    public void setEncodedPassword(String value)
    {
        _encodedPassword = value;
    }

    public String toString()
    {
        return getUserName();
    }

}
