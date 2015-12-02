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

/**
 * Does no authentication, everybody is treated as if they are not logged in.
 * The reason for this class is to ensure that
 * WikiEngine.getAuthenticationManager() never has to return null (which makes
 * the rest of the system easy to code).
 */

public class NullAuthenticationManager implements AuthenticationManager
{

    public NullAuthenticationManager()
    {
    }

    public boolean isLoggedIn()
    {
        return false;
    }

    public User getUser()
    {
        return null;
    }

    public User getUserHardWork()
    {
        return null;
    }

    public void addUser(User user)
    {
    }

    public boolean login(String userName, String password)
    {
        return false;
    }

    public void logout()
    {
    }

    public void changePassword(String userName, String oldPassword, String newPassword)
    {
    }

}
