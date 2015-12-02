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

import java.util.HashMap;
import java.util.Iterator;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.StandardRenderer;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class TemplateNode extends AbstractNode
{

    private WikiPage _wikiPage;

    private HashMap<String,Node> _wikiNodes;

    public TemplateNode(WikiPage wikiPage, Parameters parameters)
    {
        super();
        _wikiPage = wikiPage;
        setParameters(parameters);
        _wikiNodes = new HashMap<String,Node>();
    }

    public void render(StringBuffer buffer)
    {
        String templateText = _wikiPage.getCurrentVersion().getContent();

        // Add the template parameters that were passed a parameters to the
        // 'template' or 'tiles' plugin
        for (Iterator<Parameter> i = getParameters().iterator(); i.hasNext();) {
            Parameter parameter = i.next();
            String varTagRegex = "\\$\\{" + parameter.getName() + "\\}";

            // Note, need to escape the text, to prevent arbitary html being
            // passed as a parameter value.
            templateText = templateText.replaceAll(varTagRegex, StandardRenderer.escapeText(parameter.getValue()));
        }

        // Now add the template parameters that were passed as wiki nodes using
        // the 'tile' plugin.
        for (Iterator<String> i = _wikiNodes.keySet().iterator(); i.hasNext();) {
            String name = i.next();
            Node node = (Node) _wikiNodes.get(name);
            StringBuffer html = new StringBuffer();
            node.render(html);

            String varTagRegex = "\\$\\{" + name + "\\}";

            templateText = templateText.replaceAll(varTagRegex, html.toString());
        }

        WikiName wikiName = new WikiName(_wikiPage.getNamespace(), "TemplateNode fake page");
        WikiPage wikiPage = new WikiPage(wikiName);
        Version version = new Version(wikiPage);
        version.setContent(templateText);

        buffer.append(WikiEngine.instance().getRenderer().render(version));
    }

    public boolean isBlock()
    {
        return true;
    }

    public void addNode(String name, Node node)
    {
        _wikiNodes.put(name, node);
    }

}
