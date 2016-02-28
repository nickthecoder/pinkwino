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

import java.util.HashMap;
import java.util.Map;

import uk.co.nickthecoder.pinkwino.parser.tree.AbstractNode;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class ReplacementPlugin extends AbstractVisualPlugin
{
    private Map<Character,String> replacements;

    public ReplacementPlugin(String name)
    {
        super(name, BODY_TYPE_TEXT);
        replacements = new HashMap<Character,String>();
    }

    final public ReplacementPlugin replace( String x, String with )
    {
        return replace( x.charAt(0), with );
    }

    final public ReplacementPlugin replace( char x, String with )
    {
        this.replacements.put( x, with );
        return this;
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        return null;
    }

    public void bodyText(PluginSupport pluginSupport, Parameters parameters, String text, Object pluginState)
    {
        pluginSupport.add( new ReplacementsNode( text ) );
    }

    public class ReplacementsNode extends AbstractNode
    {
        public String source;
    
        public ReplacementsNode( String source )
        {
            this.source = source;
        }

        public boolean isBlock()
        {
            return true;
        }

        public void render(StringBuffer buffer)
        {
            for (char c : this.source.toCharArray()) {
                String part = replacements.get( c );
                if (part != null) {
                    buffer.append( part );
                }
            }
        }

    }
}
