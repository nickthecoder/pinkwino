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

import java.util.Iterator;

/**
*/

public class DescendantNodeIterator implements Iterator<Node>
{

    private Iterator<Node> _childIterator;

    private Iterator<Node> _deeperIterator;

    private Node _next;

    public DescendantNodeIterator(ParentNode parentNode)
    {
        _childIterator = parentNode.getChildren();
        _deeperIterator = null;

        if (_childIterator.hasNext()) {
            _next = _childIterator.next();
        } else {
            _next = null;
        }

    }

    public boolean hasNext()
    {
        return _next != null;
    }

    public void skip()
    {
        if (_deeperIterator != null) {
            _deeperIterator = null;
            next();
        }

    }

    public Node next()
    {
        Node result = _next;

        if (_deeperIterator == null) {

            if (_next instanceof ParentNode) {
                // We need to descend into its children before continuing with
                // its siblins
                _deeperIterator = new DescendantNodeIterator((ParentNode) _next);
            }

        }

        if (_deeperIterator != null) {

            if (_deeperIterator.hasNext()) {
                _next = _deeperIterator.next();
            } else {
                _deeperIterator = null;
            }

        }

        if (_deeperIterator == null) {

            if (_childIterator.hasNext()) {
                _next = _childIterator.next();
            } else {
                _next = null;
            }

        }

        return result;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
