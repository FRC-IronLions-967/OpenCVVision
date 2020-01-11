import edu.wpi.first.vision.VisionPipeline;

import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.opencv.imgcodecs.*;

public class MyPipeline implements VisionPipeline {
    public int val;

    @Override
    public void process(Mat mat) {
      val += 1;
      Imgcodecs.imwrite("/home/pi/photos/original.jpg", mat);
      Mat grey = new Mat();
      Imgproc.cvtColor(mat, grey, Imgproc.COLOR_RGB2GRAY);
      Imgcodecs.imwrite("/home/pi/photos/grey.jpg", grey);
      Mat edges = new Mat();
      Imgproc.Canny(grey, edges, 50, 200, 3, false);
      Imgcodecs.imwrite("/home/pi/photos/edges.jpg", edges);
      Mat colorEdges = new Mat();
      Imgproc.cvtColor(edges, colorEdges, Imgproc.COLOR_GRAY2BGR);
      Mat lines = new Mat(); // will hold the results of the detection
      Imgproc.HoughLines(edges, lines, 1, Math.PI/180, 150); // runs the actual detection
      // Draw the lines
      for (int x = 0; x < lines.rows(); x++) {
          double rho = lines.get(x, 0)[0];
          double theta = lines.get(x, 0)[1];
          double a = Math.cos(theta), b = Math.sin(theta);
          double x0 = a*rho, y0 = b*rho;
          Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
          Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
          Imgproc.line(colorEdges, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
      }
      Imgcodecs.imwrite("/home/pi/photos/coloredges.jpg", colorEdges);

      Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(edges, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(colorEdges, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        Imgcodecs.imwrite("/home/pi/photos/coloredgesprobablistic.jpg", colorEdges);
    }
  }