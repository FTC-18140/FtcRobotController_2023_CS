package org.firstinspires.ftc.teamcode.Robot;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class ArtemisEyes
{
    WebcamName theCamera;
    public TGEVisionProcessor tgeFinder;
    AprilTagProcessor aprilTagFinder;
    VisionPortal thePortal;
    Telemetry telemetry;
    double rangeError;
    double headingError;
    double yawError;
    void init(HardwareMap hardwareMap, Telemetry telem )
    {
        telemetry = telem;
        theCamera = hardwareMap.get( WebcamName.class, "Webcam 1");
        aprilTagFinder = new AprilTagProcessor.Builder().setDrawTagOutline(true).build();
        tgeFinder = new TGEVisionProcessor();
        thePortal = VisionPortal.easyCreateWithDefaults(theCamera, aprilTagFinder, tgeFinder);
        aprilTagFinder.setDecimation(2);
    }

    public String getSpikePos() {
        if (tgeFinder != null) {
            return tgeFinder.getSpikePos();
        } else {
            return "TGEFINDER NOT INITIALIZED";
        }
    }

    @SuppressLint("DefaultLocale")
    public int getTagNumber(int tagNum)
    {
        List<AprilTagDetection> currentDetections = aprilTagFinder.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());
        int tagPos = -1;

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections)
        {
            if (detection.metadata != null && detection.id == tagNum) {
                rangeError = detection.ftcPose.range;
                headingError = detection.ftcPose.bearing;
                yawError = detection.ftcPose.yaw;

                tagPos = detection.id;
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else if (detection.metadata == null) {
                telemetry.addLine(String.format("No Detections Found"));
            }
            else
            {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

        return tagPos;
    }

    public double getPropX() {
        if (tgeFinder != null) {
            return tgeFinder.xPos;
        } else {
            return -1;
        }
    }
    public double getPropY() {
        if (tgeFinder != null) {
            return tgeFinder.yPos;
        } else {
            return -1;
        }
    }

}
