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

package uk.co.nickthecoder.pinkwino.storage;

import java.io.File;

import uk.co.nickthecoder.pinkwino.UserMessageException;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * A storage wrapper which checks with the authentification and authorisation
 * managers before making any changes to a wiki page. If you don't use this
 * wrapper, then your wiki can be edited by anybody, without signing in.
 */

public class SecureStorage extends WrappedStorage
{

    public SecureStorage(WikiStorage storage)
    {
        super(storage);
    }

    public void save(WikiPage wikiPage, String markup, File mediaFile)
    {
        if (wikiPage.canEdit()) {

            super.save(wikiPage, markup, mediaFile);

        } else {
            throw new UserMessageException("You are not authorised to edit this page.");
        }

    }

    public void delete(WikiPage wikiPage)
    {
        if (wikiPage.canEdit()) {

            super.delete(wikiPage);

        } else {
            throw new UserMessageException("You are not authorised to delete this page.");
        }
    }

    public String toString()
    {
        return super.toString() + " (secure)";
    }

}
