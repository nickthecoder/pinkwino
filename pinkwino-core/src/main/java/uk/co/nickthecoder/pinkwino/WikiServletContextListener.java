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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
  Listens for when the servlet context is started, and initialises the WikiEngine.
*/

public class WikiServletContextListener
  implements ServletContextListener
{
  protected static Logger _logger = LogManager.getLogger( WikiServletContextListener.class );


  public WikiServletContextListener()
  {
  }

  public void contextDestroyed( ServletContextEvent sce )
  {
    // Do nothing
  }

  public void contextInitialized( ServletContextEvent sce )
  {
    try {
      ServletContext servletContext = sce.getServletContext();
      WikiEngine.initialise( servletContext );
    } catch (Exception e) {
      _logger.error( "contextInitialized failed" );
      _logger.error( e );
    }
  }

}

