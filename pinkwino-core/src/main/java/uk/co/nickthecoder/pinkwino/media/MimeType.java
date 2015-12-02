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

package uk.co.nickthecoder.pinkwino.media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Information about each mime type, such as the valid extensions.
 */

public class MimeType implements Comparable<MimeType>
{

    private String _mimeType;

    private List<String> _extensions;

    private boolean _isText;

    private boolean _isSafe;

    private Map<String, String> _views;

    public MimeType(String mimeType, List<String> extensions, boolean isSafe, boolean isText)
    {
        _mimeType = mimeType;
        _extensions = new ArrayList<String>();
        _extensions.addAll(extensions);
        _isSafe = isSafe;
        _isText = isText;
        _views = new HashMap<String, String>();
    }

    public MimeType(String mimeType, List<String> extensions, boolean isSafe)
    {
        this(mimeType, extensions, isSafe, mimeType.startsWith("text/"));
    }

    public MimeType(String mimeType, List<String> extensions)
    {
        this(mimeType, extensions, !("text/html".equals(mimeType)));
    }

    private static final List<String> singleItemList(String item)
    {
        List<String> extensions = new ArrayList<String>();
        extensions.add(item);
        return extensions;
    }

    public MimeType(String mimeType, String extension)
    {
        this(mimeType, singleItemList(extension), !("text/html".equals(mimeType)));
    }

    public List<String> getExtensions()
    {
        return _extensions;
    }

    public String getExtension()
    {
        if (_extensions.size() == 0) {
            return "";
        } else {
            return (String) _extensions.get(0);
        }
    }

    public String getMimeType()
    {
        return _mimeType;
    }

    public boolean isImage()
    {
        return _mimeType.startsWith("image");
    }

    public boolean isText()
    {
        return _isText;
    }

    public boolean isSafe()
    {
        return _isSafe;
    }

    /**
     * Returns the path of the jsp page that is responsisble for rendering the
     * view, or null if the default view should be used. See WikiServlet.forward
     * for more details. See WikiServlet.forward for more details.
     */
    public String getView(String viewType)
    {
        return (String) (_views.get(viewType));
    }

    /**
     * Sets the pat of the jsp page that is responsible for rendering the view.
     * Only set this if you want to override the default view. See
     * WikiServlet.forward for more details.
     */
    public void setView(String viewType, String page)
    {
        _views.put(viewType, page);
    }

    public String toString()
    {
        return _mimeType;
    }

    public int compareTo(MimeType other)
    {
        return _mimeType.compareTo(other._mimeType);
    }

}
