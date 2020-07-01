/**
 * Author: Archie, Disono (webmonsph@gmail.com)
 * Website: http://www.webmons.com
 *
 * Created at: 12/08/2018
 */
package disono.com.webmons.ruler.Actions;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import stud.dress.booth.R;

public class StorageActions {

    /**
     * Create images
     *
     * @param activity
     * @return
     * @throws IOException
     */
    public static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                /* prefix */
                imageFileName,
                /* suffix */
                ".jpg",
                /* directory */
                storageDir
        );
    }

    /**
     * Delete all images taken
     *
     * @param activity
     */
    public static void cleanPhotoStorage(Activity activity) {
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fList[] = storageDir.listFiles();

        // Search for pictures in the directory
        for (File aFList : fList) {
            String pes = aFList.getName();
            if (pes.endsWith(".jpg"))
                new File(aFList.getAbsolutePath()).delete();
        }

        Toast.makeText(activity,
                activity.getResources().getString(R.string.storageDeleted), Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Show the final computation for length
     *
     * @param activity
     * @param result
     */
    public static void showResult(Activity activity, double result, double reference) {
        if (result != -1) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            String mLabel = "";
            switch ((int) reference) {
                case 0:
                    mLabel = "Meters";
                    break;
                case 1:
                    mLabel = "Centimeters";
                    break;
                case 2:
                    mLabel = "Millimetres";
                    break;
                case 3:
                    mLabel = "Inch";
                    break;
                case 4:
                    mLabel = "Foot";
                    break;
                case 5:
                    mLabel = "Yard";
                    break;
            }

            Intent intent = new Intent();
            intent.setAction("CordovaCameraRuler.Filter");
            intent.putExtra("length", decimalFormat.format(result));
            intent.putExtra("unit", mLabel);
            activity.sendBroadcast(intent);
        }
    }

}
