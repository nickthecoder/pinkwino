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

import uk.co.nickthecoder.pinkwino.parser.tree.IFrameNode;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * A plugin that creates an iframe
 */

public class IFramePlugin extends AbstractVisualPlugin
{

    private String _url;

    private int _width;

    private int _height;

    public IFramePlugin(String name, String url, int width, int height)
    {
        super(name, BODY_TYPE_NONE);
        _url = url;
        _width = width;
        _height = height;
    }

    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {

        IFrameNode iFrame = new IFrameNode(_url, _width, _height);

        pluginSupport.add(iFrame);

        return null;
    }

}
