import java.util.*;

import org.opencv.core.*;
import org.opencv.imgproc.*;

import edu.wpi.first.vision.VisionPipeline;

public class LemonPipeline implements VisionPipeline {
    public static volatile int val;
    public static volatile Mat drawing;
    public static volatile double tx;
    public static volatile double ty;
    public static volatile double width;
    public static volatile double height;

    @Override
    public void process(Mat mat) {
        val++;

        Mat proc = new Mat();
  
        Imgproc.cvtColor(mat, proc, Imgproc.COLOR_BGR2HSV);
  
        //inRange(proc, Scalar(0, 127, 127), Scalar(15, 255, 255), proc); //tracks red
        Core.inRange(proc, new Scalar(22, 95, 0), new Scalar(30, 255, 255), proc);
  
        //blur(proc, proc, Size(4, 4));
  
        //threshold(proc, proc, 150, 255, 0);
  
        Imgproc.erode(proc, proc, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
        Imgproc.dilate(proc, proc, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
  
        Imgproc.dilate(proc, proc, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
        Imgproc.erode(proc, proc, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
  
        Imgproc.Canny(proc, proc, 100, 200, 3, false);
  
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
  
        Imgproc.findContours(proc, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
  
        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
        Rect boundRects[] = new Rect[contours.size()];
  
        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRects[i] = Imgproc.boundingRect(contoursPoly[i]);
        }
  
        Imgproc.cvtColor(proc, proc, Imgproc.COLOR_GRAY2BGR);
  
        //for (int i = 0; i < contours.size(); i++) {
        //    drawContours(*dest, contours, i, Scalar(255, 0, 0), 2, 8, hierarchy, 0, Point());
        //}
        Rect bRect = new Rect();
  
        if (boundRects.length > 0) {
            int maxIndex = 0;
            for (int i = 0; i < boundRects.length; i++) {
                maxIndex = (boundRects[i].area() > boundRects[maxIndex].area()) ? i : maxIndex;
            }
            Imgproc.rectangle(proc, boundRects[maxIndex].tl(), boundRects[maxIndex].br(), new Scalar(0, 0, 255));
            bRect = boundRects[maxIndex];
        }
  
        //double tx, ty; //pixels offset from center of image
  
        if (bRect.width != 0 && bRect.height != 0) {
            tx = ((double)bRect.tl().x - (((double)proc.cols() / 2.0) - (double)bRect.width / 2.0));
            ty = ((((double)proc.rows() / 2.0) - (double)bRect.height / 2.0) - (double)bRect.tl().y);
            width = (double) bRect.width;
            height = (double) bRect.height;
            //cout << tx << ", " << ty << endl;
        }
  
      
      }
}