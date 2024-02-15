package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.util.Range.clip;
import static org.firstinspires.ftc.teamcode.Robot.Delivery.TWIST_INIT;
import static org.firstinspires.ftc.teamcode.Robot.Intake.Positions.DOWN_TO_PIXEL;
import static org.firstinspires.ftc.teamcode.Robot.Intake.Positions.TRANSFER;
import static org.firstinspires.ftc.teamcode.Robot.Intake.Positions.WAIT_TO_INTAKE;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.A;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.B;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.BACK;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.DPAD_DOWN;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.DPAD_LEFT;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.DPAD_RIGHT;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.DPAD_UP;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.LEFT_BUMPER;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.RIGHT_BUMPER;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.RIGHT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.X;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.Y;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.LEFT_STICK_BUTTON;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Trigger.LEFT_TRIGGER;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Trigger.RIGHT_TRIGGER;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.LinearSlide;
import org.firstinspires.ftc.teamcode.Robot.TBDGamepad;
import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    public static double WRIST_INCREMENT = 0.025;
    public static double WRIST_POSITION = Delivery.WRIST_INIT;
    public static double TWIST_INCREMENT = 0.01;
    public static double TWIST_POSITION = TWIST_INIT;
    public static double ELBOW_INCREMENT = 0.0035;
    public static double ELBOW_POSITION = Delivery.ELBOW_INIT;
    public static double INTAKE_INCREMENT = 0.01;
    public static double INTAKE_POSITION = Intake.INTAKEELBOW_INIT;
    Thunderbot2023 robot = new Thunderbot2023();

    boolean toggle = false;

    TBDGamepad tbdGamepad1;
    TBDGamepad tbdGamepad2;

    @Override
    public void init()
    {
        tbdGamepad1 = new TBDGamepad(gamepad1);
        tbdGamepad2 = new TBDGamepad(gamepad2);
        telemetry.addData("Init", "Start");
        robot.init(hardwareMap, telemetry, false, false);
        telemetry.addData("Init", "Done");
    }

    /**
     * User-defined init_loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the init button is pressed and when the play button is pressed (or the
     * OpMode is stopped).
     * <p>
     * This method is optional. By default, this method takes no action.
     */
    @Override
    public void init_loop()
    {
        super.init_loop();

        // TODO: Can use this to test the vision processing to find the team prop.  DELETE WHEN HAPPY.
        String spikePos = robot.getSpikePos();
        telemetry.addData("Spike Pos = ", spikePos);
        telemetry.addData("Prop X:", robot.getPropX());
        telemetry.addData("Prop Y:",  robot.getPropY());

    }

    @Override
    public void start() {}

    public void loop()
    {
        ////////////////////////////////////////
        // UPDATE robot, gamepads, and timers
        ////////////////////////////////////////

        robot.update();
        tbdGamepad1.update();
        tbdGamepad2.update();
        telemetry.addData("Timer:", getRuntime());

        //////////////
        // DRIVING
        //////////////

        // Resets the measured angle of the robot
//        if(tbdGamepad1.getButton(LEFT_STICK_BUTTON) && tbdGamepad1.getButton(RIGHT_STICK_BUTTON)) {
//            robot.imu.resetYaw();
//            telemetry.addData("imu: ", "reset");
//        }

        if ( robot.intake.driveSlowly()) {
//            if (robot.intake.intakeElbowPos > 0.185) {
            robot.joystickDrive(tbdGamepad1.getLeftY() * 0.2, tbdGamepad1.getLeftX() * 0.2,
                    tbdGamepad1.getRightX() * 0.1);
//        } else if(tbdGamepad1.getTrigger(LEFT_TRIGGER) > 0.1) {
//            robot.joystickDrive(tbdGamepad1.getLeftY(), tbdGamepad1.getLeftX(),
//                    tbdGamepad1.getRightX());
        } else if (tbdGamepad1.getButton(RIGHT_BUMPER)) {
            robot.joystickDrive(tbdGamepad1.getLeftY() * 0.25, tbdGamepad1.getLeftX() * 0.25,
                    tbdGamepad1.getRightX() * 0.25);
        } else {
            robot.joystickDrive(tbdGamepad1.getLeftY() * 0.9, tbdGamepad1.getLeftX() * 0.9,
                    tbdGamepad1.getRightX() * 0.9);
        }

        //////////////////////
        // INTAKE UP & DOWN
        //////////////////////
        if (tbdGamepad1.getTrigger(LEFT_TRIGGER) > 0.1 ) {
           robot.intake.goTo(WAIT_TO_INTAKE, false);
        } else if (tbdGamepad1.getTrigger(RIGHT_TRIGGER) > 0.1) {
            robot.intake.goTo(TRANSFER, false);
            robot.delivery.setElbowPosition(0.92);
        } else if (tbdGamepad1.getButtonPressed(B)) {
             robot.intake.goTo( DOWN_TO_PIXEL, false);
             robot.intake.mandibleOpen();
        }


        ////////////////////
        // INTAKE GRIPPER
        ////////////////////
        if (tbdGamepad1.getButtonPressed(X)) {
            robot.intake.toggleGripper();

        }

        ////////////////////
        // MANDIBLE
        ////////////////////

        if (tbdGamepad1.getButtonPressed(LEFT_BUMPER)) {
            robot.intake.leftMandibleToggle();
        }
        //////////////////////////////////////////////
        // GAMEPAD 1 ENDGAME
        //////////////////////////////////////////////

        ////////////////////
        // PULL-UP
        ////////////////////
        if (tbdGamepad1.getButton(BACK) || getRuntime() > 90)
        {
            if (tbdGamepad1.getButton(Y)) {
                robot.endGame.pullUp(1);
            } else if (tbdGamepad1.getButton(A)) {
                robot.endGame.pullUp(-1);
            } else {
                robot.endGame.pullUp(0);
            }
        }

        if (robot.notifyDriver1()) { tbdGamepad1.notifyDriver( 1); }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////// GAMEPAD 2 //////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////
        // BACK TO INIT
        //////////////////
        if (tbdGamepad2.getButton(LEFT_STICK_BUTTON)) {
            robot.delivery.goTo(Delivery.Positions.ALIGN_FOR_TRANSFER);
            ELBOW_POSITION = 0.92;
            robot.linearSlide.goToLinear(LinearSlide.Positions.LEVEL_0);
        }
        else if (tbdGamepad2.getButton(RIGHT_STICK_BUTTON)) {
            robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
            ELBOW_POSITION = 0.8;
            robot.linearSlide.goToLinear(LinearSlide.Positions.LEVEL_1);
        }


        //////////////
        // LINEAR SLIDES
        //////////////
        if (tbdGamepad2.getButtonPressed(Y)) {
            robot.linearSlide.slideTogglePositionsUp();
        }

//        if (tbdGamepad2.getButtonPressed(Y)) {
//            robot.linearSlide.toggleUp(12);
//        } else if (tbdGamepad2.getButtonPressed(A)) {
//            robot.linearSlide.toggleDown(12, false);
//        }
//
//        if (tbdGamepad2.getButton(BACK) && tbdGamepad2.getButton(A)) {
//            robot.linearSlide.toggleDown(12,  true);
//        } else if (tbdGamepad2.getButton(BACK) && tbdGamepad2.getButtonPressed(Y)) {
//            robot.linearSlide.toggleUp(6);
//        }

        //////////////////
        // DELIVERY ELBOW
        //////////////////
        if ( robot.intake.clearedTransferZone()) {
            if (tbdGamepad2.getButton(X) ) {
                ELBOW_POSITION += ELBOW_INCREMENT;
                ELBOW_POSITION = robot.delivery.setElbowPosition(ELBOW_POSITION);
            } else if (tbdGamepad2.getButton(B) ) {
                ELBOW_POSITION -= ELBOW_INCREMENT;
                ELBOW_POSITION = robot.delivery.setElbowPosition(ELBOW_POSITION);
            }
        }

        //////////////////
        // DELIVERY WRIST
        //////////////////

        if ( robot.delivery.clearedTransferZone())
        {
            if (tbdGamepad2.getButtonPressed(DPAD_UP)) {
                WRIST_POSITION += WRIST_INCREMENT;
                WRIST_POSITION = robot.delivery.setWristPosition(WRIST_POSITION);
                robot.delivery.toggleUp();
            } else if (tbdGamepad2.getButtonPressed(DPAD_DOWN)) {
                WRIST_POSITION -= WRIST_INCREMENT;
                WRIST_POSITION = robot.delivery.setWristPosition(WRIST_POSITION);
                robot.delivery.toggleDown();
            }
        }
        else {
            robot.delivery.setWristPosition(Delivery.WRIST_INIT);
        }

//        if (robot.delivery.lElbowPos > 0.45) {
//            robot.delivery.setWristPosition(robot.delivery.WRIST_INIT);
//        } else {
//            if (tbdGamepad2.getButtonPressed(DPAD_UP)) {
//                WRIST_POSITION += WRIST_INCREMENT;
//            WRIST_POSITION = robot.delivery.setWristPosition(WRIST_POSITION);
//                robot.delivery.toggleUp();
//            } else if (tbdGamepad2.getButtonPressed(DPAD_DOWN)) {
//                WRIST_POSITION -= WRIST_INCREMENT;
//                WRIST_POSITION = robot.delivery.setWristPosition(WRIST_POSITION);
//                robot.delivery.toggleDown();
//            }
//        }

        //////////////////
        // TWIST
        //////////////////
//        if (tbdGamepad2.getTrigger(RIGHT_TRIGGER) > 0.1) {
//            ELBOW_POSITION = robot.delivery.setElbowPosition(0.775);
//            robot.delivery.setWristPosition(0.525);
//            robot.delivery.setTwistPos(0.3);
//        } else if (tbdGamepad2.getTrigger(LEFT_TRIGGER) > 0.1) {
//            ELBOW_POSITION = robot.delivery.setElbowPosition(0.775);
//            robot.delivery.setWristPosition(0.525);
//            robot.delivery.setTwistPos(0.7);
//        } else if (tbdGamepad2.getButtonPressed(DPAD_DOWN)) {
//            robot.delivery.setTwistPos(0.5);
//        }

        ////////////////////
        // DELIVERY GRIPPER
        ////////////////////

        if (tbdGamepad2.getTriggerPressed(LEFT_TRIGGER)) {
            robot.delivery.toggleGrippersLeft();
        } else if (tbdGamepad2.getTriggerPressed(RIGHT_TRIGGER)) {
            robot.delivery.toggleGripperRight();
        }
//        if (robot.notifyDriver2()) { tbdGamepad2.notifyDriver( 1); }

    }
}
