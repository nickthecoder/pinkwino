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

package uk.co.nickthecoder.pinkwino.storage;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiPage;

public class RegexSaveFilter implements SaveFilter
{

    protected static Logger _logger = LogManager.getLogger(RegexSaveFilter.class);

    private Pattern _pattern;
    private String _message;

    public RegexSaveFilter(String regex, String message)
    {
        _message = message;
        _pattern = Pattern.compile(regex);
    }

    public String filter(WikiPage wikiPage, String markup, File mediaFile)
    {
        Matcher matcher = _pattern.matcher(markup);

        if (matcher.matches()) {
            return _message;
        }

        // Doesn't match, therefore it passes this filter, return no error
        // message.
        return null;

    }

}
