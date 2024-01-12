package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.util.Range.clip;
import static org.firstinspires.ftc.teamcode.Robot.Intake.Positions.DOWN_TO_PIXEL;
import static org.firstinspires.ftc.teamcode.Robot.Intake.Positions.TRANSFER;
import static org.firstinspires.ftc.teamcode.Robot.Intake.Positions.WAIT_TO_INTAKE;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.A;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Button.B;
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

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.TBDGamepad;
import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    public static double WRIST_INCREMENT = 0.025;
    public static double WRIST_POSITION = Delivery.WRIST_INIT;
    public static double TWIST_INCREMENT = 0.01;
    public static double TWIST_POSITION = Delivery.TWIST_INIT;
    public static double ELBOW_INCREMENT = 0.0025;
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
        robot.init(hardwareMap, telemetry, false);
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
        // TODO: test the ability to detect the team prop and find the LEFT/CENTER/RIGHT

        String spikePos = robot.getSpikePos();
        telemetry.addData("Spike Pos = ", spikePos);
        telemetry.addData("Prop X:", robot.getPropX());
        telemetry.addData("Prop Y:",  robot.getPropY());

    }

    @Override
    public void start() {}

    public void loop()
    {
        robot.update();
        tbdGamepad1.update();
        tbdGamepad2.update();
        resetRuntime();

        //////////////
        // DRIVING
        //////////////

        // Resets the measured angle of the robot
        if(tbdGamepad1.getButton(LEFT_STICK_BUTTON) && tbdGamepad1.getButton(RIGHT_STICK_BUTTON))
        {
            robot.imu.resetYaw();
            telemetry.addData("imu: ", "reset");
        }

        // Activates the oriented drive that changes the direction based on the robots movement
        // Each part of the if then statement
//        if (tbdGamepad1.getTrigger(LEFT_TRIGGER) > 0.5)
//        {
//            // TODO: Test Field Centric Drive
//            if ( tbdGamepad1.getTrigger(RIGHT_TRIGGER) > 0.5)
//            {
//                robot.orientedDrive(tbdGamepad1.getExpo(LEFT_Y) * 0.25, tbdGamepad1.getExpo(LEFT_X) * 0.25,
//                                    tbdGamepad1.getExpo(RIGHT_X) * 0.25);
//            }
//            else
//            {
//                robot.orientedDrive(tbdGamepad1.getExpo(LEFT_Y) * 0.9, tbdGamepad1.getExpo(LEFT_X) * 0.9,
//                                    tbdGamepad1.getExpo(RIGHT_X) * 0.9);
//            }
//        }
//
//        else // TODO: Test EXPO.  Try different values of the variable called expoYValue and expoXValue in TBDGamepad
//        {
//            if ( robot.intake.driveSlowly()) {
            if (robot.intake.intakeElbowPos > 0.185) {
                robot.joystickDrive(tbdGamepad1.getLeftY() * 0.2, tbdGamepad1.getLeftX() * 0.2,
                        tbdGamepad1.getRightX() * 0.1);
            } else if(tbdGamepad1.getTrigger(LEFT_TRIGGER) > 0.1) {
                robot.joystickDrive(tbdGamepad1.getLeftY(), tbdGamepad1.getLeftX(),
                        tbdGamepad1.getRightX());
            } else if (tbdGamepad1.getTrigger(RIGHT_TRIGGER) > 0.1) {
                robot.joystickDrive(tbdGamepad1.getLeftY() * 0.25, tbdGamepad1.getLeftX() * 0.25,
                        tbdGamepad1.getRightX() * 0.25);
            } else {
                robot.joystickDrive(tbdGamepad1.getLeftY() * 0.9, tbdGamepad1.getLeftX() * 0.9,
                        tbdGamepad1.getRightX() * 0.9);
            }
//            robot.joystickDrive(tbdGamepad1.getLeftY() * 0.9, tbdGamepad1.getLeftX() * 0.9,
//                                tbdGamepad1.getLeftX() * 0.9);
        //}

//        if (gamepad1.left_trigger > 0.5) {
//            if (gamepad1.right_trigger > 0.5) {
//                //TURBO
//                robot.orientedDrive(-gamepad1.left_stick_y * 0.9, -gamepad1.left_stick_x * 0.9, gamepad1.right_stick_x);
//            } else if (gamepad1.left_trigger > 0.5) {
//                //ACCURATE
//                robot.orientedDrive(-gamepad1.left_stick_y * 0.2, -gamepad1.left_stick_x * 0.2, gamepad1.right_stick_x);
//            } else {
//                //NORMAL
//                robot.orientedDrive(-gamepad1.left_stick_y * 0.6, -gamepad1.left_stick_x * 0.6, gamepad1.right_stick_x);
//            }
//        } else {
//            robot.joystickDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
//        }

        //////////////////////
        // INTAKE UP & DOWN
        //////////////////////

//         TODO: presets or use goTo method
        if (tbdGamepad1.getButtonPressed(LEFT_BUMPER) ) {
           robot.intake.goTo(WAIT_TO_INTAKE, false);
           // TRY THIS
         //   robot.intake.toggleDown();
        } else if (tbdGamepad1.getButtonPressed(RIGHT_BUMPER)) {
          //  robot.intake.setElbowPos(0);
            robot.intake.goTo(TRANSFER, false);
            // TRY THIS
         //    robot.intake.toggleUp();
        } else if (tbdGamepad1.getButtonPressed(DPAD_DOWN)) {
//            robot.intake.setElbowPos(0.25);
            // TRY THIS
             robot.intake.goTo( DOWN_TO_PIXEL, false);
        }


        ////////////////////
        // INTAKE GRIPPER
        ////////////////////

//        if (tbdGamepad1.getButtonPressed(X)) {
//            robot.intake.dropBoth();
//        } else if (tbdGamepad1.getButtonPressed(Y)) {
//            robot.intake.holdPixelsBoth();
//        }

        // TRY THIS
        if (tbdGamepad1.getButtonPressed(X))
        {
            robot.intake.toggleGripper();
        }

        //////////////////////////////////////////////
        // GAMEPAD 1 ENDGAME
        //////////////////////////////////////////////

        ////////////////////
        // PULL-UP
        ////////////////////
        if (tbdGamepad1.getButton(LEFT_BUMPER) && tbdGamepad1.getButton(RIGHT_BUMPER) && tbdGamepad1.getButton(Y)) {
            robot.endGame.pullUp(1);
        } else if (tbdGamepad1.getButton(LEFT_BUMPER) && tbdGamepad1.getButton(RIGHT_BUMPER) && tbdGamepad1.getButton(A)) {
            robot.endGame.pullUp(-1);
        } else {
            robot.endGame.pullUp(0);
        }
        ////////////////////
        // DRONE LAUNCHER
        ////////////////////

        // TODO: Add a timer so that this cannot be activated before End Game
        if (tbdGamepad1.getButton(LEFT_BUMPER) && tbdGamepad1.getButton(RIGHT_BUMPER) && tbdGamepad1.getButton(X)) {
            robot.endGame.launcherPower(1);
        } else {
            robot.endGame.launcherPower(0);
        }


        if (robot.notifyDriver1()) { tbdGamepad1.notifyDriver( 1); }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////// GAMEPAD 2 //////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////
        // DELIVERY ELBOW
        //////////////////
        if ( robot.intake.clearedTransferZone())
        {
            if (tbdGamepad2.getButton(X) )
            {
//        if (tbdGamepad2.getButton(B) && robot.intake.intakeElbowPos > 0.05) {
                ELBOW_POSITION += ELBOW_INCREMENT;
                ELBOW_POSITION = robot.delivery.setElbowPosition(ELBOW_POSITION);
            }
            else if (tbdGamepad2.getButton(B) )
            {
//        } else if (tbdGamepad2.getButton(X) && robot.intake.intakeElbowPos > 0.05) {
                ELBOW_POSITION -= ELBOW_INCREMENT;
                ELBOW_POSITION = robot.delivery.setElbowPosition(ELBOW_POSITION);
//            robot.delivery.setlElbowPos(0.04);
//            robot.delivery.setrElbowPos(0.04);
            }
        }

        //////////////////
        // DELIVERY WRIST
        //////////////////

        // TODO: Use Presets and possibly goTo method

        if (tbdGamepad2.getButtonPressed(DPAD_UP)) {
            WRIST_POSITION += WRIST_INCREMENT;
//            WRIST_POSITION = robot.delivery.setWristPos(WRIST_POSITION);
            robot.delivery.toggleUp();
        } else if (tbdGamepad2.getButtonPressed(DPAD_DOWN)) {
            WRIST_POSITION -= WRIST_INCREMENT;
//            WRIST_POSITION = robot.delivery.setWristPos(WRIST_POSITION);
            robot.delivery.toggleDown();
        }

        //////////////////
        // TWIST
        //////////////////

        if (tbdGamepad2.getButtonPressed(DPAD_RIGHT)) {
//            robot.delivery.setTwistPos(0);
            robot.delivery.toggleTwistCW();
        } else if (tbdGamepad2.getButtonPressed(DPAD_LEFT)) {
//            robot.delivery.setTwistPos(0.5);
            robot.delivery.toggleTwistCCW();
        }

        //////////////
        // LIFT
        //////////////
        // One needs to be reversed
        if (tbdGamepad2.getButton(Y)) {
            robot.linearSlide.linearMove(1);
        } else if (tbdGamepad2.getButton(A)) {
            robot.linearSlide.linearMove(-1);
        } else {
            robot.linearSlide.linearMove(0);
        }

        ////////////////////
        // DELIVERY GRIPPER
        ////////////////////

        if (tbdGamepad2.getTrigger(LEFT_TRIGGER) > 0) {
            robot.delivery.dropBoth();
        } else if (tbdGamepad2.getTrigger(RIGHT_TRIGGER) > 0) {
            robot.delivery.holdPixelsBoth();
        }
        if (robot.notifyDriver2()) { tbdGamepad2.notifyDriver( 1); }

    }
}
