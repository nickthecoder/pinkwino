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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.UserMessageException;
import uk.co.nickthecoder.pinkwino.WikiEngine;

public class StandardAuthenticationManager implements AuthenticationManager
{
    protected static Logger _logger = LogManager.getLogger(StandardAuthenticationManager.class);

    public static final String COOKIE_NAME = "PINKWINO_AUTH";

    public static final String SESSION_ATTRIBUTE = "PINKWINO_AUTH";

    private File _passwordFile;

    private Map<String,User> _usersByName; // Map of String userName -> User

    private Map<String,User> _usersByEmail; // Map of String email -> User

    public StandardAuthenticationManager(File passwordFile)
    {
        _passwordFile = passwordFile;

        load();
    }

    public boolean isLoggedIn()
    {
        return getUser() != null;
    }

    public User getUser()
    {
        return WikiEngine.instance().getWikiContext().getUser();
    }

    public User getUserHardWork()
    {
        String sessionToken = (String) WikiEngine.instance().getWikiContext().getServletRequest().getSession()
                        .getAttribute(SESSION_ATTRIBUTE);

        if (sessionToken == null) {
            return null;
        }

        Cookie[] cookies = WikiEngine.instance().getWikiContext().getServletRequest().getCookies();
        if (cookies == null) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {

            if (COOKIE_NAME.equals(cookies[i].getName())) {

                String token = cookies[i].getValue();

                if (!sessionToken.equals(token)) {
                    return null;
                }

                int sep = token.indexOf("|");
                if (sep <= 0) {
                    return null;
                }

                String userName = token.substring(0, sep);

                return (User) _usersByName.get(userName);

            }
        }

        return null;
    }

    public void addUser(User user)
    {
        load();
        _usersByName.put(user.getUserName(), user);
        _usersByEmail.put(user.getEmail(), user);
        save();
    }

    protected User getUserByNameOrEmail(String name)
    {
        User user = (User) _usersByName.get(name);
        if (user == null) {
            user = (User) _usersByEmail.get(name);
        }
        return user;
    }

    protected User authenticate(String userName, String password)
    {
        User user = getUserByNameOrEmail(userName);
        if (user == null) {
            return null;
        }

        String encodedPassword = encode(password);
        if (encodedPassword.equals(user.getEncodedPassword())) {
            return user;
        }
        return null;
    }

    public boolean login(String userName, String password)
    {
        User user = authenticate(userName, password);
        if (user != null) {

            String token = createToken(user.getUserName());
            WikiEngine.instance().getWikiContext().getServletRequest().getSession()
                            .setAttribute(SESSION_ATTRIBUTE, token);

            Cookie cookie = new Cookie(COOKIE_NAME, token);
            cookie.setMaxAge(-1);

            WikiEngine.instance().getWikiContext().getServletResponse().addCookie(cookie);

            return true;
        }

        return false;
    }

    public void logout()
    {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setMaxAge(0);

        WikiEngine.instance().getWikiContext().getServletResponse().addCookie(cookie);
        WikiEngine.instance().getWikiContext().getServletRequest().getSession().setAttribute(SESSION_ATTRIBUTE, null);
    }

    public void changePassword(String userName, String oldPassword, String newPassword)
    {
        User user = authenticate(userName, oldPassword);
        if (user != null) {

            String encodedPassword = encode(newPassword);

            load();
            user.setEncodedPassword(encodedPassword);
            save();

        } else {
            throw new UserMessageException("The old password does not match. Password not changed.");
        }

    }

    protected String encode(String password)
    {
        try {

            byte[] passwordAsBytes = password.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordAsBytes);
            byte[] digest = md.digest();

            StringBuffer buffer = new StringBuffer();
            buffer.append("MD5:");
            for (byte b : digest) {
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() < 2) {
                    buffer.append("0");
                }
                buffer.append(hex);
            }
            return buffer.toString();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);

        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        }
    }

    /**
     * Reads each line from the file, and creates a user entry.
     */
    protected void load()
    {
        _usersByName = new HashMap<String,User>();
        _usersByEmail = new HashMap<String,User>();

        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(_passwordFile)));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                StringTokenizer st = new StringTokenizer(line);
                String userName = st.hasMoreTokens() ? st.nextToken() : "";
                String email = st.hasMoreTokens() ? st.nextToken() : "";
                String encodedPassword = st.hasMoreTokens() ? st.nextToken() : "";

                User user = new StandardUser(userName, email, encodedPassword);
                _usersByName.put(userName, user);
                _usersByEmail.put(email, user);

            }

        } catch (Exception e) {

            _logger.error("Failed to read password file : " + _passwordFile);
            _logger.error(e);

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }

    }

    protected void save()
    {
        Writer writer = null;

        try {
            writer = new OutputStreamWriter(new FileOutputStream(_passwordFile));

            for (Iterator<String> i = _usersByName.keySet().iterator(); i.hasNext();) {
                String userName = i.next();
                User user = (User) _usersByName.get(userName);

                writer.write(user.getUserName());
                writer.write(" ");
                writer.write(user.getEmail());
                writer.write(" ");
                writer.write(user.getEncodedPassword());
                writer.write("\n");
            }

        } catch (Exception e) {

            _logger.error("Failed to write password file : " + _passwordFile);
            _logger.error(e);

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     * This is probably close to useless, if you can do better PLEASE send me
     * the code.
     */
    protected String createToken(String userName)
    {
        return userName + "|" + Math.random();
    }

}
