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

public class Dependency implements Comparable<Dependency>
{
    public static final int DEPENDENCY_INTERNAL_LINK = 0;

    // public static final int DEPENDENCY_EXTERNAL_LINK = 0;

    public static final int DEPENDENCY_ATTACH = 1;

    public static final int DEPENDENCY_INCLUDE = 2;

    private WikiName _wikiName;

    private int _type;

    public Dependency(WikiName wikiName, int type)
    {
        _wikiName = wikiName;
        _type = type;
    }

    public WikiName getWikiName()
    {
        return _wikiName;
    }

    public int getType()
    {
        return _type;
    }

    public boolean isLink()
    {
        return _type == DEPENDENCY_INTERNAL_LINK;
        // return (_type == DEPENDENCY_INTERNAL_LINK) || (_type ==
        // DEPENDENCY_EXTERNAL_LINK);
    }

    public int compareTo(Dependency other)
    {
        if (other == null) {
            return 1;
        }

        int result = _wikiName.compareTo(other._wikiName);

        if (result == 0) {

            return _type - other._type;

        } else {

            return result;

        }
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Dependency) {

            return (_wikiName.equals(((Dependency) obj)._wikiName)) && _type == ((Dependency) obj)._type;

        }

        return false;
    }

    public String getLabel()
    {
        return _wikiName.getFormatted();
    }

    public String getUrl()
    {
        return _wikiName.getViewUrl();
    }

    public String toString()
    {
        return _wikiName.getFormatted();
    }

}
