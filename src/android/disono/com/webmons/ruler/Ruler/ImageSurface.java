/**
 * Author: Archie, Disono (webmonsph@gmail.com)
 * Website: http://www.webmons.com
 *
 * Created at: 12/08/2018
 */
package disono.com.webmons.ruler.Ruler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;

public class ImageSurface extends SurfaceView implements SurfaceHolder.Callback {
    private Bitmap icon;
    private Paint paint;

    public ImageSurface(Context context, File image) {
        super(context);
        getHolder().addCallback(this);
        String imageFile = image.getAbsolutePath();
        icon = BitmapFactory.decodeFile(imageFile);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int newWidth = canvas.getHeight() * icon.getWidth() / icon.getHeight();
        icon = Bitmap.createScaledBitmap(icon, newWidth, canvas.getHeight(), false);
        canvas.drawColor(Color.BLACK);
        int cx = (canvas.getWidth() - icon.getWidth()) / 2;
        int cy = (canvas.getHeight() - icon.getHeight()) / 2;
        canvas.drawBitmap(icon, cx, cy, paint);
    }

    @SuppressLint("WrongCall")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas(null);
            synchronized (holder) {
                onDraw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
