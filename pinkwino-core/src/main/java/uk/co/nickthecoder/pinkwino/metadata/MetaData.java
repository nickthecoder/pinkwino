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

package uk.co.nickthecoder.pinkwino.metadata;

import uk.co.nickthecoder.pinkwino.WikiName;

/**
 * Holds meta data about the wiki, such as dependancies between pages
 */

public interface MetaData
{

    /**
     * Returns all wiki pages which link to, or otherwise depend on the given
     * wiki page. This will include pages that simply link to the page, or pages
     * that include the page, or pages that use the image (or other media) held
     * by the given wiki page.
     */
    public SearchResults getBackLinks(WikiName wikiName) throws Exception;

    public SearchResults getDependents(WikiName wikiName) throws Exception;

    /**
     * Searches for the given text within all of the wiki pages' contents
     */
    public SearchResults search(String searchString) throws Exception;

}

