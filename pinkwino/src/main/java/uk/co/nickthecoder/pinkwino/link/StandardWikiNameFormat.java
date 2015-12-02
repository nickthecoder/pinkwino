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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;

/**
 * Possible name syntax :
 * 
 * title namespace/title namespace/title{relation} title{relation}
 * 
 * This implies that namespaces cannot conatin a "/", titles cannot contain a
 * "{".
 */

public class StandardWikiNameFormat implements WikiNameFormat
{

    public static final String NAMESPACE_SEP = "/";

    public static final String RELATION_SEP = "{";

    public static final String RELATION_TERM = "}";

    protected static Logger _logger = LogManager.getLogger(StandardWikiNameFormat.class);

    public StandardWikiNameFormat()
    {
    }

    public WikiName parse(String path)
    {
        return parse(WikiEngine.instance().getDefaultNamespace(), path);
    }

    public WikiName parse(Namespace namespace, String path)
    {
        String relation = null;

        int slash = path.indexOf(NAMESPACE_SEP);
        if (slash > 0) {
            namespace = WikiEngine.instance().getNamespace(path.substring(0, slash));
            path = path.substring(slash + NAMESPACE_SEP.length());
        }

        int open = path.indexOf(RELATION_SEP);
        if (open > 0) {
            int close = path.lastIndexOf(RELATION_TERM);
            if (close > open) {
                relation = path.substring(open + RELATION_SEP.length(), close);
                path = path.substring(0, open);
            }
        }

        return new WikiName(namespace, path, relation);
    }

    public String format(WikiName wikiName)
    {
        String path;

        path = wikiName.getTitle();

        if (wikiName.getNamespace() != WikiEngine.instance().getDefaultNamespace()) {
            path = wikiName.getNamespace().getName() + "/" + path;
        }

        if (wikiName.getRelation() != null) {
            path = path + RELATION_SEP + wikiName.getRelation() + RELATION_TERM;
        }

        return path;
    }

}
