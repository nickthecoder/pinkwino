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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.co.nickthecoder.pinkwino.parser.syntax.WikiLineSyntax;
import uk.co.nickthecoder.pinkwino.parser.syntax.WikiSyntax;

/**
 * Holds the knowledge about how wiki markup is parsed
 */

public class SyntaxManager
{

    private List<WikiSyntax> _wikiSyntax;

    private List<WikiLineSyntax> _wikiLineSyntax;

    public SyntaxManager()
    {
        _wikiSyntax = new ArrayList<WikiSyntax>();
        _wikiLineSyntax = new ArrayList<WikiLineSyntax>();

    }

    public Iterator<WikiSyntax> getWikiSyntax()
    {
        return _wikiSyntax.iterator();
    }

    public Iterator<WikiLineSyntax> getWikiLineSyntax()
    {
        return _wikiLineSyntax.iterator();
    }

    public void addWikiSyntax(WikiSyntax wikiSyntax)
    {
        _wikiSyntax.add(wikiSyntax);
    }

    public void addWikiLineSyntax(WikiLineSyntax wikiLineSyntax)
    {
        _wikiLineSyntax.add(wikiLineSyntax);
    }

}
