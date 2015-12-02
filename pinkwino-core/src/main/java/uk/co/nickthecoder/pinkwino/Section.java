/* {{{ GPL

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

}}} */

package uk.co.nickthecoder.pinkwino;

// {{{ imports
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
*/

public class Section
{
    private Section _parent;

    private int _level;

    private String _id;

    private List<Section> _subsections;

    private String _title;

    private int _startLineNumber;

    private int _endLineNumber;

    private String _linkName;

    public Section(int level, String title)
    {
        _parent = null;
        _level = level;
        _title = title;
        _subsections = new ArrayList<Section>();
        _id = "";
        _startLineNumber = -1;
        _endLineNumber = -1;
        _linkName = null;
    }

    public Section getParent()
    {
        return _parent;
    }

    public void addSubsection(Section section)
    {
        _subsections.add(section);
        section._parent = this;
        if (this._id.length() > 0) {
            section._id = this._id + "_" + _subsections.size();
        } else {
            section._id = "" + _subsections.size();
        }
    }

    public String getId()
    {
        return _id;
    }

    public String getLinkName()
    {
        if (_linkName == null) {
            return _id;
        } else {
            return _linkName;
        }
    }

    public List<Section> getSubsections()
    {
        return _subsections;
    }

    public int getLevel()
    {
        return _level;
    }

    public void setLevel(int value)
    {
        _level = value;
    }

    public String getTitle()
    {
        return _title;
    }

    public void setTitle(String value)
    {
        _title = value;
        StringBuffer buffer = new StringBuffer(value.length() + _id.length() + 1);
        buffer.append(_id).append("_");

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                buffer.append(c);
            }
        }
        _linkName = buffer.toString();
    }

    public String getEditUrl()
    {
        return WikiEngine.instance().getUrlManager().getEditSectionUrl(getWikiDocument().getWikiPage(), _id);
    }

    public WikiDocument getWikiDocument()
    {
        if (_parent == null) {
            return null;
        }

        return _parent.getWikiDocument();
    }

    public void setStartLineNumber(int line)
    {
        _startLineNumber = line;
    }

    public int getStartLineNumber()
    {
        return _startLineNumber;
    }

    public void setEndLineNumber(int line)
    {
        _endLineNumber = line;
    }

    public int getEndLineNumber()
    {
        return _endLineNumber;
    }

    public String getContent()
    {
        String all = getWikiDocument().getVersion().getContent();

        // Wow, this is a lot of code to extract a subset of lines from a
        // string.
        // Replace this if there is a better way!
        try {

            StringBuffer buffer = new StringBuffer();
            LineNumberReader reader = new LineNumberReader(new StringReader(all));
            int i;
            for (i = 1; i < _startLineNumber; i++) {
                String skipped = reader.readLine();
                if (skipped == null) {
                    return "";
                }
            }

            for (int j = i; (j <= _endLineNumber) || (_endLineNumber < 0); j++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                buffer.append(line).append("\n");
            }

            return buffer.toString();

        } catch (IOException e) {
            // I don't believe this will ever happen.
            throw new RuntimeException(e);
        }

    }

    public Section getSection(String id)
    {
        if (id.equals(_id)) {
            return this;
        }

        for (Iterator<Section> i = _subsections.iterator(); i.hasNext();) {
            Section section = (Section) i.next();

            if (id.equals(section.getId())) {
                return section;
            }

            if (id.startsWith(section.getId() + "_")) {
                return section.getSection(id);
            }
        }

        return null;
    }

    public String toString()
    {
        return "Section: " + _id + "(" + _title + ") line range (" + _startLineNumber + ".." + _endLineNumber + ")";
    }

}
