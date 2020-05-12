import edu.wpi.first.vision.VisionPipeline;

import java.util.*;

import org.opencv.core.*;
import org.opencv.imgproc.*;

public class MyPipeline implements VisionPipeline {
  public static Mat drawing;
  public static int val;
  public static double tx;
  public static double ty;

  @Override
  public void process(Mat mat) {
    Mat proc = new Mat();

    val++;
    
    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

    Imgproc.blur(mat, mat, new Size(3, 3));

    Imgproc.threshold(mat, mat, 150, 255, 0);

    Imgproc.Canny(mat, mat, 50, 200, 3, false);

    List<MatOfPoint> contours = new ArrayList<>();
    Mat hierarchy = new Mat();

    Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

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
      //cout << tx << ", " << ty << endl;
  }
}

  // public static int val;
  // // public static Mat hierarchy;
  // public static Mat drawing;

  // @Override
  // public void process(Mat mat) {
  //   // hierarchy = new Mat();
  //   // drawing = new Mat();
  //   val += 1;
  //   // Mat grey = new Mat();
  //   Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
  //   // Imgcodecs.imwrite("/home/pi/photos/grey.jpg", grey);
  //   Imgproc.blur(mat, mat, new Size(3, 3));
  //   // Mat bin = new Mat();
  //   Imgproc.threshold(mat, mat, 150, 255, 0);
  //   // Imgcodecs.imwrite("/home/pi/photos/bin.jpg", bin);
  //   // Mat edges = new Mat();
  //   Imgproc.Canny(mat, mat, 50, 200, 3, false);
  //   List<MatOfPoint> contours = new ArrayList<>();
  //   Mat hierarchy = new Mat();
  //   Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

  //   //calculate the bounding rectangles
  //   MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
  //   Rect bounds[] = new Rect[contours.size()];
  //   for (int i = 0; i < contours.size(); i++) {
  //     contoursPoly[i] = new MatOfPoint2f();
  //     Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
  //     bounds[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
  //   }

  //   //initialize matrix and list for holding the contours and shapes
  //   drawing = Mat.zeros(mat.size(), CvType.CV_8UC3);
  //   List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
    
  //   //add all of the bounding rectangles to a list
  //   for (MatOfPoint2f poly : contoursPoly) {
  //     contoursPolyList.add(new MatOfPoint(poly.toArray()));
  //   }

  //   //draw the bounding rectangles and the contours - unused, currently draws the largest later on in the code
  //   for (int i = 0; i < contours.size(); i++) {
  //     Imgproc.drawContours(drawing, contoursPolyList, i, new Scalar(0, 255, 0));
  //     // Imgproc.rectangle(drawing, bounds[i].tl(), bounds[i].br(), new Scalar(255, 0, 0), 2);
  //   }

  //   // Imgcodecs.imwrite("/home/pi/photos/drawing.jpg", drawing);

  //   //find the index of the largest rect by area
  //   if(bounds.length > 0) {
  //     int maxIndex = 0;
  //     for(int i = 0; i < bounds.length; i++) {
  //       maxIndex = bounds[i].area() > bounds[maxIndex].area() ? i : maxIndex;
  //     }
  //     Imgproc.rectangle(drawing, bounds[maxIndex].tl(), bounds[maxIndex].br(), new Scalar(255, 0, 0), 2);
  //     double height = bounds[maxIndex].height;
  //     double width = bounds[maxIndex].width;
  //     NetworkTableEntry areaEntry = Main.visionTable.getEntry("height");
  //     NetworkTableEntry widthEntry = Main.visionTable.getEntry("width");
  //     areaEntry.setDouble(height);
  //     widthEntry.setDouble(width);
  //   }
    // Mat lines = new Mat();
    // Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180, 150);
    // Point vertices[] = new Point[lines.rows()];
    // for(int i = 0; i < lines.rows(); i++) {
    // double rho = lines.get(i, 0)[0];
    // double theta = lines.get(i, 0)[1];
    // vertices[i] = new Point(Math.round(rho * Math.cos(theta)), Math.round(rho *
    // Math.sin(theta)));
    // }
    // // Mat colorEdges = new Mat();
    // Imgproc.cvtColor(edges, colorEdges, Imgproc.COLOR_GRAY2BGR);
    // // Draw the lines
    // for (int x = 0; x < lines.rows(); x++) {
    // double rho = lines.get(x, 0)[0];
    // double theta = lines.get(x, 0)[1];
    // double a = Math.cos(theta), b = Math.sin(theta);
    // double x0 = a*rho, y0 = b*rho;
    // Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
    // Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
    // Imgproc.line(colorEdges, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA,
    // 0);
    // }
    // Imgcodecs.imwrite("/home/pi/photos/coloredges.jpg", colorEdges);
  
}