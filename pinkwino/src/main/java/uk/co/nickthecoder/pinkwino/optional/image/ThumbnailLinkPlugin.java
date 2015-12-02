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

package uk.co.nickthecoder.pinkwino.optional.image;

import uk.co.nickthecoder.pinkwino.Dependency;
import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.ExternalLinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.ImageNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Link;
import uk.co.nickthecoder.pinkwino.plugins.AbstractVisualPlugin;
import uk.co.nickthecoder.pinkwino.plugins.PluginSupport;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class ThumbnailLinkPlugin extends AbstractVisualPlugin
{

    public static final String A_STYLE_CLASS = "wiki_thumbnail";

    private String _relation;

    public ThumbnailLinkPlugin()
    {
        this("thumbnailLink", "thumbnail");
    }

    public ThumbnailLinkPlugin(String name, String relation)
    {
        super(name, BODY_TYPE_NONE);

        _relation = relation;

        addParameterDescription(ParameterDescription.find("url").required());
        addParameterDescription(ParameterDescription.find("log"));
        addParameterDescription(ParameterDescription.find("page"));
        addParameterDescription(ParameterDescription.find("width"));
        addParameterDescription(ParameterDescription.find("height"));
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {

        Parameter pageParameter = parameters.getParameter("page");
        WikiPage wikiPage = getWikiPage(parameters.getParameter("url"), pageParameter);

        if ((!wikiPage.getExists()) && wikiPage.canEdit()) {
            createWikiPage(wikiPage, parameters);
        }

        Link link = new Link();
        ExternalLinkDestination eld = new ExternalLinkDestination(parameters.getParameter("url").getValue(),
                        A_STYLE_CLASS);
        link.setDestination(eld);

        WikiPage relatedPage = _relation == null ? wikiPage : wikiPage.getRelatedPage(_relation);

        ImageNode imageNode = new ImageNode(relatedPage.getMediaUrl(), wikiPage.getWikiName().getTitle());
        link.add(imageNode);

        pluginSupport.add(link);
        pluginSupport.getWikiDocument().addDependency(wikiPage.getWikiName(), Dependency.DEPENDENCY_ATTACH);

        return null;
    }

    public WikiPage getWikiPage(Parameter urlParameter, Parameter pageParameter)
    {
        String pageName;
        if (pageParameter == null) {
            pageName = urlParameter.getValue();
            pageName = pageName.replaceAll("[^/]*/", "");
            pageName = pageName.replaceAll("\\?.*", "");
            pageName = pageName + ".png";
        } else {
            pageName = pageParameter.getValue();
        }

        return WikiEngine.instance().getWikiPage(pageName);
    }

    public void createWikiPage(WikiPage wikiPage, Parameters parameters)
    {
        String content = "Created by ThumbnailLinkPlugin\n" + "{{captureWebPage(" + " page=\""
                        + wikiPage.getWikiName().getFormatted() + "\"" + repeatParameter("url", parameters)
                        + repeatParameter("width", parameters) + repeatParameter("height", parameters)
                        + repeatParameter("log", parameters) + ") }}\n";

        Version version = wikiPage.getCurrentVersion();
        version.setContent(content);
        version.render(); // causes the plugin to be run, which captures the web
                          // image.

        WikiEngine.instance().save(wikiPage, content, null);
    }

    public String repeatParameter(String name, Parameters parameters)
    {
        Parameter parameter = parameters.getParameter(name);
        if (parameter == null) {
            return "";
        }
        return " " + name + "=\"" + parameter.getValue() + "\"";
    }

}
