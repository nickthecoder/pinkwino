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

import java.util.Iterator;
import java.util.List;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Lists all plugins
 */
public class IndexPlugin extends JspPlugin
{

    public IndexPlugin()
    {
        this("index", "indexPlugin.jsp");
    }

    public IndexPlugin(String name, String jspPath)
    {
        super(name, jspPath, BODY_TYPE_NONE);

        addParameterDescription(ParameterDescription.find("namespace").defaultValue(
                        WikiEngine.instance().getDefaultNamespace().getName()));
        addParameterDescription(ParameterDescription.find("ignoreMedia"));

    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        Parameter namespaceParameter = parameters.getParameter("namespace");
        Parameter ignoreMediaParameter = parameters.getParameter("ignoreMedia");

        Namespace namespace = WikiEngine.instance().getNamespace(namespaceParameter.getValue());
        boolean ignoreMedia = ignoreMediaParameter.getValue().equals("true");

        if (namespace == null) {

            return new ErrorText("namespace not found");

        } else {

            List<WikiPage> pages = namespace.getPages();
            if (ignoreMedia) {
                for (Iterator<WikiPage> i = pages.iterator(); i.hasNext();) {
                    WikiPage page = i.next();
                    if (page.getWikiName().isMedia()) {
                        i.remove();
                    }
                }
            }

            jspNode.setRequestAttribute("wikiPages", pages);
            jspNode.setRequestAttribute("namespace", namespace);

            return super.decorateJspNode(jspNode, wikiDocument, parameters);
        }
    }

}
