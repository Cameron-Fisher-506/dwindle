package za.co.dwindle.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

import static java.util.jar.Pack200.Packer.ERROR;

public class DeviceMemoryUtils
{
    public static Long getFreeStorageSize()
    {
        Long toReturn = null;

        try
        {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                toReturn = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
            }
            else {
                toReturn = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
            }

            Long availableExternalMemory = getAvailableExternalMemorySize();
            if(availableExternalMemory != null)
            {
                if(toReturn != null)
                {
                    toReturn += availableExternalMemory;
                }else
                {
                    toReturn = availableExternalMemory;
                }

            }

        }catch (Exception e)
        {
            Log.d(ConstantUtils.TAG, "Method: DeviceUtils - getFreeStorageSize"
                    + "\nMessage: " + e.getMessage()
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static Long getUsedStorageSize()
    {
        Long toReturn = null;

        try
        {
            if(getTotalStorageSize() != null && getFreeStorageSize() != null)
            {
                toReturn = getTotalStorageSize() - getFreeStorageSize();
            }
        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG, "Method: DeviceUtils - getUsedStorageSize"
                    + "\nMessage: " + e.getMessage()
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static Long getTotalStorageSize()
    {
        Long toReturn = null;

        try
        {
            if(getTotalInternalMemorySize() != null)
            {
                toReturn = getTotalInternalMemorySize();
            }

            if(getTotalExternalMemorySize() != null)
            {
                if(toReturn != null)
                {
                    toReturn += getTotalExternalMemorySize();
                }else
                {
                    toReturn = getTotalExternalMemorySize();
                }

            }
        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG, "Method: DeviceUtils - getTotalStorageSize"
                    + "\nMessage: " + e.getMessage()
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


    public static Long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        } else {
            return null;
        }
    }

    public static Long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return totalBlocks * blockSize;
        } else {
            return null;
        }
    }

    public static Long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if(size >= 1024)
                {
                    suffix = "GB";
                    size /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }
}
