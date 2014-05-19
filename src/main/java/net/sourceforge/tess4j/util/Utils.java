/*
 * Copyright @ 2013 Quan Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.tess4j.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Utils {

    /**
     * Writes byte array to file.
     *
     * @param data byte array
     * @param outFile output file
     * @throws IOException
     */
    public static void writeFile(byte[] data, File outFile) throws IOException {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(outFile);
            fos.write(data);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * Gets user-friendly name of the public static final constant defined in a
     * class or an interface for display purpose.
     *
     * @param value the constant value
     * @param c type of class or interface
     * @return name
     */
    public static String getConstantName(Object value, Class c) {
        for (Field f : c.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                try {
                    if (f.get(null).equals(value)) {
                        return f.getName();
                    }
                } catch (IllegalAccessException e) {
                    return String.valueOf(value);
                }
            }
        }
        return String.valueOf(value);
    }
        
    /**
     * Gets filename without extension.
     * http://stackoverflow.com/questions/924394/how-to-get-file-name-without-the-extension
     *
     * @param str
     * @return
     */
    public static String stripExtension(String str) {
        // Handle null case specially.
        if (str == null) {
            return null;
        }

        // Get position of last '.'.
        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) {
            return str;
        }

        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }
}
