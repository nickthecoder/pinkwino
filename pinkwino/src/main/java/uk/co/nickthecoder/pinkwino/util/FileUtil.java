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

package uk.co.nickthecoder.pinkwino.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
*/

public class FileUtil
{

    /**
     * It seems that java doesn't like renaming files across partitions (i.e. if
     * a rename actually requires a copy and a delete, it just returns false).
     * This method attempts to use java's rename, and if that fails, it does its
     * own copy and delete.
     */
    public static void rename(File source, File dest) throws IOException
    {
        if (!source.renameTo(dest)) {

            FileInputStream from = null;
            FileOutputStream to = null;

            try {
                from = new FileInputStream(source);
                to = new FileOutputStream(dest);

                stream(from, to);

            } finally {
                if (from != null) {
                    try {
                        from.close();
                    } catch (IOException e) {
                        // nothing
                    }
                }

                if (to != null) {
                    try {
                        to.close();
                    } catch (IOException e) {
                        // nothing
                    }
                }
            }

            source.delete();
        }

    }

    public static void stream(InputStream from, OutputStream to) throws IOException
    {

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = from.read(buffer)) != -1) {
            to.write(buffer, 0, bytesRead); // write
        }

        to.flush();
    }

    public static void streamFile(File file, OutputStream out) throws IOException
    {
        FileInputStream from = null;

        try {
            from = new FileInputStream(file);

            stream(from, out);

        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException e) {
                    // nothing
                }
            }
        }
    }

}
