package org.firstinspires.ftc.teamcode;
/*
 * Copyright (c) 2023 FIRST
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import android.graphics.Canvas;
import android.util.Log;

import com.qualcomm.robotcore.util.MovingStatistics;
import com.qualcomm.robotcore.util.RobotLog;

import org.checkerframework.framework.qual.ImplicitFor;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class VisionEyesImpl extends VisionEyesProcessor
{
    public static final String TAG = "AprilTagProcessorImpl";

    private Mat threshholdMat = new Mat();
    private Mat hsvMat = new Mat();
    private Mat channelMat = new Mat();

    private final Object detectionsUpdateSync = new Object();


    private Mat cameraMatrix;

    private double fx;
    private double fy;
    private double cx;
    private double cy;


    public VisionEyesImpl(double fx, double fy, double cx, double cy, int threads)
    {
        this.fx = fx;
        this.fy = fy;
        this.cx = cx;
        this.cy = cy;

    }

    @Override
    protected void finalize()
    {

    }

    @Override
    public void init(int width, int height, CameraCalibration calibration)
    {
        // If the user didn't give us a calibration, but we have one built in,
        // then go ahead and use it!!
        if (calibration != null && fx == 0 && fy == 0 && cx == 0 && cy == 0
                && !(calibration.focalLengthX == 0 && calibration.focalLengthY == 0 && calibration.principalPointX == 0 && calibration.principalPointY == 0)) // needed because we may get an all zero calibration to indicate none, instead of null
        {
            fx = calibration.focalLengthX;
            fy = calibration.focalLengthY;
            cx = calibration.principalPointX;
            cy = calibration.principalPointY;

            Log.d(TAG, String.format("User did not provide a camera calibration; but we DO have a built in calibration we can use.\n [%dx%d] (may be scaled) %s\nfx=%7.3f fy=%7.3f cx=%7.3f cy=%7.3f",
                                     calibration.getSize().getWidth(), calibration.getSize().getHeight(), calibration.getIdentity().toString(), fx, fy, cx, cy));
        }
        else if (fx == 0 && fy == 0 && cx == 0 && cy == 0)
        {
            // set it to *something* so we don't crash the native code

            String warning = "User did not provide a camera calibration, nor was a built-in calibration found for this camera; 6DOF pose data will likely be inaccurate.";
            Log.d(TAG, warning);
            RobotLog.addGlobalWarningMessage(warning);

            fx = 578.272;
            fy = 578.272;
            cx = width/2;
            cy = height/2;
        }
        else
        {
            Log.d(TAG, String.format("User provided their own camera calibration fx=%7.3f fy=%7.3f cx=%7.3f cy=%7.3f",
                                     fx, fy, cx, cy));
        }

        constructMatrix();

    }

    @Override
    public Object processFrame(Mat input, long captureTimeNanos)
    {
        // Convert to greyscale
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);
        Core.extractChannel( hsvMat, channelMat, 1 );
        Imgproc.threshold(channelMat, threshholdMat, 190, 255, Imgproc.THRESH_BINARY );

        // TODO do we need to deep copy this so the user can't mess with it before use in onDrawFrame()?
        return threshholdMat;
    }

    private MovingStatistics solveTime = new MovingStatistics(50);


    private final Object drawSync = new Object();

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext)
    {
        // Only one draw operation at a time thank you very much.
        // (we could be called from two different threads - viewport or camera stream)
        synchronized (drawSync)
        {

        }
    }

    void constructMatrix()
    {
        //     Construct the camera matrix.
        //
        //      --         --
        //     | fx   0   cx |
        //     | 0    fy  cy |
        //     | 0    0   1  |
        //      --         --
        //

        cameraMatrix = new Mat(3,3, CvType.CV_32FC1);

        cameraMatrix.put(0,0, fx);
        cameraMatrix.put(0,1,0);
        cameraMatrix.put(0,2, cx);

        cameraMatrix.put(1,0,0);
        cameraMatrix.put(1,1,fy);
        cameraMatrix.put(1,2,cy);

        cameraMatrix.put(2, 0, 0);
        cameraMatrix.put(2,1,0);
        cameraMatrix.put(2,2,1);
    }

}