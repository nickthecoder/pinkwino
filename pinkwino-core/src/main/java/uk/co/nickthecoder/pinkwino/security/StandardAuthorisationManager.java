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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiConstants;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

public class StandardAuthorisationManager implements AuthorisationManager
{

    protected static Logger _logger = LogManager.getLogger(StandardAuthorisationManager.class);

    public static final int DEFAULT_DEPTH_LIMIT = 3;

    private Set<String> _superUsers;

    private String _namespaceSecurityPageName;

    /**
     * When security pages are included, then this limits the depth of
     * iteration. The most likely time this will come into affect is if there is
     * mutual recursion, and without some limit, it would recurse forever.
     */
    private int _depthLimit;

    public StandardAuthorisationManager()
    {
        this("security");
    }

    public StandardAuthorisationManager(String namespaceSecurityPageName)
    {
        this(namespaceSecurityPageName, DEFAULT_DEPTH_LIMIT);
    }

    public StandardAuthorisationManager(String namespaceSecurityPageName, int depthLimit)
    {
        _superUsers = new HashSet<String>();
        _namespaceSecurityPageName = namespaceSecurityPageName;
        _depthLimit = depthLimit;
    }

    public void addSuperUser(String name)
    {
        _superUsers.add(name);
    }

    public void removeSuperUser(String name)
    {
        _superUsers.remove(name);
    }

    public boolean canEdit(User user, WikiPage wikiPage)
    {
        String userName = (user == null) ? null : user.getUserName();

        if (isSuperUser(userName)) {
            return true;
        }

        WikiPage securityPage = wikiPage.getRelatedPage(WikiConstants.RELATION_SECURITY);

        if (securityPage.getExists()) {

            return canEdit(userName, wikiPage, securityPage, 0);

        } else {

            WikiPage namespaceSecurityPage = getNamespaceSecurityPage(wikiPage);

            if (namespaceSecurityPage.getExists()) {

                return canEdit(userName, wikiPage, namespaceSecurityPage, 0);

            }
        }

        return false;
    }

    /**
     * According to the security page given, can this user edit the page?
     */
    protected boolean canEdit(String userName, WikiPage wikiPage, WikiPage securityPage, int depth)
    {
        if (depth >= _depthLimit) {
            _logger.warn("StandardAuthorisationManager.canEdit reached its depth limit.");
            return false;
        }

        Version securityVersion = securityPage.getCurrentVersion();

        WikiDocument wikiDocument = WikiEngine.instance().getRenderer().parse(securityVersion);

        PageSecurity pageSecurity = PageSecurity.getPageSecurity(wikiDocument);

        if (pageSecurity.canEdit(userName)) {
            return true;
        }

        for (Iterator<WikiPage> i = pageSecurity.includedPages(); i.hasNext();) {
            WikiPage subPage = i.next();

            if (canEdit(userName, wikiPage, subPage, depth + 1)) {
                return true;
            }
        }

        return false;
    }

    public boolean canDelete(User user, WikiPage wikiPage)
    {
        String userName = (user == null) ? null : user.getUserName();

        return isSuperUser(userName);
    }

    /**
     * Can all pages be edited by this person?
     */
    protected boolean isSuperUser(String userName)
    {
        return _superUsers.contains(userName);
    }

    protected WikiPage getNamespaceSecurityPage(WikiPage wikiPage)
    {
        WikiName nsspName = new WikiName(wikiPage.getNamespace(), _namespaceSecurityPageName, null);

        return WikiEngine.instance().getWikiPage(nsspName);
    }

}
