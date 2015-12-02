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

package uk.co.nickthecoder.pinkwino.optional.image;

import java.io.File;

import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public abstract class AbstractImageTransform implements ImageTransform
{

    private ParameterDescriptions _parameterDescriptions;

    public AbstractImageTransform()
    {
        _parameterDescriptions = new ParameterDescriptions();
    }

    public ParameterDescriptions getParameterDescriptions()
    {
        return _parameterDescriptions;
    }

    public void addParameterDescription(ParameterDescription pd)
    {
        _parameterDescriptions.addParameterDescription(pd);
    }

    abstract public void transform(File input, File ouput, Parameters parameters) throws Exception;

    abstract public long getDependentDate();

}
