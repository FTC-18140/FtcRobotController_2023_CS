package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous
public class AutoFeatureTest extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    int state = 0;
    boolean done = false;
    /**
     * Initialize the AprilTag processor.
     */

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, false);

        // Initialize the Apriltag Detection process

//        if (USE_WEBCAM) {
//            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
//        }
    }
    public void init_loop() {
        super.init_loop();
        telemetry.addData("Tag Number: ", robot.eyes.getTagNumber(4) );
        telemetry.addData("Tag X:", robot.eyes.tgeFinder.xPos);
        telemetry.addData("Tag Y:",  robot.eyes.tgeFinder.yPos);
    }
    @Override
    public void start() {

    }

    @Override
    public void loop() {
        switch (state) {
            case 0:
                if (!done) {
                    done = robot.driveToTag(4, 0.25, 10);
                } else {
                    robot.stop();
                    state++;
                }
                break;
            default:
                break;
        }

    }
}

