package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;

@Autonomous
@Disabled
public class AutoFeatureTestBackstage extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    int state = 0;
    boolean done = false;
    private int tagNum;

    /**
     * Initialize the AprilTag processor.
     */

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, false, true);

        // Initialize the Apriltag Detection process

//        if (USE_WEBCAM) {
//            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
//        }
    }
    public void init_loop() {
        super.init_loop();

        switch (robot.eyes.getSpikePos())
        {
            case "LEFT":
                tagNum = 4;
                break;
            case "RIGHT":
                tagNum = 6;
                break;
            default: // default CENTER
                tagNum = 5;
                break;
        }
        telemetry.addData("Tag Number: ", tagNum );
        telemetry.addData("Prop X:", robot.eyes.getPropX());
        telemetry.addData("Prop Y:",  robot.eyes.getPropY());
    }
    @Override
    public void start() {
        robot.resetIMUYaw();
    }

    @Override
    public void loop() {
        switch (state) {
            case 0:
                if (!done) {
                    done = robot.gyroDrive(0, 80, 0.6);
                } else {
                    robot.stop();
                    state++;
                }
            case 1:
                if (!done) {
                    done = robot.turnTo(90, 0.3);
                } else {
                    robot.stop();
                    state++;
                }
            case 2:
                if (!done) {
                    done = robot.gyroDrive(90, 40,0.6);
                } else {
                    robot.stop();
                    state++;
                }
            case 3:
                if (!done) {
                    done = robot.driveToTag(tagNum, 0.25, 10);
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

