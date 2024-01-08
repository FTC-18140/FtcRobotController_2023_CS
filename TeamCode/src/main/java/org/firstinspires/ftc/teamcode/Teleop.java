package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.util.Range.clip;
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
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Stick.LEFT_X;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Stick.LEFT_Y;
import static org.firstinspires.ftc.teamcode.Robot.TBDGamepad.Stick.RIGHT_X;
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

    public static double WRIST_INCREMENT = 0.01;
    public static double WRIST_POSITION = Delivery.WRIST_INIT;
    public static double TWIST_INCREMENT = 0.001;
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

        //////////////
        // DRIVING
        //////////////
        if (tbdGamepad1.getButton(A)) {
            toggle = true;
        } else if (tbdGamepad1.getButton(B)) {
            toggle = false;
        }

        telemetry.addData("test toggle = ", toggle);

        // Resets the measured angle of the robot
        if(tbdGamepad1.getButton(LEFT_STICK_BUTTON) && tbdGamepad1.getButton(RIGHT_STICK_BUTTON))
        {
            robot.imu.resetYaw();
            telemetry.addData("imu: ", "reset");
        }

        // Activates the oriented drive that changes the direction based on the robots movement
        // Each part of the if then statement
        if (tbdGamepad1.getTrigger(LEFT_TRIGGER) > 0.5)
        {
            // TODO: Test Field Centric Drive
            if ( tbdGamepad1.getTrigger(RIGHT_TRIGGER) > 0.5)
            {
                robot.orientedDrive(tbdGamepad1.getExpo(LEFT_Y) * 0.25, tbdGamepad1.getExpo(LEFT_X) * 0.25,
                                    tbdGamepad1.getExpo(RIGHT_X) * 0.25);
            }
            else
            {
                robot.orientedDrive(tbdGamepad1.getExpo(LEFT_Y) * 0.9, tbdGamepad1.getExpo(LEFT_X) * 0.9,
                                    tbdGamepad1.getExpo(RIGHT_X) * 0.9);
            }
        }

        else // TODO: Test EXPO.  Try different values of the variable called expoYValue and expoXValue in TBDGamepad
        {
            robot.joystickDrive(tbdGamepad1.getExpo(LEFT_Y) * 0.9, tbdGamepad1.getExpo(LEFT_X) * 0.9,
                                tbdGamepad1.getExpo(RIGHT_X) * 0.9);
//            robot.joystickDrive(tbdGamepad1.getLeftY() * 0.9, tbdGamepad1.getLeftX() * 0.9,
//                                tbdGamepad1.getLeftX() * 0.9);
        }

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
        if (tbdGamepad1.getButton(LEFT_BUMPER) ) {
//           robot.intake.setElbowPos(0);
           robot.intake.setElbowPos(0.185);
        } else if (tbdGamepad1.getButton(RIGHT_BUMPER)) {
//            robot.intake.setElbowPos(0.45);
            robot.intake.setElbowPos(0);
        } else if (tbdGamepad1.getButton(DPAD_DOWN)) {
            robot.intake.setElbowPos(0.25);
        }
//        if (tbdGamepad1.getButton(LEFT_BUMPER)) {
//            robot.intake.setElbowPos(0);
//            robot.intake.setElbowPos(0.145);
//        } else if (tbdGamepad1.getButton(RIGHT_BUMPER)) {
//            robot.intake.setElbowPos(0.08);
//            robot.intake.setElbowPos(0.225);
//        }

        ////////////////////
        // INTAKE GRIPPER
        ////////////////////

        if (tbdGamepad1.getButton(X)) {
            robot.intake.dropBoth();
        } else if (tbdGamepad1.getButton(Y)) {
            robot.intake.holdPixelsBoth();
        }

        ////////////////////
        // DRONE LAUNCHER
        ////////////////////

        // TODO: Add a timer so that this cannot be activated before End Game
        if (tbdGamepad1.getButton(LEFT_BUMPER) && tbdGamepad1.getButton(RIGHT_BUMPER) && tbdGamepad1.getButton(X))
        {
            robot.droneLauncher.launcherPower(1);
        }
        else
        {
            robot.droneLauncher.launcherPower(0);
        }
        if (tbdGamepad1.getButton(LEFT_BUMPER) && tbdGamepad1.getButton(RIGHT_BUMPER) && tbdGamepad1.getButton(Y)) {

        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////// GAMEPAD 2 //////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////
        // DELIVERY ELBOW
        //////////////////
        if (tbdGamepad2.getButton(B)) {
            ELBOW_POSITION += ELBOW_INCREMENT;
            ELBOW_POSITION = clip(ELBOW_POSITION, robot.delivery.ELBOW_MIN, robot.delivery.ELBOW_MAX);
            robot.delivery.setElbowPosition(ELBOW_POSITION);
        } else if (tbdGamepad2.getButton(X)) {
            ELBOW_POSITION -= ELBOW_INCREMENT;
            ELBOW_POSITION = clip(ELBOW_POSITION, robot.delivery.ELBOW_MIN, robot.delivery.ELBOW_MAX);
            robot.delivery.setElbowPosition(ELBOW_POSITION);
//            robot.delivery.setlElbowPos(0.04);
//            robot.delivery.setrElbowPos(0.04);
        }

        //////////////////
        // DELIVERY WRIST
        //////////////////

        // TODO: Use Presets and possibly goTo method
//        if (tbdGamepad2.getButton(DPAD_UP)) {
//            robot.delivery.setWristPos(0);
//        } else if (tbdGamepad2.getButton(DPAD_DOWN)) {
//            robot.delivery.setWristPos(1);
//        }

        if (tbdGamepad2.getButton(DPAD_UP)) {
            WRIST_POSITION += WRIST_INCREMENT;
            WRIST_POSITION = clip(WRIST_POSITION, 0, 1);
            robot.delivery.setWristPos(WRIST_POSITION);
        } else if (tbdGamepad2.getButton(DPAD_DOWN)) {
            WRIST_POSITION -= WRIST_INCREMENT;
            WRIST_POSITION = clip(WRIST_POSITION, 0, 1);
            robot.delivery.setWristPos(WRIST_POSITION);
        }

        //////////////////
        // TWIST
        //////////////////

        if (tbdGamepad2.getButton(DPAD_RIGHT)) {
            robot.delivery.setTwistPos(0);
        } else if (tbdGamepad2.getButton(DPAD_LEFT)) {
            robot.delivery.setTwistPos(1);
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

    }
}
