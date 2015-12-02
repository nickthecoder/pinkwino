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

import javax.servlet.http.HttpServletRequest;

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Common behaviour for all/most Visual Plugins
 */

public abstract class AbstractVisualPlugin implements VisualPlugin
{

    private String _name;

    private ParameterDescriptions _parameterDescriptions;

    private int _bodyType;

    public AbstractVisualPlugin(String name, int bodyType)
    {
        super();
        _name = name;
        _bodyType = bodyType;
        _parameterDescriptions = new ParameterDescriptions();
    }

    public String getName()
    {
        return _name;
    }

    public ParameterDescriptions getParameterDescriptions()
    {
        return _parameterDescriptions;
    }

    public void addParameterDescription(ParameterDescription ad)
    {
        _parameterDescriptions.addParameterDescription(ad);
    }

    public abstract Object doBegin(PluginSupport pluginSupport, Parameters parameters);

    public void bodyText(PluginSupport pluginSupport, Parameters parameters, String text, Object pluginState)
    {
        // Do nothing
    }

    public void doEnd(PluginSupport pluginSupport, Parameters parameters, Object pluginState)
    {
        // Do nothing.
    }

    public int getBodyType()
    {
        return _bodyType;
    }

    public int compareTo(Plugin other)
    {
        return this.getName().compareTo(other.getName());
    }

    public boolean isPost()
    {
        try {

            return getRequest().getMethod().equals("POST");
        } catch (Exception e) {
            return false;
        }
    }

    public HttpServletRequest getRequest()
    {
        return WikiContext.getWikiContext().getServletRequest();
    }

    /**
     * Returns the current url
     */
    public String getUrl()
    {
        HttpServletRequest request = getRequest();
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append("?").append(request.getQueryString());
        }

        return url.toString();
    }

}

