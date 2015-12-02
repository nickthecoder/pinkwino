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

import uk.co.nickthecoder.pinkwino.Dependency;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.AutoParagraphBlock;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.HiddenParent;
import uk.co.nickthecoder.pinkwino.parser.tree.IncludeNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * The body of this plugin is not rendered within the main content, instead, it
 * is added to the WikiDocument as a named Node. The template can choose to
 * render this named Node in a different part of the web page. For example, a
 * wiki page could have navigation items specific to that page (defined withing
 * the wiki page), and the template can place these navigation items above the
 * main content.
 * 
 * To render the named node, the template should include something like this :
 * 
 * <pre>
 *     ${version.wikiDocument.namedContents.myNode}
 * </pre>
 * 
 * Where myNode is the name of the NamedNode.
 * 
 * This class can work in one of two ways, either the name of the named Node can
 * be supplied to the constructor, or it can be supplied as a plugin parameter
 * by the user. I suggest you use the former, as it is easier for the end-users.
 * If you want multiple named Nodes, then create multiple NameNodePlugin
 * instances, each with a different plugin name.
 */

public class NamedNodePlugin extends AbstractVisualPlugin
{

    /**
     * If set in the constructor, then this plugin will always use this name for
     * the named Node. If its not set, then the user can choose the name with
     * the name parameter.
     */
    private String _nodeName;

    public NamedNodePlugin()
    {
        this("extra", null);
    }

    public NamedNodePlugin(String pluginName)
    {
        this(pluginName, pluginName);
    }

    /**
     * If nodeName is null, then the name of the node must be specifed by the
     * end-user as a plugin parameter ("name").
     */
    public NamedNodePlugin(String pluginName, String nodeName)
    {
        super(pluginName, BODY_TYPE_WIKI);
        _nodeName = nodeName;

        addParameterDescription(ParameterDescription.find("page"));
        addParameterDescription(new RegexParameterDescription("edit", "none|top|bottom", true, "none",
                        ParameterDescription.USAGE_GENERAL));

        if (_nodeName == null) {
            addParameterDescription(ParameterDescription.find("name").required());
        }
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        String name = _nodeName == null ? parameters.getParameter("name").getValue() : _nodeName;

        if (name == null) {
            pluginSupport.add(new ErrorText("name=???"));
            return null;
        }

        HiddenParent hiddenParent = new HiddenParent(true /* is block */);
        pluginSupport.begin(hiddenParent);

        AutoParagraphBlock apb = new AutoParagraphBlock();
        pluginSupport.begin(apb);

        pluginSupport.getWikiDocument().addNamedNode(name, apb);

        // Include the named page if one if given
        Parameter pageParameter = parameters.getParameter("page");
        if (pageParameter != null) {

            WikiPage wikiPage = WikiEngine.instance().getWikiPage(pageParameter.getValue());
            if (wikiPage == null) {
                pluginSupport.add(new ErrorText("page not found : " + pageParameter.getValue()));
            } else {
                String editPosition = parameters.getParameter("edit").getValue();
                pluginSupport.add(new IncludeNode(wikiPage, editPosition));
                pluginSupport.getWikiDocument().addDependency(wikiPage.getWikiName(), Dependency.DEPENDENCY_INCLUDE);

            }
        }

        return null;
    }

}
