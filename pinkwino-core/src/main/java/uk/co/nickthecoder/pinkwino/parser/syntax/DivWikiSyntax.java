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

import uk.co.nickthecoder.pinkwino.parser.Parser;
import uk.co.nickthecoder.pinkwino.parser.tree.AutoParagraphBlock;
import uk.co.nickthecoder.pinkwino.parser.tree.SimpleParentNode;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;

public class DivWikiSyntax extends AbstractWikiLineSyntax
{

    private String[] _terminators;

    private boolean _allowParagraphs;

    private String _htmlTag;

    private String _cssClass;

    public DivWikiSyntax(String prefix, String suffix, boolean allowParagraphs)
    {
        this(prefix, suffix, allowParagraphs, "div", null);

    }

    /**
     * While the class is called DivWikiSyntax, this constructor lets you choose
     * which html tag to use. This was created so that wiki syntax can create
     * html blocks which can automatically add &lt;p&gt; tags where needed,
     * inside of a html &lt;blockquote&gt; block.
     * 
     * @param prefix
     * @param suffix
     * @param allowParagraphs
     * @param htmlTag
     *            A value of "div" will have the same effect as the other,
     *            simpler constructor.
     * @param cssCladd
     *            The default cssClass to use, or null for no default. The css
     *            class can be overriden by the user using parameters in the
     *            wiki markup.
     */
    public DivWikiSyntax(String prefix, String suffix, boolean allowParagraphs, String htmlTag, String cssClass)
    {
        super(prefix, suffix);

        _terminators = new String[1];
        _terminators[0] = suffix;

        _allowParagraphs = allowParagraphs;
        _htmlTag = htmlTag;
        _cssClass = cssClass;

        addParameterDescription(ParameterDescription.find("class"));
        addParameterDescription(ParameterDescription.find("width"));
        addParameterDescription(ParameterDescription.find("height"));
        addParameterDescription(ParameterDescription.find("float"));
        addParameterDescription(ParameterDescription.find("text-align"));
    }

    public void processSyntax(Parser parser)
    {
        SimpleParentNode parentNode = new SimpleParentNode(_htmlTag, true, _cssClass);
        parser.begin(parentNode);

        parseParameters(parser, parentNode);

        if (_allowParagraphs) {

            AutoParagraphBlock autoParagraphBlock = new AutoParagraphBlock();
            parser.begin(autoParagraphBlock);

            String terminator = parser.parseRemainder(_terminators);
            if (terminator == null) {
                parser.abandon(autoParagraphBlock, getPrefix());
                parser.end(parentNode);
            } else {
                parser.end(autoParagraphBlock);
                parser.end(parentNode);
            }

        } else {

            String terminator = parser.parseRemainder(_terminators);
            if (terminator == null) {
                parser.abandon(parentNode, getPrefix());
            } else {
                parser.end(parentNode);
            }

        }
    }

}
