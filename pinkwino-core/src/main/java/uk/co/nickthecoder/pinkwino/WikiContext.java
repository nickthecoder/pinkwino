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

package uk.co.nickthecoder.pinkwino;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.security.User;

public class WikiContext
{
    protected static Logger _logger = LogManager.getLogger(WikiContext.class);

    private static ThreadLocal<WikiContext> _threadLocal = new ThreadLocal<WikiContext>();

    private HttpServletRequest _request;

    private HttpServletResponse _response;

    private int _count;

    private Map<String, Object> _attributes;

    private User _user;

    private WikiPage _wikiPage;

    public static void begin(HttpServletRequest request, HttpServletResponse response)
    {
        WikiContext wikiContext = (WikiContext) _threadLocal.get();

        if (wikiContext == null) {
            wikiContext = new WikiContext();
            _threadLocal.set(wikiContext);
        }

        wikiContext.beginInstance(request, response);
    }

    public static void end(HttpServletRequest request, HttpServletResponse response)
    {
        WikiContext wikiContext = _threadLocal.get();

        if (wikiContext == null) {
            wikiContext = new WikiContext();
            _threadLocal.set(wikiContext);
        }

        if (wikiContext.endInstance(request, response)) {
            _threadLocal.set(null);
        }
    }

    public static WikiContext getWikiContext()
    {
        WikiContext wikiContext = _threadLocal.get();

        if (wikiContext == null) {
            _logger.error("getWikiContext called before the thread has initialised the context.");
        }

        return wikiContext;
    }

    public WikiContext()
    {
        _count = 0;
        _attributes = null;
    }

    private void beginInstance(HttpServletRequest request, HttpServletResponse response)
    {
        _count++;
        if (_count == 1) {
            _attributes = null;
            if ((_request != request) || (_response != response)) {
                _logger.error("Different request / response objects for the same wikiContext");
            }
        }
        _request = request;
        _response = response;
    }

    private boolean endInstance(HttpServletRequest request, HttpServletResponse response)
    {
        _count--;

        if (_count == 0) {
            _request = null;
            _response = null;
            _attributes = null;
            _user = null;
            _wikiPage = null;
            return true;
        }
        return false;
    }

    public HttpServletRequest getServletRequest()
    {
        if (_request == null) {
            throw new RuntimeException("WikiContext: Attempted to get the request after it has been reset.");
        }
        return _request;
    }

    public HttpServletResponse getServletResponse()
    {
        if (_response == null) {
            throw new RuntimeException("WikiContext: Attempted to get the response after it has been reset.");
        }
        return _response;
    }

    public void setAttribute(String name, Object value)
    {
        getAttributes().put(name, value);
    }

    public Map<String,Object> getAttributes()
    {
        if (_attributes == null) {
            _attributes = new HashMap<String,Object>();
        }
        return _attributes;
    }

    public Object getAttribute(String name)
    {
        return getAttributes().get(name);
    }

    public User getUser()
    {
        if (_user == null) {
            _user = WikiEngine.instance().getAuthenticationManager().getUserHardWork();
        }
        return _user;
    }

    public WikiPage getWikiPage()
    {
        return _wikiPage;
    }

    public void setWikiPage(WikiPage wikiPage)
    {
        _wikiPage = wikiPage;
    }

    /**
     * Allows two threads to share the same wiki context. To do this, the thread
     * which process the http request should create the WikiContext in the
     * normal way. Then it gets a reference to the WikiContext using
     * WikiContext.getWikiContext(). It can spawn a new thread, and inside that
     * thread call this method. From that point on, both thread will share the
     * same WikiContext.
     */
    public void share()
    {
        _threadLocal.set(this);

        // Get the user now, because if this thread finishes after the thread
        // handling the
        // http request, then the info needed to get the user will not be
        // available.
        getUser();

        _count++;
    }

    public void unshare()
    {
        endInstance(null, null);
    }

}
