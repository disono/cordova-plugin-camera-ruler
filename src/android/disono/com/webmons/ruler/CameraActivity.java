/**
 * Author: Archie, Disono (webmonsph@gmail.com)
 * Website: http://www.webmons.com
 *
 * Created at: 12/08/2018
 */

package disono.com.webmons.ruler;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.io.File;

import disono.com.webmons.ruler.Actions.CameraActions;
import disono.com.webmons.ruler.Actions.StorageActions;
import disono.com.webmons.ruler.OpenCVLib.DetectCircle;
import disono.com.webmons.ruler.Ruler.DrawView;
import disono.com.webmons.ruler.Ruler.ImageSurface;
import disono.com.webmons.ruler.Ruler.InputDialog;
import disono.com.webmons.ruler.Ruler.Utils;
import stud.dress.booth.R;

public class CameraActivity extends AppCompatActivity implements InputDialog.InputDialogListener {
    private Activity activity;
    private static String TAG = "CameraActivity";

    private DrawView drawView;
    private FrameLayout preview;
    private File photoFile;

    private Button btn_ok;
    private Button btn_cancel;
    private Button btn_takePicture;

    DetectCircle detectCircle;

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.wtf(TAG, "OpenCV failed to load!");
        }
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String length = intent.getStringExtra("length");
                String unit = intent.getStringExtra("unit");
                if (length != null && unit != null) {
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_ruler_activity);
        activity = this;

        System.loadLibrary("opencv_java3");

        _UIInit();
        _UIAttached();
        _UIListener();
        _broadcastRCV();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(br);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_cleanStorage:
                StorageActions.cleanPhotoStorage(activity);
                break;
            case R.id.action_choosePhoto:
                CameraActions.dispatchChoosePhotoIntent(activity);
                break;
            case R.id.action_takePicture:
                photoFile = CameraActions.dispatchTakePictureIntent(activity);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        int inputUnit = ((Spinner) dialog.getDialog().findViewById(R.id.input_unit_chooser)).getSelectedItemPosition();
        int outputUnit = ((Spinner) dialog.getDialog().findViewById(R.id.output_unit_chooser)).getSelectedItemPosition();

        try {
            double reference = Double.parseDouble(((EditText) dialog.getDialog().findViewById(R.id.reference_input)).getText().toString());
            double result = drawView.calculate(reference, inputUnit, outputUnit);
            StorageActions.showResult(activity, result, outputUnit);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, getResources().getString(R.string.error_numberFormat), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CameraActions.REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(photoFile.getAbsolutePath())));
                    pictureTaken();
                }
                break;

            case CameraActions.REQUEST_SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String filePath = Utils.getPath(this, uri);
                    assert filePath != null;
                    photoFile = new File(filePath);
                    pictureTaken();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void _broadcastRCV() {
        IntentFilter filter = new IntentFilter("CordovaCameraRuler.Filter");
        activity.registerReceiver(br, filter);
    }

    private void pictureTaken() {
        preview.removeAllViews();

        btn_cancel.setVisibility(View.VISIBLE);
        btn_ok.setVisibility(View.VISIBLE);
        btn_takePicture.setVisibility(View.GONE);

        ImageSurface image = new ImageSurface(activity.getApplicationContext(), photoFile);
        preview.addView(image);

        ((TextView) findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setReferencePoints));

        // get the x and y
        detectCircle = new DetectCircle();
        detectCircle.detect(photoFile.getAbsolutePath());

        drawView = new DrawView(this, detectCircle);
        preview.addView(drawView);
    }

    private void _UIInit() {
        preview = (FrameLayout) findViewById(R.id.camera_preview);

        btn_ok = (Button) findViewById(R.id.button_calculate);
        btn_cancel = (Button) findViewById(R.id.button_cancel);
        btn_takePicture = (Button) findViewById(R.id.button_takePicture);
    }

    private void _UIAttached() {
        preview.removeAllViews();

        btn_cancel.setVisibility(View.GONE);
        btn_ok.setVisibility(View.GONE);
        btn_takePicture.setVisibility(View.VISIBLE);
    }

    private void _UIListener() {
        btn_takePicture.setOnClickListener(v -> photoFile = CameraActions.dispatchTakePictureIntent(activity));

        btn_ok.setOnClickListener(v -> new InputDialog().show(getFragmentManager(), "input_dialog"));

        btn_cancel.setOnClickListener(v -> drawView.clearCanvas());
    }

}
