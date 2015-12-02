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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * Lists all plugins
 */
public class RecentChangesPlugin extends JspPlugin
{

    public static String DEFAULT_DAYS = "1";

    public RecentChangesPlugin()
    {
        this("recentChanges", "recentChanges.jsp");
    }

    public RecentChangesPlugin(String name, String jspPath)
    {
        super(name, jspPath, BODY_TYPE_NONE);

        addParameterDescription(new RegexParameterDescription("days", "[0-9]+").defaultValue(DEFAULT_DAYS));
        addParameterDescription(ParameterDescription.find("namespace"));
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        Parameter daysParameter = parameters.getParameter("days");
        Parameter namespace = parameters.getParameter("namespace");

        int days = Integer.parseInt(daysParameter.getValue());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -days);

        Date since = calendar.getTime();

        List<WikiPage> pages;

        if (namespace == null) {
            pages = WikiEngine.instance().getRecentChanges(since);
        } else {
            pages = WikiEngine.instance().getNamespace(namespace.getValue()).getRecentChanges(since);
        }

        jspNode.setRequestAttribute("wikiPages", pages);
        jspNode.setRequestAttribute("since", since);
        jspNode.setRequestAttribute("days", new Integer(days));

        return super.decorateJspNode(jspNode, wikiDocument, parameters);

    }

}
