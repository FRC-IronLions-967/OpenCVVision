#include <cstdio>
#include <string>
#include <thread>
#include <vector>

#include <networktables/NetworkTableInstance.h>
#include <vision/VisionPipeline.h>
#include <vision/VisionRunner.h>
#include <wpi/StringRef.h>
#include <wpi/json.h>
#include <wpi/raw_istream.h>
#include <wpi/raw_ostream.h>

#include "cameraserver/CameraServer.h"

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/imgcodecs/imgcodecs.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>

using namespace std;
using namespace cv;

class MyPipeline: public frc::VisionPipeline {

public:

// static int val;
double height = 0.0;
double width = 0.0;
// static Mat drawing;

void Process(Mat& mat) {
    val++;
    
    cvtColor(mat, mat, COLOR_RGB2GRAY);

    blur(mat, mat, Size(3, 3));

    threshold(mat, mat, 150, 255, 0);

    Canny(mat, mat, 50, 200, 3, false);

    vector<vector<Point>> contours;
    vector<Vec4i> hierarchy;

    findContours(mat, contours, RETR_TREE, CHAIN_APPROX_SIMPLE);

    vector<vector<Point>> contours_poly(contours.size());
    vector<Rect> boundRects(contours.size());

    for(int i = 0; i < contours.size(); i++) {
        approxPolyDP(contours[i], contours_poly[i], 3, true);
        boundRects[i] = boundingRect(contours_poly[i]);
    }

    drawing = Mat::zeros(mat.size(), CV_8UC3);

    for(int i = 0; i < contours.size(); i++) {
        drawContours(drawing, contours, i, Scalar(255, 0, 0), 2, 8, hierarchy, 0, Point());
    }

    if(boundRects.size() > 0) {
        int maxIndex = 0;
        for(int i = 0; i < boundRects.size(); i++) {
            maxIndex = (boundRects[i].area() > boundRects[maxIndex].area()) ? i : maxIndex;
        }
        rectangle(drawing, boundRects[maxIndex].tl(), boundRects[maxIndex].br(), Scalar(0, 0, 255));
        height = boundRects[maxIndex].height;
        width = boundRects[maxIndex].width;
    }
}
};