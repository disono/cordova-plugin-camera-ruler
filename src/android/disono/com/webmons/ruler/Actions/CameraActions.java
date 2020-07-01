/**
 * Author: Archie, Disono (webmonsph@gmail.com)
 * Website: http://www.webmons.com
 *
 * Created at: 12/08/2018
 */

package disono.com.webmons.ruler.Actions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import stud.dress.booth.R;

public class CameraActions {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_SELECT_PHOTO = 2;

    private static File photoFile;

    /**
     * Take photo or launch the camera app
     *
     * @param activity
     * @return
     */
    public static File dispatchTakePictureIntent(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;

            try {
                photoFile = StorageActions.createImageFile(activity);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(activity, "Error creating image", Toast.LENGTH_SHORT).show();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(activity.getApplicationContext(), activity.getPackageName() + ".fileprovider", photoFile));
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                }

                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        return photoFile;
    }

    /**
     * Launch the gallery
     *
     * @param activity
     */
    public static void dispatchChoosePhotoIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getResources()
                .getString(R.string.action_choosePhoto)), REQUEST_SELECT_PHOTO);
    }
}
