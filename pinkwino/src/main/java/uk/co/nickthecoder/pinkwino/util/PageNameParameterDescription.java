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

package uk.co.nickthecoder.pinkwino.util;

/**
 * All parameters which are page names should use this class. The advantages
 * being consistency, and also plugins parameters which use this will
 * automatically have the correct namespace, if none is specified.
 */

public class PageNameParameterDescription extends RegexParameterDescription
{

    public PageNameParameterDescription()
    {
        this("page");
    }

    public PageNameParameterDescription(String name)
    {
        this(name, false, "", USAGE_GENERAL);
    }

    public PageNameParameterDescription(String name, boolean required, String defaultValue, String usage)
    {
        super(name, ".*", required, defaultValue, usage);
    }

    public ParameterDescription required()
    {
        return new PageNameParameterDescription(getName(), true, getDefaultValue(), getUsage());
    }

    public ParameterDescription defaultValue(String defaultValue)
    {
        return new PageNameParameterDescription(getName(), getRequired(), defaultValue, getUsage());
    }

}
