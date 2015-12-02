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

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.JspParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Allows any jsp page to be included as part of a wiki page's content.
 */
public class JspPlugin extends AbstractVisualPlugin
{

    private String _jspPath;

    private boolean _isBlock;

    public static String resolvePluginPath(String jspPath)
    {
        if (jspPath.startsWith("/")) {
            return jspPath;
        } else {
            return WikiEngine.instance().getPluginManager().getJspPluginRoot() + jspPath;
        }

    }

    /**
     * Create a JspPlugin with the give plugin name, and the path to the jsp
     * page responsible for rendering the plugin. If jspPath begins with a
     * slash, then that path is used verbatim, otherwise, the jspPlugin root is
     * prefixed to it. This jspPlugin root is obtained from the PluginManager.
     */
    public JspPlugin(String name, String jspPath, int bodyType)
    {
        super(name, bodyType);

        _jspPath = resolvePluginPath(jspPath);

        _isBlock = true;
    }

    /**
     * Creates a node, and add the following request parameters ; # wikiEngine -
     * the instance of WikiEngine. # wikiDocument - the WikiDocument object #
     * parameters - a hashmap of the Parameter objects keyed on their name. #
     * jspNode - the JspNode, or JspParentNode.
     */
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        JspNode jspNode;

        if (isParent()) {
            jspNode = new JspParentNode(this, parameters, _jspPath, isBlock());
        } else {
            jspNode = new JspNode(this, parameters, _jspPath, isBlock());
        }

        Node node = decorateJspNode(jspNode, pluginSupport.getWikiDocument(), parameters);

        if (node != null) {
            if (node instanceof ParentNode) {
                pluginSupport.begin((ParentNode) node);
            } else {
                pluginSupport.add(node);
            }
        }

        return node;
    }

    public void includeHead(PluginSupport pluginSupport, Parameters parameters, String jspPath)
    {
        JspNode jspNode = new JspNode(this, parameters, resolvePluginPath(jspPath), false);
        Node node = decorateJspNode(jspNode, pluginSupport.getWikiDocument(), parameters);

        if (node != null) {
            ParentNode parent = pluginSupport.getWikiDocument().getNamedNode("head");
            parent.add(node);
        }
    }

    public void bodyText(PluginSupport pluginSupport, Parameters parameters, String text, Object pluginState)
    {
        if (pluginState instanceof JspNode) {
            ((JspNode) pluginState).setBody(text);
        }
    }

    /**
     * Called by createJspNode, it adds various request parameters to the node.
     * This method was created, as it may be useful for sub-classes to override
     * it, adding their own decorations. (The sub-class could just do it when
     * they create the node in createJspNode though).
     * 
     * Adds the following as request parameters :
     * 
     * <pre>
     *       wikiEngine - the WikiEngine.instance()
     *       parameters - a hashmap of key value pairs of parameters given to the plugin
     *       jspNode    - the JspNode object
     * </pre>
     */
    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        jspNode.setRequestAttribute("wikiPage", WikiContext.getWikiContext().getWikiPage());
        jspNode.setRequestAttribute("wikiDocument", wikiDocument);
        jspNode.setRequestAttribute("wikiEngine", WikiEngine.instance());
        jspNode.setRequestAttribute("parameters", parameters.getHashMap());
        jspNode.setRequestAttribute("jspNode", jspNode);

        return jspNode;
    }

    /**
     * Determines whether the plugin is capable of having content *within*
     * itself. If true, then a JspParentNode is created, otherwise a JspNode is
     * created. If this is a parent, then the jsp view should typically render
     * the child nodes. To do that, use the following jsp :
     * 
     * <pre>
     *       ${jspNode.renderChildren}
     * </pre>
     */
    public boolean isParent()
    {
        return getBodyType() == BODY_TYPE_WIKI;
    }

    /**
     * Determines if this plugin will render block html elements (such as div,
     * table, p, h1 etc. ) The default value is true. set to false if your
     * plugin will only render non-block elements such as i, b, a and span tags.
     */
    public boolean isBlock()
    {
        return _isBlock;
    }

    public void setBlock(boolean value)
    {
        _isBlock = value;
    }

    public void preRender(JspNode node)
    {
    }

    public void postRender(JspNode node)
    {
    }

}
