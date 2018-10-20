package com.example.luispadilla.projecto_averias.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Utilities {
    public static Boolean isEmptyStringField(String val) {
        return val == null || val.equals("");
    }
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void SwitchActivity(Context ctx, Class newActivity, HashMap<String, Object> intentParams){ // Activity context
        Intent myIntent = new Intent(ctx, newActivity);
        if(intentParams != null && !intentParams.isEmpty()){
            for (HashMap.Entry<String, Object> entry : intentParams.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                myIntent.putExtra(key, (String)value);
            }
        }
        ctx.startActivity(myIntent);
    }

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        return sdf.format(new Date());
    }

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
    public static File createImageFile (Context context) {

        String timeStamp = Utilities.getCurrentDate() + Utilities.getUUID();
        String imageFileName = "JPEG_" + timeStamp;
        File image = null;

        try {
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return image;
    }

}
