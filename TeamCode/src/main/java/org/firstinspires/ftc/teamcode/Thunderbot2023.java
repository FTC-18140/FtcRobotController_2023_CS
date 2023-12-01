package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.List;


public class Thunderbot2023
{
    // defines all variables
    IMU imu = null;

    DcMotorEx leftFront = null;
    DcMotorEx rightFront = null;
    DcMotorEx leftRear = null;
    DcMotorEx rightRear = null;

    // Position Variables
    long leftFrontPosition = 0;
    long rightFrontPosition = 0;
    long leftRearPosition = 0;
    long rightRearPosition = 0;
    double allMotors = 0;
    double heading = 0;

    double lastAngle = 0;

    boolean moving = false;

    List<LynxModule> allHubs;

    ArtemisEyes eyes = new ArtemisEyes();

    public static double SPEED_GAIN = 0.02;
    public static double STRAFE_GAIN = 0.015;
    public static double  TURN_GAIN = 0.01;
    public static double MAX_SPEED = 0.5;
    public static double MAX_STRAFE = 0.5;
    public static double MAX_TURN = 0.3;


    // converts inches to motor ticks
    static final double COUNTS_PER_MOTOR_REV = 28; // REV HD Hex motor
    static final double DRIVE_GEAR_REDUCTION = 2.89 * 3.61;  // actual gear ratios of the 4:1 and 5:1 UltraPlanetary gear box modules
    static final double WHEEL_DIAMETER_CM = 9.6;  // goBilda mecanum wheels are 96mm in diameter
    static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)
            / (WHEEL_DIAMETER_CM * Math.PI);

    private Telemetry telemetry = null;

    /**
     * Constructor
     */
    public Thunderbot2023() {}

    /**
     * Initializes the Thunderbot and connects its hardware to the HardwareMap
     * @param ahwMap
     * @param telem
     * @param withVision
     */
    public void init(HardwareMap ahwMap, Telemetry telem, boolean withVision)
    {
        telemetry = telem;

        try
        {
            imu = ahwMap.get(IMU.class, "imu");

            RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
            RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

            RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

            imu.initialize(new IMU.Parameters(orientationOnRobot));
            imu.resetYaw();

        }
        catch (Exception p_exeception)
        {
            telemetry.addData("imu not found in config file", 0);
            imu = null;
        }

        try
        {
            allHubs = ahwMap.getAll(LynxModule.class);

            for (LynxModule module : allHubs) {
                module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            }
        }
        catch (Exception e) {
            telemetry.addData("Lynx Module not found", 0);
        }
        // Define & Initialize Motors

        try
        {
            rightFront = ahwMap.get(DcMotorEx.class, "rightFront");
            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
            rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e)
        {
            telemetry.addData("rightFront not found in config file", 0);
        }

        try
        {
            rightRear = ahwMap.get(DcMotorEx.class, "rightRear");
            rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
            rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e)
        {
            telemetry.addData("rightRear not found in config file", 0);
        }

        try
        {
            leftFront = ahwMap.get(DcMotorEx.class, "leftFront");
            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
            leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e)
        {
            telemetry.addData("leftFront not found in config file", 0);
        }

        try
        {
            leftRear = ahwMap.get(DcMotorEx.class, "leftRear");
            leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
            leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        catch (Exception e)
        {
            telemetry.addData("leftRear not found in config file", 0);
        }

        eyes.init( ahwMap, telemetry );

    }

    /**
     * This code go's through the math behind the mecanum wheel drive.  Given the joystick values,
     * it will calculate the motor commands needed for the mecanum drive.
     *
     * @param foward    - Any forward motion including backwards
     * @param right     - Any movement from left to right
     * @param clockwise - Any turning movements
     */
    public void joystickDrive(double foward, double right, double clockwise)
    {
        double frontLeft = foward + clockwise + right;
        double frontRight = foward - clockwise - right;
        double backLeft = foward + clockwise - right;
        double backRight = foward - clockwise + right;
        double max = abs(frontLeft);
        if (abs(frontRight) > max)
        {
            max = abs(frontRight);
        }
        if (abs(backLeft) > max)
        {
            max = abs(backLeft);
        }
        if (abs(backRight) > max)
        {
            max = abs(backRight);
        }
        if (max > 1)
        {
            frontLeft /= max;
            frontRight /= max;
            backLeft /= max;
            backRight /= max;
        }

        leftFront.setPower(frontLeft);
        rightFront.setPower(frontRight);
        leftRear.setPower(backLeft);
        rightRear.setPower(backRight);
    }
    public void orientedDrive(double forward, double right, double clockwise)
    {
        double theta = Math.toRadians(heading);
        double vx = (forward * cos(theta)) - (right * sin(theta));
        double vy = (forward * sin(theta)) + (right * cos(theta));

        vx *= 1.1;

        joystickDrive(vy, vx, clockwise);
    }
    public boolean driveToTag(int tagID, double speed, double distanceAway) {
        if (!moving) {
            moving = true;
        }

        int tagNumber = eyes.getTagNumber(tagID);
        double rangeError = (eyes.rangeError - distanceAway);
        double headingError = eyes.headingError;
        double yawError = eyes.yawError;

        if (tagNumber == tagID) {

            if (rangeError < 0.5 && headingError < 5 && yawError < 5) {
                stop();
                moving = false;
                return true;
            }
            else {
                double y = Range.clip(rangeError * SPEED_GAIN, -MAX_SPEED, MAX_SPEED);
                double x = Range.clip(-yawError * STRAFE_GAIN, -MAX_STRAFE, MAX_STRAFE);
                double turn = Range.clip(headingError * TURN_GAIN, -MAX_TURN, MAX_TURN);

                joystickDrive(y, x, turn);
                return false;
            }
        }
        else {
            stop();
            return true;
        }
    }


    /**
     * Read the Z axis angle, accounting for the transition from +180 <-> -180.
     * Store the current angle in globalHeading.
     * Positive angles (+) are counterclockwise/CCW, negative angles (-) are clockwise/CW.
     * @return the current heading of the robot.
     */
    public double getHeading()
    {
        double rawImuAngle =  imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        double delta = rawImuAngle - lastAngle;

        // An illustrative example: assume the robot is facing +179 degrees (last angle) and makes a +2 degree turn.
        // The raw IMU value will roll over from +180 to -180, so the final raw angle will be -179.
        // So delta = -179 - (+179) = -358.
        // Since delta is less than -180, add 360 to it: -358 + 360 = +2 (the amount we turned!)
        // This works the same way in the other direction.

        if(delta > 180)
        {
            delta -= 360;
        }
        else if(delta < -180)
        {
            delta += 360;
        }

        heading += delta; // change the global state
        lastAngle = rawImuAngle; // save the current raw Z state
        return heading;
    }


    public void update()
    {
        for (LynxModule module : allHubs)
        {
            module.clearBulkCache();
        }

        leftFrontPosition = leftFront.getCurrentPosition();
        rightFrontPosition = rightFront.getCurrentPosition();
        leftRearPosition = leftRear.getCurrentPosition();
        rightRearPosition = rightRear.getCurrentPosition();

        allMotors = (leftFrontPosition + rightFrontPosition + leftRearPosition + rightRearPosition) / 4;

        telemetry.addData("Motor Position", allMotors);

        heading = getHeading();

        telemetry.addData("Heading: ", heading);
    }

    public void start(){}

    public void stop() {
        leftFront.setPower(0);
        leftRear.setPower(0);
        rightFront.setPower(0);
        rightRear.setPower(0);

    }


}

