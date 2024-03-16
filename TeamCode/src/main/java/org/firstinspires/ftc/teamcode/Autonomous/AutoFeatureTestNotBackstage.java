package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
@Disabled
public class AutoFeatureTestNotBackstage {

//    @Autonomous
//    public class AutoFeatureTestBackstage extends OpMode {
//
//        Thunderbot2023 robot = new Thunderbot2023();
//        int state = 0;
//        boolean done = false;
//        /**
//         * Initialize the AprilTag processor.
//         */
//
//        @Override
//        public void init() {
//            robot.init(hardwareMap, telemetry, false);
//
//            // Initialize the Apriltag Detection process
//
////        if (USE_WEBCAM) {
////            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
////        }
//        }
//        public void init_loop() {
//            super.init_loop();
//            telemetry.addData("Tag Number: ", robot.eyes.getTagNumber(4) );
//            telemetry.addData("Tag X:", robot.eyes.tgeFinder.xPos);
//            telemetry.addData("Tag Y:",  robot.eyes.tgeFinder.yPos);
//        }
//        @Override
//        public void start() {}
//
//        @Override
//        public void loop() {
//            switch (state) {
//                case 0:
//                    if (!done) {
//                        done = robot.drive(80, 0.6);
//                    } else {
//                        robot.stop();
//                        state++;
//                    }
//                case 1:
//                    if (!done) {
//                        done = robot.turn(90, 0.3);
//                    } else {
//                        robot.stop();
//                        state++;
//                    }
//                case 2:
//                    if (!done) {
//                        done = robot.drive(160, 0.6);
//                    } else {
//                        robot.stop();
//                        state++;
//                    }
//                case 3:
//                    if (!done) {
//                        done = robot.driveToTag(4, 0.25, 10);
//                    } else {
//                        robot.stop();
//                        state++;
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
}
