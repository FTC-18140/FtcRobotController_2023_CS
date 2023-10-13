//package org.firstinspires.ftc.teamcode;
//
//import android.graphics.Canvas;
//
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
//import org.firstinspires.ftc.vision.VisionProcessor;
//import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//import org.opencv.calib3d.Calib3d;
//import org.opencv.core.Mat;
//
//
//public abstract class VisionEyesProcessor implements VisionProcessor
//{
//    public static final int THREADS_DEFAULT = 3;
//
//
//    public static class VisionBuilder
//    {
//        private double fx, fy, cx, cy;
//
//        private DistanceUnit outputUnitsLength = DistanceUnit.INCH;
//        private AngleUnit outputUnitsAngle = AngleUnit.DEGREES;
//        private int threads = THREADS_DEFAULT;
//
//
//        /**
//         * Set the camera calibration parameters (needed for accurate 6DOF pose unless the
//         * SDK has a built in calibration for your camera)
//         * @param fx see opencv 8 parameter camera model
//         * @param fy see opencv 8 parameter camera model
//         * @param cx see opencv 8 parameter camera model
//         * @param cy see opencv 8 parameter camera model
//         * @return the {@link VisionBuilder} object, to allow for method chaining
//         */
//        public VisionBuilder setLensIntrinsics(double fx, double fy, double cx, double cy)
//        {
//            this.fx = fx;
//            this.fy = fy;
//            this.cx = cx;
//            this.cy = cy;
//            return this;
//        }
//
//
//        /**
//         * Set the number of threads the tag detector should use
//         * @param threads the number of threads the tag detector should use
//         * @return the {@link VisionBuilder} object, to allow for method chaining
//         */
//        public VisionBuilder setNumThreads(int threads)
//        {
//            this.threads = threads;
//            return this;
//        }
//
//        /**
//         * Create a {@link VisionProcessor} object which may be attached to
//         * a {@link org.firstinspires.ftc.vision.VisionPortal} using
//         * {@link org.firstinspires.ftc.vision.VisionPortal.Builder#addProcessor(VisionProcessor)}
//         * @return a {@link VisionProcessor} object
//         */
//        public VisionProcessor build()
//        {
//            return new VisionEyesImpl( fx, fy, cx, cy, threads );
//
//        }
//    }
//
//
//
//
//}
//
