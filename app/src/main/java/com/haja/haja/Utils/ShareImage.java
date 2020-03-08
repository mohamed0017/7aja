package com.haja.haja.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;

import com.haja.haja.Service.model.ProductData;
import com.squareup.picasso.BuildConfig;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareImage {
    static public void shareImage(String url, final Context context, ProductData productName) {
        Picasso.get().load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String text = productName.getName() + "\n" + productName.getDescription()
                        + "\n"  + productName.getMobile();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.putExtra(Intent.EXTRA_TEXT, productName.getName());
                i.putExtra(Intent.EXTRA_TEXT,text + "  \n \n     يمكنك تحميل التطبيق من هنا  " + "https://play.google.com/store/apps/details?id=" +context.getPackageName());
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                context.startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                e.printStackTrace();

            }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }
    static Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
             bmpUri = FileProvider.getUriForFile(context, "com.haja.haja" + ".fileprovider",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
