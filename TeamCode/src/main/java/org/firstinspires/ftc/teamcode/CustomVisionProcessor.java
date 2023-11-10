package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.checkerframework.framework.qual.ImplicitFor;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
import org.opencv.imgproc.Moments;


import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;
import static android.graphics.Typeface.BOLD;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX;

public class CustomVisionProcessor implements VisionProcessor
{

    //Outputs
    private Mat cvCvtcolorOutput = new Mat();
    private Mat cvExtractchannelOutput = new Mat();
    private Mat cvThresholdOutput = new Mat();
    private Mat cvDilate0Output = new Mat();
    private Mat cvErodeOutput = new Mat();
    private Mat cvDilate1Output = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    private String spikePos = "LEFT";

    Scalar blue = new Scalar(7,197,235,255);
    Scalar red = new Scalar(255,0,0,255);
    Scalar green = new Scalar(0,255,0,255);
    Scalar white = new Scalar(255,255,255,255);
    Scalar yellow = new Scalar(255, 255, 0);

    @Override
    public void init(int width, int height, CameraCalibration calibration)
    {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos)
    {
        // Step CV_cvtColor0:
        Mat cvCvtcolorSrc = frame;
        int cvCvtcolorCode = Imgproc.COLOR_RGB2YCrCb;
        cvCvtcolor(cvCvtcolorSrc, cvCvtcolorCode, cvCvtcolorOutput);

        // Step CV_extractChannel0:
        Mat cvExtractchannelSrc = cvCvtcolorOutput;
        double cvExtractchannelChannel = 1.0;
        cvExtractchannel(cvExtractchannelSrc, cvExtractchannelChannel, cvExtractchannelOutput);

        // Step CV_Threshold0:
        Mat cvThresholdSrc = cvExtractchannelOutput;
        double cvThresholdThresh = 140.0;
        double cvThresholdMaxval = 255.0;
        int cvThresholdType = Imgproc.THRESH_BINARY;
        cvThreshold(cvThresholdSrc, cvThresholdThresh, cvThresholdMaxval, cvThresholdType, cvThresholdOutput);

        // Step CV_dilate0:
        Mat cvDilate0Src = cvThresholdOutput;
        Mat cvDilate0Kernel = new Mat();
        Point cvDilate0Anchor = new Point(-1, -1);
        double cvDilate0Iterations = 10.0;
        int cvDilate0Bordertype = Core.BORDER_CONSTANT;
        Scalar cvDilate0Bordervalue = new Scalar(-1);
        cvDilate(cvDilate0Src, cvDilate0Kernel, cvDilate0Anchor, cvDilate0Iterations, cvDilate0Bordertype, cvDilate0Bordervalue, cvDilate0Output);

        // Step CV_erode0:
        Mat cvErodeSrc = cvDilate0Output;
        Mat cvErodeKernel = new Mat();
        Point cvErodeAnchor = new Point(-1, -1);
        double cvErodeIterations = 70.0;
        int cvErodeBordertype = Core.BORDER_CONSTANT;
        Scalar cvErodeBordervalue = new Scalar(-1);
        cvErode(cvErodeSrc, cvErodeKernel, cvErodeAnchor, cvErodeIterations, cvErodeBordertype, cvErodeBordervalue, cvErodeOutput);

        // Step CV_dilate1:
        Mat cvDilate1Src = cvErodeOutput;
        Mat cvDilate1Kernel = new Mat();
        Point cvDilate1Anchor = new Point(-1, -1);
        double cvDilate1Iterations = 70.0;
        int cvDilate1Bordertype = Core.BORDER_CONSTANT;
        Scalar cvDilate1Bordervalue = new Scalar(-1);
        cvDilate(cvDilate1Src, cvDilate1Kernel, cvDilate1Anchor, cvDilate1Iterations, cvDilate1Bordertype, cvDilate1Bordervalue, cvDilate1Output);

        // Step Find_Contours0:
        Mat findContoursInput = cvDilate1Output;
        boolean findContoursExternalOnly = false;
        findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

        if ( findContoursOutput.size() > 0 )
        {
            double maxArea = 0;
            int maxIndex = 0;

            for ( int i = 0; i < findContoursOutput.size(); i++ )
            {
                double currentArea = Imgproc.contourArea( findContoursOutput.get(i) );
                if ( currentArea > maxArea)
                {
                    maxArea = currentArea;
                    maxIndex = i;
                }
            }
            Moments moments = Imgproc.moments(findContoursOutput.get(maxIndex));

            double xPos = moments.m10/moments.m00;
            double yPos = moments.m01/moments.m00;

            if (xPos < 200 )
            {
                spikePos = "LEFT";// LEFT
            }
            else if ( xPos > 300)
            {
                spikePos = "RIGHT";   // RIGHT
            }
            else
            {
                spikePos = "CENTER";// CENTER
            }

        }
        else
        {
            spikePos = "CENTER";// CENTER
        }

        return frame;
    }

    public String getSpikePos()
    {
        return spikePos;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext)
    {
        Paint yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(true);
        yellowPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText( spikePos, 10, 450, yellowPaint );
    }

    /**
     * This method is a generated getter for the output of a CV_cvtColor.
     * @return Mat output from CV_cvtColor.
     */
    public Mat cvCvtcolorOutput() {
        return cvCvtcolorOutput;
    }

    /**
     * This method is a generated getter for the output of a CV_extractChannel.
     * @return Mat output from CV_extractChannel.
     */
    public Mat cvExtractchannelOutput() {
        return cvExtractchannelOutput;
    }

    /**
     * This method is a generated getter for the output of a CV_Threshold.
     * @return Mat output from CV_Threshold.
     */
    public Mat cvThresholdOutput() {
        return cvThresholdOutput;
    }

    /**
     * This method is a generated getter for the output of a CV_dilate.
     * @return Mat output from CV_dilate.
     */
    public Mat cvDilate0Output() {
        return cvDilate0Output;
    }

    /**
     * This method is a generated getter for the output of a CV_erode.
     * @return Mat output from CV_erode.
     */
    public Mat cvErodeOutput() {
        return cvErodeOutput;
    }

    /**
     * This method is a generated getter for the output of a CV_dilate.
     * @return Mat output from CV_dilate.
     */
    public Mat cvDilate1Output() {
        return cvDilate1Output;
    }

    /**
     * This method is a generated getter for the output of a Find_Contours.
     * @return ArrayList<MatOfPoint> output from Find_Contours.
     */
    public ArrayList<MatOfPoint> findContoursOutput() {
        return findContoursOutput;
    }


    /**
     * Converts an image from one color space to another.
     * @param src Image to convert.
     * @param code conversion code.
     * @param dst converted Image.
     */
    private void cvCvtcolor(Mat src, int code, Mat dst) {
        Imgproc.cvtColor(src, dst, code);
    }

    /**
     * Extracts given channel from an image.
     * @param src the image to extract.
     * @param channel zero indexed channel number to extract.
     * @param dst output image.
     */
    private void cvExtractchannel(Mat src, double channel, Mat dst) {
        Core.extractChannel(src, dst, (int)channel);
    }

    /**
     * Apply a fixed-level threshold to each array element in an image.
     * @param src Image to threshold.
     * @param threshold threshold value.
     * @param maxVal Maximum value for THRES_BINARY and THRES_BINARY_INV
     * @param type Type of threshold to appy.
     * @param dst output Image.
     */
    private void cvThreshold(Mat src, double threshold, double maxVal, int type,
                             Mat dst) {
        Imgproc.threshold(src, dst, threshold, maxVal, type);
    }

    /**
     * Expands area of lower value in an image.
     * @param src the Image to erode.
     * @param kernel the kernel for erosion.
     * @param anchor the center of the kernel.
     * @param iterations the number of times to perform the erosion.
     * @param borderType pixel extrapolation method.
     * @param borderValue value to be used for a constant border.
     * @param dst Output Image.
     */
    private void cvErode(Mat src, Mat kernel, Point anchor, double iterations,
                         int borderType, Scalar borderValue, Mat dst) {
        if (kernel == null) {
            kernel = new Mat();
        }
        if (anchor == null) {
            anchor = new Point(-1,-1);
        }
        if (borderValue == null) {
            borderValue = new Scalar(-1);
        }
        Imgproc.erode(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
    }

    /**
     * Expands area of higher value in an image.
     * @param src the Image to dilate.
     * @param kernel the kernel for dilation.
     * @param anchor the center of the kernel.
     * @param iterations the number of times to perform the dilation.
     * @param borderType pixel extrapolation method.
     * @param borderValue value to be used for a constant border.
     * @param dst Output Image.
     */
    private void cvDilate(Mat src, Mat kernel, Point anchor, double iterations,
                          int borderType, Scalar borderValue, Mat dst) {
        if (kernel == null) {
            kernel = new Mat();
        }
        if (anchor == null) {
            anchor = new Point(-1,-1);
        }
        if (borderValue == null){
            borderValue = new Scalar(-1);
        }
        Imgproc.dilate(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
    }


    private void findContours(Mat input, boolean externalOnly,
                              List<MatOfPoint> contours) {
        Mat hierarchy = new Mat();
        contours.clear();
        int mode;
        if (externalOnly) {
            mode = Imgproc.RETR_EXTERNAL;
        }
        else {
            mode = Imgproc.RETR_LIST;
        }
        int method = Imgproc.CHAIN_APPROX_SIMPLE;
        Imgproc.findContours(input, contours, hierarchy, mode, method);
    }


    public static class Builder {
        public CustomVisionProcessor build() {
            return new CustomVisionProcessor();
        }
    }
}
