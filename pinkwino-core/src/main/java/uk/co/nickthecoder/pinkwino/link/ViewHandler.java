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

package uk.co.nickthecoder.pinkwino.link;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.co.nickthecoder.pinkwino.WikiName;

/**
 * When pinkwino is used as a wiki component, then a link from one wiki snippet
 * to another, the page being linked to is NOT controlled by pinkwino. This
 * interface allows the owning application to decide how to handle the request.
 * Typically this will be to redirect.
 * 
 * For example, if we have a web application that is a directory of employees,
 * and pinkwino is used to allow comments about each employee. Imagine my page
 * links to your page (using the normal wiki style links such as
 * "See my mate [Bob Flemming]."). That link should NOT link to the page contain
 * Bob Flemming's wiki comment, it should link to the Bob Flemming's employee
 * page (which has his wiki comment somewhere on it). i.e. pinkwino does not
 * know how to render the whole page, only part of it.
 * 
 * This class allows the owning application (in this example the
 * "employee directory" webapp) to define what should happen when a wiki link is
 * followed. Typically this is a simple http redirect.
 */

public interface ViewHandler
{

    /**
     * If the return value is false, then the request has NOT been handled, and
     * pinkwino will proceed with its normal processing. If the return value is
     * true, pinkwino will do nothing with the request.
     */
    public boolean view(HttpServletRequest request, HttpServletResponse response, WikiName wikiName);

    public boolean view(HttpServletRequest request, HttpServletResponse response, WikiName wikiName, int versionNumber);

}

