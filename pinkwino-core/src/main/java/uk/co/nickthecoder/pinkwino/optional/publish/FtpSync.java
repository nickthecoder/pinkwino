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

package uk.co.nickthecoder.pinkwino.optional.publish;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FtpSync
{

    protected static Logger _logger = LogManager.getLogger(FtpSync.class);

    private FtpDetails _ftpDetails;

    private int _maxDepth;

    private FTPClient _ftp;

    public FtpSync(FtpDetails ftpDetails)
    {
        this(ftpDetails, 10);
    }

    public FtpSync(FtpDetails ftpDetails, int maxDepth)
    {
        _ftpDetails = ftpDetails;
        _maxDepth = maxDepth;
    }

    public void upload() throws IOException
    {

        _logger.info("Uploading");

        _ftp = new FTPClient();

        if (_ftpDetails.getTimeZoneId() != null) {
            // _logger.debug( "Setting time zone to " +
            // _ftpDetails.getTimeZoneId() );
            FTPClientConfig config = new FTPClientConfig();
            config.setServerTimeZoneId(_ftpDetails.getTimeZoneId());
            _ftp.configure(config);
        }

        try {

            _ftp.connect(_ftpDetails.getServer());

            int reply = _ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                _logger.error("FTP Connection failed");

            } else {

                _ftp.login(_ftpDetails.getUsername(), _ftpDetails.getPassword());

                uploadDirectory(new File(_ftpDetails.getLocalBase()), _ftpDetails.getRemoteBase(), 0);

                _ftp.logout();

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (_ftp.isConnected()) {
                try {
                    _ftp.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }

        _logger.info("Uploading Complete");

    }

    private void uploadDirectory(File localDir, String remotePath, int depth) throws IOException
    {
        // _logger.info( "Syncing directory " + localDir + " -> " + remotePath
        // );

        if (depth >= _maxDepth) {
            // _logger.info( "Max recursion met : " + _maxDepth );
            return;
        }

        _ftp.changeWorkingDirectory(remotePath);

        File[] localFiles = localDir.listFiles();
        FTPFile[] remoteFiles = _ftp.listFiles(remotePath);

        // Iterate over each file in the local directory
        for (int i = 0; i < localFiles.length; i++) {

            File localFile = localFiles[i];

            if (localFile.isFile()) {

                FTPFile remoteFile = findFTPFile(remoteFiles, localFile.getName());

                if (remoteFile == null) {
                    uploadFile(localFile);
                } else if (needsUploading(localFile, remoteFile)) {
                    _ftp.deleteFile(localFile.getName());
                    uploadFile(localFile);
                } else {
                }
            }
        }

        // Iterate over each directory in the local directory
        for (int i = 0; i < localFiles.length; i++) {
            File localFile = localFiles[i];
            if (localFile.isDirectory()) {

                FTPFile remoteFile = findFTPFile(remoteFiles, localFile.getName());

                // If an existing **file** is where a directory needs to be,
                // delete the file.
                if ((remoteFile != null) && (!remoteFile.isDirectory())) {
                    _ftp.deleteFile(localFile.getName());
                    remoteFile = null;
                }

                String remoteDirectory = remotePath + "/" + localFile.getName();

                // Make the directory if needed
                if (remoteFile == null) {
                    _ftp.makeDirectory(remoteDirectory);
                }

                // Recurse
                uploadDirectory(localFile, remoteDirectory, depth + 1);
            }
        }

    }

    public FTPFile findFTPFile(FTPFile[] ftpFiles, String name)
    {
        for (int i = 0; i < ftpFiles.length; i++) {
            if (ftpFiles[i].getName().equals(name)) {
                return ftpFiles[i];
            }
        }
        return null;
    }

    public boolean needsUploading(File localFile, FTPFile remoteFile)
    {
        long remoteTime = remoteFile.getTimestamp().getTimeInMillis();
        long localTime = localFile.lastModified();

        return remoteTime < localTime;
    }

    public void uploadFile(File localFile) throws IOException
    {
        InputStream local = new FileInputStream(localFile);
        try {

            _ftp.storeFile(localFile.getName(), local);

        } finally {
            try {
                local.close();
            } catch (Exception e) {
                // Do nothing
            }
        }
    }

    public static void main(String[] argv)
    {
        if (argv.length < 3) {
            System.out.println("Usage : ftpSync SERVER USERNAME PASSWORD [LOCAL_BASE] [REMOTE_BASE]");
            System.exit(0);
        }

        String server = argv[0];
        String username = argv[1];
        String password = argv[2];

        String remoteBase = "/";
        String localBase = ".";

        if (argv.length > 3) {
            localBase = argv[3];
        }
        if (argv.length > 4) {
            remoteBase = argv[4];
        }

        FtpDetails ftpDetails = new FtpDetails(server, username, password, remoteBase, localBase);
        FtpSync ftpSync = new FtpSync(ftpDetails);

        try {
            ftpSync.upload();
        } catch (Exception e) {
            System.out.println("Failed ");
            e.printStackTrace();
        }

    }

}
