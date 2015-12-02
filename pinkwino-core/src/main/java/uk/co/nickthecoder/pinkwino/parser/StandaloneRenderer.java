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

package uk.co.nickthecoder.pinkwino.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Allows wiki syntax to be converted as a standalone command line tool. Useful
 * for ad-hoc testing.
 */

public class StandaloneRenderer
{

    public static void main(String[] args)
    {
        StringBuffer buffer = new StringBuffer();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                buffer.append(line).append("\n");
            }

        } catch (Exception e) {
            System.err.println("Failed to read input : " + e);
        }

        WikiPage page = new WikiPage(WikiName.create("unknown"));
        Version version = new Version(page);
        page.setCurrentVersion(version);
        version.setContent(buffer.toString());

        StandardRenderer renderer = new StandardRenderer();
        String result = renderer.render(version);
        System.out.println(result);

    }

    public StandaloneRenderer()
    {
    }

}
