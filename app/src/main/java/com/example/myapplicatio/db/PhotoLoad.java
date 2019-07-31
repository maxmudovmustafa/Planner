package com.example.myapplicatio.db;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import uz.greenwhite.lib.util.BitmapUtil;
import uz.greenwhite.lib.util.Util;

public class PhotoLoad {

    public static Bitmap getPhoto(int requestCode, int resultCode, Intent intent, Activity activity) {
        String pathFile = intent.getData().getPath();
        if (TextUtils.isEmpty(pathFile)) {
            Uri data = intent.getData();
            ContentResolver contentResolver = activity.getContentResolver();
            String mimeType = contentResolver.getType(data);
            String fileName = "";

            if (TextUtils.isEmpty(mimeType)) {
                fileName = TextUtils.isEmpty(pathFile) ? new File(data.getPath()).getName() : new File(pathFile).getName();

            } else {
                Cursor returnCursor = contentResolver.query(data, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                fileName = returnCursor.getString(nameIndex);
            }

            try {
                InputStream in = contentResolver.openInputStream(data);
                File file = new File(activity.getExternalCacheDir(), fileName);
                FileOutputStream out = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                assert in != null;
                while (in.read(bytes) != -1) {
                    out.write(bytes);
                }
                in.close();
                out.close();
                return fileLoad(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File file = new File(pathFile);
            return fileLoad(file);
        }
        return null;
    }

    private static Bitmap fileLoad(File file) {

        try {
//            InputStream is = new FileInputStream(file);
//            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//            byte[] byteFile = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = is.read(byteFile)) != -1) {
//                buffer.write(byteFile, 0, bytesRead);
//            }
//            byte[] bytes = buffer.toByteArray();
//            String sha = Util.calcSHA(bytes);

            Bitmap bitmap = BitmapUtil.decodeFile(file, 100);
            file.getPath();

            return bitmap;
        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
