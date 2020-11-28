package com.deadely.itgenglish.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileUtils {
    public byte[] toByteArray(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = fis.read(buffer);
            if (read != -1)
                out.write(buffer, 0, read);
        }
        out.close();

        Log.e("byte array", Arrays.toString(out.toByteArray()));
        return out.toByteArray();
    }

}
