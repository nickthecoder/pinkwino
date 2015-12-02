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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiPage;

public class PageSecurity
{

    public static final String ATTRIBUTE_NAME = "PAGE_SECURITY";

    protected static Logger _logger = LogManager.getLogger(PageSecurity.class);
    /**
     * The set of user names (String), who are allowed to edit the page.
     */
    private Set<String> _editAllowed;

    /**
     * A list of WikiPages who's security is inherreted.
     */
    private List<WikiPage> _included;

    /**
     * If set, then all users can edit, otherwise the list set of editAllowed is
     * used.
     */
    private boolean _allowAll;

    public static PageSecurity getPageSecurity(WikiDocument wikiDocument)
    {
        PageSecurity pageSecurity = (PageSecurity) wikiDocument.getAttribute(ATTRIBUTE_NAME);
        if (pageSecurity == null) {
            pageSecurity = new PageSecurity();
            wikiDocument.setAttribute(PageSecurity.ATTRIBUTE_NAME, pageSecurity);
        }

        return pageSecurity;
    }

    public PageSecurity()
    {
        _editAllowed = new HashSet<String>();
        _included = new ArrayList<WikiPage>();
        _allowAll = false;
    }

    public void setAllowAll(boolean value)
    {
        _allowAll = value;
    }

    public void addInclude(WikiPage wikiPage)
    {
        _included.add(wikiPage);
    }

    public Iterator<WikiPage> includedPages()
    {
        return _included.iterator();
    }

    public void addEditUser(String userName)
    {
        _editAllowed.add(userName);
    }

    public boolean canEdit(String userName)
    {
        return _allowAll || _editAllowed.contains(userName);
    }

}
