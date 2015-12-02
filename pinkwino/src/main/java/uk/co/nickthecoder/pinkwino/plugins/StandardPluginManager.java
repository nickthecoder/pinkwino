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
import java.util.Iterator;

/**
 * Holds the set of plugins
 */
public class StandardPluginManager implements PluginManager
{

    public static final String JSP_PLUGIN_ROOT = "/wiki/jspPlugins/";

    private HashMap<String,Plugin> _plugins;

    private HashMap<String,VisualPlugin> _visualPlugins;

    private String _jspPluginRoot;

    public StandardPluginManager()
    {
        _plugins = new HashMap<String,Plugin>();
        _visualPlugins = new HashMap<String,VisualPlugin>();
        _jspPluginRoot = JSP_PLUGIN_ROOT;
    }

    public void add(Plugin plugin)
    {
        _plugins.put(plugin.getName(), plugin);

        if (plugin instanceof VisualPlugin) {
            _visualPlugins.put(plugin.getName(), (VisualPlugin) plugin);
        }
    }

    public Plugin getPlugin(String name)
    {
        return (Plugin) _plugins.get(name);
    }

    public VisualPlugin getVisualPlugin(String name)
    {
        return (VisualPlugin) _visualPlugins.get(name);
    }

    /**
     * iterator of all plugins that have been registered.
     */
    public Iterator<Plugin> getPlugins()
    {
        return _plugins.values().iterator();
    }

    /**
     * iterator of all visual plugins that have been registered.
     */
    public Iterator<VisualPlugin> getVisualPlugins()
    {
        return _visualPlugins.values().iterator();
    }

    public String getJspPluginRoot()
    {
        return _jspPluginRoot;
    }

    public void setJspPluginRoot(String value)
    {
        _jspPluginRoot = value;
    }

}
