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

package uk.co.nickthecoder.pinkwino.link;

import java.util.Collection;

/**
 * Determines which links are external links, and how to build complete urls.
 * 
 * If the user links to [foo:bar], then "foo" is used as the key to the hash
 * map. If a value is found, let say the value is "http://gohere.com/%1", then
 * the "%1" is replaced by "bar" (as specified in the requested link [foo:bar]).
 * So the final url is : http://gohere.com/bar
 */

public interface ExternalLinkManager
{

    /**
     * Returns the ExternalLinkType for the given prefix, or null if not found.
     */
    public ExternalLinkType getExternalLinkType(String prefix);

    /**
     * If there is no matching prefix, then null is returned, otherwise the
     * resolved url is returned.
     */
    public String resolve(String prefix, String remainder);

    public Collection<ExternalLinkType> getExternalLinkTypes();

}
