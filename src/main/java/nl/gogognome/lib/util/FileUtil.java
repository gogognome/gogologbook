/*
   Copyright 2011 Sander Kooijmans

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package nl.gogognome.lib.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class contains utility methods for files.
 */
public class FileUtil {

    /** Private constructor. Use static methods only. */
    private FileUtil() {
        // should never be called
    }

    /**
     * Copies a file. The last-modification time of <code>dst</code> will be
     * the same as that of <code>src</code>.
     *
     * <p>If <code>dst</code> exists and it has the same last-modification time
     * as <code>src</code> and the same size, then it is assumed that
     * the files are equal. In this case, the method returns immediately.
     *
     * @param src the source file
     * @param dst the destination file
     * @throws IOException if a problem occurs while copying
     */
    public static void copyFile(File src, File dst) throws IOException {
        if (dst.exists() && src.lastModified() == dst.lastModified()
                && src.length() == dst.length()) {
            return;
        }

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(src));
            outputStream = new BufferedOutputStream(new FileOutputStream(dst));
            byte[] buffer = new byte[16 * 1024];
            int size = inputStream.read(buffer);
            while (size != -1) {
                outputStream.write(buffer, 0, size);
                size = inputStream.read(buffer);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (dst.exists()) {
                dst.setLastModified(src.lastModified());
            }
        }
    }
}
