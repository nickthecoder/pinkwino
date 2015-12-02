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

package uk.co.nickthecoder.pinkwino.parser.tree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiConstants;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;

/**
 * A link to a wiki page
 */

public class InternalLinkDestination implements LinkDestination
{

    protected static Logger _logger = LogManager.getLogger(InternalLinkDestination.class);

    private WikiName _wikiName;

    public InternalLinkDestination(Namespace namespace, String pageName)
    {
        _wikiName = WikiEngine.instance().getWikiNameFormat().parse(namespace, pageName);
    }

    public InternalLinkDestination(String pageName)
    {
        _wikiName = WikiEngine.instance().getWikiNameFormat().parse(pageName);
    }

    public InternalLinkDestination(WikiName wikiName)
    {
        _wikiName = wikiName;
    }

    public String getHref()
    {
        return _wikiName.getViewUrl();
    }

    public String getText()
    {
        return _wikiName.getTitle();
    }

    public String getCssClass()
    {
        if (_wikiName.getExists()) {
            return null;
        }

        return WikiConstants.CSS_NOT_FOUND;
    }

    public WikiName getWikiName()
    {
        return _wikiName;
    }

}
