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

public interface AuthenticationManager
{

    public boolean isLoggedIn();

    /**
     * Gets the current user
     */
    public User getUser();

    /**
     * Only to be used by WikiContext. This does the hard work, whereas
     * getUser() just asks the WikiContext for the user. The WikiContext then
     * asks getUser2() for the answer, unless it already knows.
     */
    public User getUserHardWork();

    public void addUser(User user);

    public boolean login(String userName, String password);

    public void logout();

    public void changePassword(String userName, String oldPassword, String newPassword);

}
