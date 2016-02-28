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

package uk.co.nickthecoder.pinkwino.plugins;

import java.io.Reader;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;

/**
 * This acts as an intermediate between the plugins and the Parser.
 */
public interface PluginSupport
{

    public ParentNode getCurrentParent();
    
    /**
     * Add a node to the tree of nodes (at the current position).
     */
    public void add(Node node);

    /**
     * Begin a parent node. All subsequent calls to add or begin will cause
     * those nodes to be added as children to the ParentNode given here. It is
     * vital to call end for each begin. If you want to add a ParentNode without
     * ANY children, you can either call add, or you could call begin, and then
     * end immediately afterwards.
     */
    public void begin(ParentNode parent);

    /**
     * Ends the ParentNode, so that subsequent calls to add of begin, will be
     * added as siblings to the ParentNode given here. It is vital to match
     * begin and ends correctly.
     */
    public void end(ParentNode parent);

    public WikiDocument getWikiDocument();

    public Namespace getDefaultNamespace();

    public WikiName parseWikiName(String name);

    public void insertWikiMarkup(Reader wikiMarkup);

}
