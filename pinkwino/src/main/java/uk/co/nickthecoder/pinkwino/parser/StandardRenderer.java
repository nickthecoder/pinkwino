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

package uk.co.nickthecoder.pinkwino.parser;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.webwidgets.util.TagUtil;

/**
 * Converts the wiki syntax into html
 */

public class StandardRenderer implements Renderer
{
    public static void parameter(StringBuffer buffer, String name, String value)
    {
        buffer.append(" ").append(name).append("=\"").append(escapeText(value)).append("\"");
    }

    public static void unescapedParameter(StringBuffer buffer, String name, String value)
    {
        buffer.append(" ").append(name).append("=\"").append(value).append("\"");
    }

    public static String escapeText(String text)
    {
        return TagUtil.safeText(text);
    }

    public StandardRenderer()
    {
    }

    public String render(Version version)
    {
        return version.getWikiDocument().render();
    }

    public WikiDocument parse(Version version)
    {
        Parser parser = new Parser(version, WikiEngine.instance().getSyntaxManager());
        version.setWikiDocument(parser.parse());

        return version.getWikiDocument();
    }

}
