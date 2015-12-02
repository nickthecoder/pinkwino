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

package uk.co.nickthecoder.pinkwino;

// {{{ imports
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainParentNode;

// }}}

/**
 * When a Version of a WikiPage has been parsed, the WikiDocument holds the
 * parsed data. Rendering wiki pages is a two step operation, first we parse it,
 * and then we convert the parsed data into html. WikiDocument holds this
 * intermediate stage using a tree of Node objects. Each Node holds information
 * about one piece of the wiki markup.
 * 
 * In addition to the tree of Nodes, a WikiDocument also has a set of
 * attributes. These can be used in a similar way to attributes of a
 * ServletRequest object. i.e. they are name value pairs that can hold anthing
 * you want. Plugins can pass information to the jsp template using these
 * attributes.
 * 
 * Most wiki pages are rendered to a single piece of HTML. This html is the
 * result of rendering the _rootNode. However, a wiki document can also have
 * additional nodes, which can be referenced by name. For example, imagine you
 * have a web page with the main content on the right, and a thin navigation on
 * the left. If you want the left hand side of the web page to be dependant upon
 * which wiki page you are looking at, then this part of the page can be
 * rendered separately. The main content, and the left hand side are both
 * contained in one wiki page - the left hand side typically being included from
 * another wiki page.
 */

public class WikiDocument extends Section
{
    protected static Logger _logger = LogManager.getLogger(WikiDocument.class);

    private Version _version;

    private Node _rootNode;

    private Map<String,Object> _attributes;

    private Map<String, Node> _namedNodes;

    private Map<String,String> _namedContents;

    private Set<Dependency> _dependencies;

    public WikiDocument(Version version, Node rootNode)
    {
        super(0, version.getWikiPage().getWikiName().getFormatted());
        _version = version;
        _rootNode = rootNode;
        _attributes = new HashMap<String,Object>();
        _namedNodes = new HashMap<String,Node>();
        _dependencies = new TreeSet<Dependency>();
    }

    public Version getVersion()
    {
        return _version;
    }

    public WikiDocument getWikiDocument()
    {
        return this;
    }

    public WikiPage getWikiPage()
    {
        return _version.getWikiPage();
    }

    public String render()
    {
        StringBuffer buffer = new StringBuffer();
        _rootNode.render(buffer);

        return buffer.toString();
    }

    public String render(String nodeName)
    {
        Node node = _namedNodes.get(nodeName);

        if (node == null) {
            return "";
        } else {
            StringBuffer buffer = new StringBuffer();
            node.render(buffer);
            return buffer.toString();
        }

    }

    public void addNamedNode(String name, Node node)
    {
        _namedNodes.put(name, node);
    }

    public Map<String,Node> getNamedNodes()
    {
        return _namedNodes;
    }

    public Node getRootNode()
    {
        return _rootNode;
    }

    public Map<String,String> getNamedContents()
    {
        if (_namedContents == null) {
            _namedContents = new HashMap<String,String>();

            if (_namedNodes.size() == 0) {
                return _namedContents;
            } else {
                for (Iterator<String> i = _namedNodes.keySet().iterator(); i.hasNext();) {
                    String name = i.next();
                    String rendered = render(name);

                    _namedContents.put(name, rendered);
                }
            }

        }

        return _namedContents;
    }

    /**
     * If the named node does not exist, then it is created. This is designed to
     * allow plugins to add to specific nodes, in particular, the node named
     * "head", to allow javascript to be appended to the document.
     **/
    public ParentNode getNamedNode(String name)
    {
        ParentNode node = (ParentNode) _namedNodes.get(name);
        if (node == null) {
            node = new PlainParentNode(false);
            _namedNodes.put(name, node);
        }
        return node;
    }

    public void setAttribute(String name, Object value)
    {
        _attributes.put(name, value);
    }

    public Object getAttribute(String name)
    {
        return _attributes.get(name);
    }

    public Map<String,Object> getAttributes()
    {
        return _attributes;
    }

    /**
     * An iteration of WikiName objects that are linked to directly from this
     * document.
     */
    public Iterator<WikiName> getLinks()
    {
        return new FilteredDependencyIterator(_dependencies.iterator(), Dependency.DEPENDENCY_INTERNAL_LINK);
    }

    /**
     * An iteration of WikiName objects that are used as attachments withing
     * this document. So far, only images can be attached to wiki documents, but
     * later perhaps other forms of media will also be attached.
     */
    public Iterator<WikiName> getAttachments()
    {
        return new FilteredDependencyIterator(_dependencies.iterator(), Dependency.DEPENDENCY_ATTACH);
    }

    /**
     * An iteration of WikiName objects that are included by this document.
     */
    public Iterator<WikiName> getIncludes()
    {
        return new FilteredDependencyIterator(_dependencies.iterator(), Dependency.DEPENDENCY_INCLUDE);
    }

    /**
     * A set of Dependency objects which are required to render this page. Note,
     * this is only a level 1 list, so if A depends on B and B depends on C,
     * then C will NOT be in the list of A's dependencies.
     */
    public Collection<Dependency> getDependencies()
    {
        return _dependencies;
    }

    public void addDependency(WikiName wikiName, int type)
    {
        addDependency(new Dependency(wikiName, type));
    }

    public void addDependency(Dependency dependency)
    {
        _dependencies.add(dependency);

        try {
            @SuppressWarnings("unchecked")
            Set<Dependency> dependencies = (Set<Dependency>) WikiContext.getWikiContext().getServletRequest().getAttribute("wiki_dependencies");
            if (dependencies == null) {
                dependencies = new TreeSet<Dependency>();
                WikiContext.getWikiContext().getServletRequest().setAttribute("wiki_dependencies", dependencies);
            }
            dependencies.add(dependency);

        } catch (Exception e) {
            // This method is sometimes run AFTER the request has finished
            // i.e. HttpServletRequest.getAttribute is not valid, in this case,
            // we should
            // quietly ignore the error.
            // This happens if a request spawns a new thread, such as the
            // CaptureWebPagePlugin.
        }

    }
}
