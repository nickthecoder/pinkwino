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
import java.util.LinkedList;
import java.util.List;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Shows thumbnails of all images in a namespace
 */

public class ImageIndexPlugin extends JspPlugin
{

    private static String _thumbnailRelation;

    public ImageIndexPlugin(String thumbailRelation)
    {
        this("imageIndex", "imageIndex.jsp", thumbailRelation);
    }

    public ImageIndexPlugin(String name, String jspPath, String thumbailRelation)
    {
        super(name, jspPath, BODY_TYPE_NONE);

        _thumbnailRelation = thumbailRelation;

        addParameterDescription(ParameterDescription.find("namespace").defaultValue(
                        WikiEngine.instance().getDefaultNamespace().getName()));

    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        Parameter namespaceParameter = parameters.getParameter("namespace");

        WikiEngine wikiEngine = WikiEngine.instance();
        Namespace namespace = wikiEngine.getNamespace(namespaceParameter.getValue());

        if (namespace == null) {

            return new ErrorText("namespace not found");

        } else {

            List<WikiPage> pages = namespace.getPages();
            List<WikiPage> thumbnailPages = new LinkedList<WikiPage>();

            for (Iterator<WikiPage> i = pages.iterator(); i.hasNext();) {
                WikiPage page = i.next();
                WikiName wikiName = page.getWikiName();

                if ((wikiName.isImage()) && (wikiName.getRelation() == null)) {
                    // We have a non-related page with an image, add the
                    // thumbnail page to the
                    // list.
                    WikiName thumbnailName = wikiName.getRelatedName(_thumbnailRelation);
                    WikiPage thumbnailPage = wikiEngine.getWikiPage(thumbnailName);
                    thumbnailPages.add(thumbnailPage);
                }
            }

            jspNode.setRequestAttribute("thumbnailPages", thumbnailPages);
            jspNode.setRequestAttribute("namespace", namespace);
            jspNode.setRequestAttribute("imageIndex_url", wikiDocument.getWikiPage().getViewUrl());

            return super.decorateJspNode(jspNode, wikiDocument, parameters);
        }
    }

}
