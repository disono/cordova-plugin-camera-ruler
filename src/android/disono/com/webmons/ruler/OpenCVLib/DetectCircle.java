/**
 * Author: Archie, Disono (webmonsph@gmail.com)
 * Website: http://www.webmons.com
 *
 * Created at: 12/08/2018
 */
package disono.com.webmons.ruler.OpenCVLib;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class DetectCircle {
    private static final String TAG = "DetectCircle";

    public double x = 0;
    public double y = 0;
    public double r = 0;

    public void detect(String path) {
        Mat input = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Log.i(TAG, String.valueOf("Data Size: " + input.width()));

        if (input.width() > 0) {
            Mat circles = new Mat();
            Imgproc.blur(input, input, new Size(7, 7), new Point(2, 2));
            Imgproc.HoughCircles(input, circles, Imgproc.CV_HOUGH_GRADIENT,
                    2, 100, 100, 90, 0, 1000);

            if (circles.cols() > 0) {
                double circleVec[] = circles.get(0, 0);

                if (circleVec == null) {
                    return;
                }

                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];

                this.x = center.x;
                this.y = center.y;
                this.r = radius;

                Log.i(TAG, String.valueOf("Data Size: " + center.x) + ", " + String.valueOf(center.y) + ", " + String.valueOf(radius));
            }

            circles.release();
            input.release();
        }
    }
}
