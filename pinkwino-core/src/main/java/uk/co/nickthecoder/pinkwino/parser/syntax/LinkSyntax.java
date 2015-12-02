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

package uk.co.nickthecoder.pinkwino.parser.syntax;

import uk.co.nickthecoder.pinkwino.Dependency;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.link.ExternalLinkType;
import uk.co.nickthecoder.pinkwino.parser.Parser;
import uk.co.nickthecoder.pinkwino.parser.RemainderResult;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.ExternalLinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.InternalLinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.Link;
import uk.co.nickthecoder.pinkwino.parser.tree.LinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainText;

/**
 * Defines the wiki syntax for links
 */

public class LinkSyntax implements WikiSyntax
{

    private static String LINK_PREFIX = "[";

    private static String LINK_SEPARATOR = "|";

    private static String LINK_SUFFIX = "]";

    private static String[] LINK_TERMINATORS_1 = { LINK_SEPARATOR, LINK_SUFFIX };

    private static String[] LINK_TERMINATORS_2 = { LINK_SUFFIX };

    public LinkSyntax()
    {
    }

    public String getPrefix()
    {
        return LINK_PREFIX;
    }

    public void processSyntax(Parser parser)
    {
        Link link = new Link();
        parser.begin(link);
        String terminator = parser.parseRemainder(LINK_TERMINATORS_1);

        if (terminator == null) {

            // A badly formed link, abandon now.
            parser.abandon(link, LINK_PREFIX);

        } else if (terminator.equals(LINK_SEPARATOR)) {

            // We have the form [label|destination]. We've just parsed the
            // label, now
            // we need to get the destination.
            RemainderResult remainderResult = parser.getRemainder(LINK_TERMINATORS_2, false);
            if (LINK_SUFFIX.equals(remainderResult.getTerminator())) {

                link.setDestination(createLinkDestination(parser, remainderResult.getText()));

                if (link.getDestination() instanceof InternalLinkDestination) {
                    parser.getWikiDocument().addDependency(
                                    ((InternalLinkDestination) link.getDestination()).getWikiName(),
                                    Dependency.DEPENDENCY_INTERNAL_LINK);
                }

                parser.end(link);

            } else {

                // We've got half way though, but then found a syntax error (not
                // terminated).
                parser.abandon(link, LINK_PREFIX);
                parser.add(new ErrorText("(link) " + LINK_SEPARATOR));
                if (remainderResult.getText() != null) {
                    parser.add(new PlainText(remainderResult.getText()));
                }

            }

        } else if (terminator.equals(LINK_SUFFIX)) {

            // We have a link in the form [foo] (i.e. no pipe)
            String text = link.getText();
            link.setDestination(createLinkDestination(parser, text));

            // If its an internal link, with an explicit namespace, its nicer
            // not to
            // see the namespace, so lets get rid of it.
            link.clear();
            link.add(new PlainText(link.getDestination().getText()));

            if (link.getDestination() instanceof InternalLinkDestination) {
                parser.getWikiDocument().addDependency(((InternalLinkDestination) link.getDestination()).getWikiName(),
                                Dependency.DEPENDENCY_INTERNAL_LINK);
            }

            parser.end(link);

        } else {
            throw new RuntimeException("LinkSyntax has a bug!");
        }

    }

    protected LinkDestination createLinkDestination(Parser parser, String string)
    {
        int colonIndex = string.indexOf(":");
        if (colonIndex >= 0) {
            String prefix = string.substring(0, colonIndex);
            String remainder = string.substring(colonIndex + 1);

            ExternalLinkType elt = WikiEngine.instance().getExternalLinkManager().getExternalLinkType(prefix);

            if (elt != null) {
                return new ExternalLinkDestination(elt.resolve(remainder), elt.getCssClass());
            }

        }

        // Ok, so its an internal link.

        if ((parser.getVersion() != null) && (parser.getVersion().getWikiPage() != null)) {

            // Use the current page's namespace if one isn't specified
            return new InternalLinkDestination(parser.parseWikiName(string));

        } else {

            return new InternalLinkDestination(string);
        }

    }

}
