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

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.parser.tree.Comment;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Renders a google map, with optional directions from place to place.
 */
public class ConditionalPlugin extends AbstractVisualPlugin
{

    private String _attributeName;

    private Object _comparison;

    private boolean _hide;

    public ConditionalPlugin(String name, String attributeName, Object comparison, boolean hide)
    {
        super(name, BODY_TYPE_WIKI);
        _attributeName = attributeName;
        _comparison = comparison;
        _hide = hide;
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        if (_hide ^ meetsCondition()) {
        } else {
            pluginSupport.begin(new Comment());
        }

        return null;
    }

    public boolean meetsCondition()
    {
        Object attribute = WikiEngine.instance().getAttribute(_attributeName);

        if (attribute == null) {
            return _comparison == null;
        }

        return attribute.equals(_comparison);

    }

}
