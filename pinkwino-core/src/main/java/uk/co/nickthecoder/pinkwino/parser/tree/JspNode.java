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

package uk.co.nickthecoder.pinkwino.parser.tree;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.plugins.JspPlugin;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.webwidgets.util.BufferedHttpServletResponse;

/**
 * Allows an arbitary jsp page to be part of a wiki. This is used by
 * VisualPlugins, such as the PluginsPlugin.
 */

public class JspNode implements Node
{

    private String _jspPath;

    private HashMap<String, Object> _requestAttributes;

    private boolean _isBlock;

    private String _body;

    private JspPlugin _jspPlugin;

    private Parameters _parameters;

    public JspNode(JspPlugin plugin, Parameters parameters, String jspPath, boolean isBlock)
    {
        super();
        _jspPlugin = plugin;
        _parameters = parameters;
        _jspPath = jspPath;
        _requestAttributes = new HashMap<String,Object>();
        _isBlock = isBlock;
        _body = "";
    }

    public Parameters getParameters()
    {
        return _parameters;
    }

    public JspPlugin getJspPlugin()
    {
        return _jspPlugin;
    }

    /**
     * As we are redirecting to a jsp page, there is probably at least one block
     * element.
     */
    public boolean isBlock()
    {
        return _isBlock;
    }

    public void render(StringBuffer buffer)
    {
        _jspPlugin.preRender(this);

        WikiContext wikiContext = WikiEngine.instance().getWikiContext();

        HttpServletRequest request = (HttpServletRequest) wikiContext.getServletRequest();
        HttpServletResponse response = (HttpServletResponse) wikiContext.getServletResponse();
        ;

        RequestDispatcher dispatcher = request.getRequestDispatcher(getJspPath());
        BufferedHttpServletResponse bufferedResponse = new BufferedHttpServletResponse(response);

        if (dispatcher == null) {

            // MORE append error message

        } else {

            HashMap<String,Object> previousValues = new HashMap<String,Object>();
            try {

                for (Iterator<String> i = _requestAttributes.keySet().iterator(); i.hasNext();) {
                    String key = i.next();
                    Object value = _requestAttributes.get(key);
                    previousValues.put(key, request.getAttribute(key));
                    request.setAttribute(key, value);
                }

                dispatcher.include(request, bufferedResponse);
                buffer.append(bufferedResponse.getBuffer());

            } catch (Exception e) {

                new ExceptionNode(e, false).render(buffer);

            }

            // Put back the old request attributes.
            for (Iterator<String> i = previousValues.keySet().iterator(); i.hasNext();) {
                String key = i.next();
                Object oldValue = (previousValues.get(key));
                request.setAttribute(key, oldValue);
            }

        }

        _jspPlugin.postRender(this);
    }

    public void setRequestAttribute(String name, Object value)
    {
        _requestAttributes.put(name, value);
    }

    public String getJspPath()
    {
        return _jspPath;
    }

    public void text(StringBuffer buffer)
    {
        // nothing
    }

    public void setBody(String body)
    {
        _body = body;
    }

    public String getBody()
    {
        return _body;
    }

}
