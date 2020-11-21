package za.co.dwindle.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import za.co.dwindle.dialogs.PermissionCallback;

public class ImageUtils
{

    public static final String JPEG_EXTENSION = ".jpg";
    public static final String PNG_EXTENSION = ".png";

    public static Bitmap createBitmap(Context context, int width, int height, int resource)
    {
        BitmapDrawable bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(resource);
        Bitmap b = bitmapdraw.getBitmap();

        return Bitmap.createScaledBitmap(b, width, height, false);
    }

    public static void compressJPEG(final Context context, File file, int quality)
    {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try
        {
            if(file != null && file.exists())
            {
                fis = new FileInputStream(file);
                Bitmap original = BitmapFactory.decodeStream(fis);

                fos = new FileOutputStream(file);
                original.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                GeneralUtils.makeToast(context, "Compressed: " + quality + "% quality!");
            }
        }catch (Exception e)
        {
            Log.d(ConstantUtils.TAG, "Method: ImageUtils - compressJPEG"
                    + "\nError: " + e.getMessage()
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());

            DialogUtils.createAlertPermission(context, "Storage Permission", "Please enable storage permission for Dwindle.", true, new PermissionCallback() {
                @Override
                public void checkPermission(boolean ischeckPermission) {
                    if(ischeckPermission)
                    {
                        GeneralUtils.openAppSettingsScreen(context);
                    }
                }
            }).show();
        }finally {

            try
            {
                if(fis != null)
                {
                    fis.close();
                }

                if(fos != null)
                {
                    fos.flush();
                    fos.close();
                }

            }catch(Exception e)
            {
                Log.d(ConstantUtils.TAG, "Method: ImageUtils - compressJPEG"
                        + "\nError: " + e.getMessage()
                        + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
            }

        }
    }

    public static void compressPNG(final Context context, File file, int quality)
    {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try
        {
            if(file != null && file.exists())
            {
                fis = new FileInputStream(file);
                Bitmap original = BitmapFactory.decodeStream(fis);

                fos = new FileOutputStream(file);
                original.compress(Bitmap.CompressFormat.PNG, quality, fos);
                GeneralUtils.makeToast(context, "Compressed: " + quality + "% quality!");
            }
        }catch (Exception e)
        {
            Log.d(ConstantUtils.TAG, "Method: ImageUtils - compressPNG"
                    + "\nError: " + e.getMessage()
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());

            DialogUtils.createAlertPermission(context, "Storage Permission", "Please enable storage permission for Dwindle.", true, new PermissionCallback() {
                @Override
                public void checkPermission(boolean ischeckPermission) {
                    if(ischeckPermission)
                    {
                        GeneralUtils.openAppSettingsScreen(context);
                    }
                }
            }).show();
        }finally {

            try
            {
                if(fis != null)
                {
                    fis.close();
                }

                if(fos != null)
                {
                    fos.flush();
                    fos.close();
                }

            }catch(Exception e)
            {
                Log.d(ConstantUtils.TAG, "Method: ImageUtils - compressPNG"
                        + "\nError: " + e.getMessage()
                        + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
            }

        }

    }

    public static String getFileExtension(String path)
    {
        String toReturn = null;

        try
        {
            if(path != null)
            {
                toReturn = path.substring(path.lastIndexOf("."));
            }
        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG, "Method: ImageUtils - getFileExtension"
                    + "\nError: " + e.getMessage()
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }



        return toReturn;
    }
}
