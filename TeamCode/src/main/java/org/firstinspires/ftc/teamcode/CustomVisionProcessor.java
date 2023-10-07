package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;

import org.checkerframework.framework.qual.ImplicitFor;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CustomVisionProcessor implements VisionProcessor
{
    Mat smallMat = new Mat();
    Mat threshMatLow = new Mat();
    Mat threshMatHigh = new Mat();
    Mat threshMatTot = new Mat();
    Mat erodeMat = new Mat();
    Mat dilateMat = new Mat();
    Mat laplacianMat = new Mat();

    Scalar lowerBoundLow = new Scalar(0, 200, 30);
    Scalar upperBoundLow = new Scalar(5, 255, 255);
    Scalar lowerBoundHigh = new Scalar(170, 200, 30);
    Scalar upperBoundHigh = new Scalar(180, 255, 255);

    Size kernelSize = new Size(1, 1);


    Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kernelSize);

    @Override
    public void init(int width, int height, CameraCalibration calibration)
    {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos)
    {
        Imgproc.cvtColor(frame, smallMat, Imgproc.COLOR_RGB2HSV);
        Core.inRange(smallMat, lowerBoundLow, upperBoundLow, threshMatLow);
        Core.inRange(smallMat, lowerBoundHigh, upperBoundHigh, threshMatHigh);
        Core.add(threshMatHigh, threshMatLow, threshMatTot);
        Imgproc.erode( threshMatTot, erodeMat, kernel);
        Imgproc.dilate( erodeMat, dilateMat, kernel);
        Imgproc.Laplacian(dilateMat, laplacianMat, 1);

        return laplacianMat;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext)
    {

    }

    public static class Builder {
        public CustomVisionProcessor build() {
            return new CustomVisionProcessor();
        }
    }
}
