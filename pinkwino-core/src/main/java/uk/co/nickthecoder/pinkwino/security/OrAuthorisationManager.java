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

import uk.co.nickthecoder.pinkwino.WikiPage;

public class OrAuthorisationManager implements AuthorisationManager
{
    private List<AuthorisationManager> _authorisationManagers;

    public OrAuthorisationManager()
    {
        _authorisationManagers = new ArrayList<AuthorisationManager>();
    }

    public OrAuthorisationManager(AuthorisationManager a, AuthorisationManager b)
    {
        this();

        _authorisationManagers.add(a);
        _authorisationManagers.add(b);
    }

    public void addAuthorisationManager(AuthorisationManager am)
    {
        _authorisationManagers.add(am);
    }
    public boolean canEdit(User user, WikiPage wikiPage)
    {
        for (Iterator<AuthorisationManager> i = _authorisationManagers.iterator(); i.hasNext();) {
            AuthorisationManager am = i.next();

            if (am.canEdit(user, wikiPage)) {
                return true;
            }
        }

        return false;
    }

    public boolean canDelete(User user, WikiPage wikiPage)
    {
        for (Iterator<AuthorisationManager> i = _authorisationManagers.iterator(); i.hasNext();) {
            AuthorisationManager am = (AuthorisationManager) i.next();

            if (am.canDelete(user, wikiPage)) {
                return true;
            }
        }

        return false;
    }
    // }}}

    // -------------------- [[Test / Debug]] --------------------

}
