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

package uk.co.nickthecoder.pinkwino.commands;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

public class BatchProcessor implements Runnable
{

    protected static Logger _logger = LogManager.getLogger(BatchProcessor.class);

    private static Map<WikiName, BatchProcessor> _register = new HashMap<WikiName, BatchProcessor>();

    private WikiPage _logPage;

    private Runnable _runnable;

    private WikiContext _wikiContext;

    private boolean register(BatchProcessor batchProcessor)
    {
        if (batchProcessor.getWikiName() != null) {
            if (_register.get(batchProcessor.getWikiName()) != null) {
                return false;
            }
            _register.put(batchProcessor.getWikiName(), batchProcessor);
        }
        return true;
    }

    private void unregister(BatchProcessor batchProcessor)
    {
        if (batchProcessor.getWikiName() != null) {
            _register.remove(batchProcessor.getWikiName());
        }
    }

    public BatchProcessor(Runnable runnable)
    {
        this(runnable, null);
    }

    public BatchProcessor(Runnable runnable, WikiPage logPage)
    {
        _runnable = runnable;
        _logPage = logPage;
        _wikiContext = WikiContext.getWikiContext();
    }

    public WikiPage getWikiPage()
    {
        return _logPage;
    }

    public WikiName getWikiName()
    {
        if (_logPage == null) {
            return null;
        } else {
            return _logPage.getWikiName();
        }
    }

    public void start()
    {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run()
    {
        _wikiContext.share();

        try {

            if (register(this)) {

                try {
                    update();
                    _runnable.run();
                    update();

                } finally {

                    unregister(this);
                }

            } else {
                _logger.info("This batch process is already running");
            }

        } finally {
            _wikiContext.unshare();
        }

    }

    public void updateResults(String string)
    {
        if (_logPage != null) {
            WikiEngine.instance().save(_logPage, string, null);
        }
    }

    /**
     * Override this method in subclasses. There are two ways this can be
     * called, (1) when the job completes and (2) whenever update is called
     * externally - this will be done using a special "update log" plugin.
     */
    public void update()
    {
        // do nothing
    }

}

