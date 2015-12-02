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
import uk.co.nickthecoder.pinkwino.parser.tree.IncludeNode;
import uk.co.nickthecoder.pinkwino.parser.tree.SimpleParentNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * Puts the body in a div, and also notes that this div has been used, by
 * placing an attrbute in the WikiDocument. This was created to allow a side
 * panel to be added to a wiki page, which then turns off adverts which would
 * normally occupy that part of the page. (the adverts need to check for the
 * existance of the WikiDocument parameter).
 * 
 * Consider using the newer plugin, NamedNodePluing, instead of this one.
 */

public class NotedDivPlugin extends AbstractVisualPlugin
{

    private String _cssClass;

    private String _attributeName;

    /**
     * The plugin, the cssClass and the parameter name all share the same value.
     */
    public NotedDivPlugin(String name)
    {
        this(name, name, name);
    }

    public NotedDivPlugin(String name, String attributeName, String cssClass)
    {
        super(name, BODY_TYPE_WIKI);
        _attributeName = attributeName;
        _cssClass = cssClass;

        addParameterDescription(ParameterDescription.find("page"));
        addParameterDescription(new RegexParameterDescription("attribute", ".*").defaultValue("true"));
        addParameterDescription(new RegexParameterDescription("edit", "none|top|bottom", true, "none",
                        ParameterDescription.USAGE_GENERAL));
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {

        SimpleParentNode div = new SimpleParentNode("div", true);
        Parameters divParameters = new Parameters();
        divParameters.addParameter(new Parameter(ParameterDescription.find("class"), _cssClass));
        div.setParameters(divParameters);

        pluginSupport.begin(div);
        pluginSupport.begin(new AutoParagraphBlock());

        WikiEngine.instance().getWikiContext()
                        .setAttribute(_attributeName, parameters.getParameter("attribute").getValue());

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
