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

package uk.co.nickthecoder.pinkwino.optional.publish;

/**
*/

public class FtpDetails
{
    private String _server;

    private String _username;

    private String _password;

    private String _remoteBase;

    private String _localBase;

    private String _timeZoneId; // Can be null, in which case, it is left
                                // unchanged.

    public FtpDetails(String server, String username, String password)
    {
        this(server, username, password, "/", "/DEFAULT_LOCAL_DIRECTORY", null);
    }

    public FtpDetails(String server, String username, String password, String timeZoneId)
    {
        this(server, username, password, "/", "/DEFAULT_LOCAL_DIRECTORY", timeZoneId);
    }

    public FtpDetails(String server, String username, String password, String remoteBase, String localBase)
    {
        this(server, username, password, remoteBase, localBase, null);
    }

    public FtpDetails(String server, String username, String password, String remoteBase, String localBase,
                    String timeZoneId)
    {
        _server = server;
        _username = username;
        _password = password;

        _remoteBase = remoteBase;
        _localBase = localBase;
        _timeZoneId = timeZoneId;
    }

    public String getServer()
    {
        return _server;
    }

    public String getUsername()
    {
        return _username;
    }

    public String getPassword()
    {
        return _password;
    }

    public void setRemoteBase(String remoteBase)
    {
        _remoteBase = remoteBase;
    }

    public String getRemoteBase()
    {
        return _remoteBase;
    }

    public void setLocalBase(String localBase)
    {
        _localBase = localBase;
    }

    public String getLocalBase()
    {
        return _localBase;
    }

    public String getTimeZoneId()
    {
        return _timeZoneId;
    }

}
