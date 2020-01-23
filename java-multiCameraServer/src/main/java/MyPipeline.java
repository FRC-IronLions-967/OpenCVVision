import edu.wpi.first.vision.VisionPipeline;

import java.util.*;

import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.opencv.imgcodecs.*;

public class MyPipeline implements VisionPipeline {
    public int val;

    @Override
    public void process(Mat mat) {
        val += 1;
        Mat grey = new Mat();
        Imgproc.cvtColor(mat, grey, Imgproc.COLOR_RGB2GRAY);
        Imgcodecs.imwrite("/home/pi/photos/grey.jpg", grey);
        Mat bin = new Mat();
        Imgproc.threshold(grey, bin, 150, 255, 0);
        Imgcodecs.imwrite("/home/pi/photos/bin.jpg", bin);
        Mat edges = new Mat();
        Imgproc.Canny(bin, edges, 50, 200, 3, false);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f[] contoursPoly  = new MatOfPoint2f[contours.size()];
        Rect bounds[] = new Rect[contours.size()];
        for(int i = 0; i < contours.size(); i++) {
          contoursPoly[i] = new MatOfPoint2f();
          Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
          bounds[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
        }
        if(bounds.length > 0) {
          double a = bounds[0].area();
          //TODO find the distance as a function of area
        }
        // Mat lines = new Mat();
        // Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180, 150);
        // Point vertices[] = new Point[lines.rows()];
        // for(int i = 0; i < lines.rows(); i++) {
        //     double rho = lines.get(i, 0)[0];
        //     double theta = lines.get(i, 0)[1];
        //     vertices[i] = new Point(Math.round(rho * Math.cos(theta)), Math.round(rho * Math.sin(theta)));
        // }
        // // Mat colorEdges = new Mat();
        // Imgproc.cvtColor(edges, colorEdges, Imgproc.COLOR_GRAY2BGR);
        // // Draw the lines
        // for (int x = 0; x < lines.rows(); x++) {
        //     double rho = lines.get(x, 0)[0];
        //     double theta = lines.get(x, 0)[1];
        //     double a = Math.cos(theta), b = Math.sin(theta);
        //     double x0 = a*rho, y0 = b*rho;
        //     Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
        //     Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
        //     Imgproc.line(colorEdges, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        // }
        // Imgcodecs.imwrite("/home/pi/photos/coloredges.jpg", colorEdges);
    }
  }

//   Imgcodecs.imwrite("/home/pi/photos/original.jpg", mat);
//         Mat grey = new Mat();
//         Imgproc.cvtColor(mat, grey, Imgproc.COLOR_RGB2GRAY);
//         Imgproc.blur(grey, grey, new Size(3, 3), new Point(-1, -1));
//         Imgcodecs.imwrite("/home/pi/photos/grey.jpg", grey);
//         Mat edges = new Mat();
//         Imgproc.Canny(grey, edges, 50, 200, 3, false);
//         Imgcodecs.imwrite("/home/pi/photos/edges.jpg", edges);
        // Mat colorEdges = new Mat();
        // Imgproc.cvtColor(edges, colorEdges, Imgproc.COLOR_GRAY2BGR);
        // Mat lines = new Mat(); // will hold the results of the detection
        // Imgproc.HoughLines(edges, lines, 1, Math.PI/180, 150); // runs the actual detection
        // List<Line> linesV2ElectricBoogaloo = new ArrayList<Line>();
        // // Draw the lines
        // for (int x = 0; x < lines.rows(); x++) {
        //     double rho = lines.get(x, 0)[0];
        //     double theta = lines.get(x, 0)[1];
        //     double a = Math.cos(theta), b = Math.sin(theta);
        //     double x0 = a*rho, y0 = b*rho;
        //     Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
        //     Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
        //     double m = (pt1.y - pt2.y)/(pt1.x - pt2.x);
        //     double intercept = (pt1.y - (pt1.x * m));
        //     linesV2ElectricBoogaloo.add(new Line(m, intercept));
        //     Imgproc.line(colorEdges, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        // }
        // Imgcodecs.imwrite("/home/pi/photos/coloredges.jpg", colorEdges);

        // Mat linesP = new Mat(); // will hold the results of the detection
        // Imgproc.HoughLinesP(edges, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
        // // Draw the lines
        // for (int x = 0; x < linesP.rows(); x++) {
        //     double[] l = linesP.get(x, 0);
        //     Imgproc.line(colorEdges, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        // }
        // Imgcodecs.imwrite("/home/pi/photos/coloredgesprobablistic.jpg", colorEdges);

        // List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        // Mat hierarchy = new Mat();
        // Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        // Mat drawnContours = Mat.zeros(edges.size(), CvType.CV_8UC3);
        // Rect[] boundRect = new Rect[contours.size()];
        // for (int i = 0; i < contours.size(); i++) {
        //     Scalar color = new Scalar(0, 0, 255);
        //     Imgproc.drawContours(drawnContours, contours, i, color, 2, Core.LINE_8, hierarchy, 0, new Point());
        //     Imgproc.rectangle(drawnContours, boundRect[i].tl(), boundRect[i].br(), color, 2);
        // }

        // Imgcodecs.imwrite("/home/pi/photos/contours.jpg", drawnContours);