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

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.parser.tree.AutoParagraphBlock;
import uk.co.nickthecoder.pinkwino.parser.tree.DescendantNodeIterator;
import uk.co.nickthecoder.pinkwino.parser.tree.InternalLinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.Link;
import uk.co.nickthecoder.pinkwino.parser.tree.LinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.plugins.AbstractVisualPlugin;
import uk.co.nickthecoder.pinkwino.plugins.PluginSupport;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

public class SecurityPlugin extends AbstractVisualPlugin
{

    public static final String DEFAULT_USER_NAMESPACE_NAME = "user";

    protected static Logger _logger = LogManager.getLogger(SecurityPlugin.class);

    public SecurityPlugin()
    {
        // The name of the plugin
        this("security");
    }

    public SecurityPlugin(String name)
    {
        super(name, BODY_TYPE_WIKI);

        addParameterDescription(new RegexParameterDescription("all", "true|false", true, "false",
                        ParameterDescription.USAGE_GENERAL));
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        AutoParagraphBlock apb = new AutoParagraphBlock();
        pluginSupport.begin(apb);
        // pluginSupport.add( new PlainText( "Begin APB" ) );

        return apb;
    }

    public void doEnd(PluginSupport pluginSupport, Parameters parameters, Object pluginState)
    {
        // pluginSupport.add( new PlainText( "End ? APB" ) );

        AutoParagraphBlock parentNode = (AutoParagraphBlock) pluginState;

        PageSecurity pageSecurity = PageSecurity.getPageSecurity(pluginSupport.getWikiDocument());

        String all = parameters.getParameter("all").getValue();

        if ("true".equals(all)) {
            pageSecurity.setAllowAll(true);
        } else {

            for (Iterator<Node> i = new DescendantNodeIterator(parentNode); i.hasNext();) {
                Node node = i.next();

                if (node instanceof Link) {
                    processLink(pageSecurity, (Link) node);
                }
            }
        }
    }

    protected String getUserNamespaceName()
    {
        Namespace namespace = WikiEngine.instance().getUserNamespace();
        if (namespace == null) {
            return DEFAULT_USER_NAMESPACE_NAME;
        } else {
            return namespace.getName();
        }
    }

    protected void processLink(PageSecurity pageSecurity, Link link)
    {
        LinkDestination dest = link.getDestination();

        String userNamespaceName = getUserNamespaceName();

        if (dest instanceof InternalLinkDestination) {
            WikiName wikiName = ((InternalLinkDestination) dest).getWikiName();

            if (wikiName.getNamespace().getName().equals(userNamespaceName)) {
                pageSecurity.addEditUser(wikiName.getTitle());

            } else {
                pageSecurity.addInclude(wikiName.getWikiPage());
            }

        }
    }

}

