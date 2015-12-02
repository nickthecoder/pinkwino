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

package uk.co.nickthecoder.pinkwino.security;

// {{{ imports
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiPage;

public class IPAuthorisationManager implements AuthorisationManager
{
    protected static Logger _logger = LogManager.getLogger(IPAuthorisationManager.class);

    private List<Pattern> _patterns;

    public IPAuthorisationManager()
    {
        _logger.debug("Creating IPAuthorisationManager");
        _patterns = new ArrayList<Pattern>();
    }

    public IPAuthorisationManager(String pattern)
    {
        this();
        addAllowedIPAddress(pattern);
    }

    public IPAuthorisationManager(String[] patterns)
    {
        this();
        for (int i = 0; i < patterns.length; i++) {
            addAllowedIPAddress(patterns[i]);
        }
    }

    /**
     * ipaddresses is in the form x.x.x.x and if a '*' is included, then it is
     * treated as a wildcard character (i.e. is replaced by '.*' in a regex
     * expression.
     */
    public final void addAllowedIPAddress(String ipAddresses)
    {
        String pattern = ipAddresses.replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*");

        Pattern p = Pattern.compile(pattern);
        _patterns.add(p);
    }

    public boolean canEdit(User user, WikiPage wikiPage)
    {

        HttpServletRequest request = WikiContext.getWikiContext().getServletRequest();
        if (request == null) {
            return false;
        }
        String address = request.getRemoteAddr();

        for (Iterator<Pattern> i = _patterns.iterator(); i.hasNext();) {
            Pattern p = i.next();
            Matcher m = p.matcher(address);
            if (m.matches()) {
                return true;
            }
        }

        return false;
    }

    public boolean canDelete(User user, WikiPage wikiPage)
    {
        return canEdit(user, wikiPage);
    }

}
