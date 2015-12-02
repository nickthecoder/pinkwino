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
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.IncludeNode;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainText;
import uk.co.nickthecoder.pinkwino.parser.tree.SimpleParentNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

public class IncludePlugin extends AbstractVisualPlugin
{

    public IncludePlugin()
    {
        this("include");
    }

    public IncludePlugin(String name)
    {
        super(name, BODY_TYPE_NONE);

        addParameterDescription(ParameterDescription.find("page").required());
        addParameterDescription(new RegexParameterDescription("edit", "none|top|bottom", true, "none",
                        ParameterDescription.USAGE_GENERAL));
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        String editPosition = parameters.getParameter("edit").getValue();

        Parameter pageParameter = parameters.getParameter("page");
        WikiPage wikiPage = WikiEngine.instance().getWikiPage(pageParameter.getValue());
        if (wikiPage == null) {
            return new ErrorText("page not found");
        }

        if (wikiPage.getWikiName().isText()) {

            ParentNode pre = new SimpleParentNode("pre", true);
            pre.add(new PlainText(wikiPage.getCurrentVersion().getContent()));
            pluginSupport.add(pre);

        } else {

            pluginSupport.add(new IncludeNode(wikiPage, editPosition));

        }

        pluginSupport.getWikiDocument().addDependency(wikiPage.getWikiName(), Dependency.DEPENDENCY_INCLUDE);

        return null;
    }

}
