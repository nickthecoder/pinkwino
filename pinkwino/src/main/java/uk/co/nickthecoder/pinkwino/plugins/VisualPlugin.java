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

import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * The interface for all plugins that can be embedded within a page.
 */

public interface VisualPlugin extends Plugin
{
    public static final int BODY_TYPE_NONE = 0;

    public static final int BODY_TYPE_WIKI = 1;

    public static final int BODY_TYPE_TEXT = 2;

    /**
     * If your plugin needs to be stateful between its doBegin and doEnd, create
     * a new Object with the state, and return it from this method. Whatever is
     * returned will be passed to the doEnd method. Most plugins don't need to
     * be stateful, and therefore return null.
     */
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters);

    /**
     * Called only if getBodyType returned BODY_TYPE_TEXT.
     */
    public void bodyText(PluginSupport pluginSupport, Parameters parameters, String text, Object pluginState);

    /**
     * Called after the body of the plugin has been parsed.
     * 
     * Note, if doBegin began new parent nodes, then these will automatically be
     * ended - you can end them yourself, but it isn't necessary.
     * 
     * Note the pluginState is whatever was returned from the doBegin call. For
     * most plugins this will be null.
     */
    public void doEnd(PluginSupport pluginSupport, Parameters parameters, Object pluginState);

    /**
     * These are used to define what parameters a user is allowed to specify,
     * and therefore limit what can be passed to the doBegin and doEnd methods.
     */
    public ParameterDescriptions getParameterDescriptions();

    /**
     * Returns one of BODY_TYPE_WIKI, BODY_TYPE_NONE, BODY_TYPE_TEXT
     */
    public int getBodyType();

}
