package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import com.arcrobotics.ftclib.hardware.motors.Motor;
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
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.List;


public class Thunderbot2023
{
    // defines all variables
    IMU imu = null;

    Orientation angles = new Orientation();
    DcMotorEx leftFront = null;
    DcMotorEx rightFront = null;
    DcMotorEx leftRear = null;
    DcMotorEx rightRear = null;

    Motor rightFront1;
    Motor rightRear1;
    Motor leftFront1;
    Motor leftRear1;

    Delivery delivery = new Delivery();
    Lift lift = new Lift();
    Intake intake = new Intake();

    // Position Variables
    double leftFrontPosition = 0;
    double rightFrontPosition = 0;
    double leftRearPosition = 0;
    double rightRearPosition = 0;
    double allMotors = 0;
    double heading = 0;
    double initRotation = 0;
    double lastAngle = 0;
    double initialPosition = 0;
    boolean moving = false;
    double startAngle = 0;
    boolean angleWrap = false;


    List<LynxModule> allHubs;
    //Eyes vision = new Eyes();


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
        try
        {
            imu = ahwMap.get(IMU.class, "imu");

            RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
            RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

            RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

            imu.initialize(new IMU.Parameters(orientationOnRobot));

            angles = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
                    AngleUnit.DEGREES);

            initRotation = angles.firstAngle;

        }
        catch (Exception p_exeception)
        {
            telemetry.addData("imu not found in config file", 0);
            imu = null;
        }

        telemetry = telem;

        // Define & Initialize Motors

        try {
            allHubs = ahwMap.getAll(LynxModule.class);

            for (LynxModule module : allHubs) {
                module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            }
        }
        catch (Exception e) {
            telemetry.addData("Lynx Module not found", 0);
        }

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


            delivery.init(ahwMap, telem);

            lift.init(ahwMap, telem);

            intake.init(ahwMap, telem);


    }

    /**
     * This code go's through the math behind the mecanum wheel drive.  Given the joystick values,
     * it will calculate the motor commands needed for the mecanum drive.
     *
     * @param foward    - Any forward motion including backwards
     * @param right     - Any movement from left to right
     * @param clockwise ffb - Any turning movements
     */
    public void joystickDrive(double foward, double right, double clockwise) {
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

    // Autonomous Opmodes
    public boolean drive(double targetHeading, double distance, double power)
    {
        double xValue = Math.sin(toRadians(targetHeading)) * power;
        double yValue = Math.cos(toRadians(targetHeading)) * power;

        double currentAngle = updateHeading();

        telemetry.addData("current angle", currentAngle);

        // Set desired angle and initial distanceMoved
        if (!moving)
        {
            startAngle = currentAngle;
            // Determine if I am in one of the scenarios where my gyro angle might wrap
            //      Neg start -> neg degrees to turn
            //      Pos start -> pos degrees to turn
            if ( startAngle < 0.0 && targetHeading > 0.0  &&
                    (startAngle+360) - targetHeading < 180.0 )
            {
                angleWrap = true;
            }
            else if ( startAngle > 0.0 && targetHeading < 0.0 &&
                    (targetHeading+360) - startAngle  < 180.0 )
            {
                angleWrap = true;
            }
            else
            {
                angleWrap = false;
            }

            if (startAngle < 0.0 && angleWrap )
            {
                startAngle = startAngle + 360;
            }

            if (targetHeading == 45 || targetHeading == -135)
            {
                // the rightFront wheel doesn't move at a desired direction of 45 degrees
                initialPosition = leftFrontPosition;
            }
            else
            {
                initialPosition = rightFrontPosition;
            }
            moving = true;
        }

        if ( angleWrap && currentAngle < 0.0 )
        {
            // Prevent the rollover of the currentAngle
            currentAngle += 360;
        }

        if ( angleWrap && targetHeading < 0.0 )
        {
            targetHeading += 360;
        }

        double distanceMoved;
        if (targetHeading == 45 || targetHeading == -135)
        {
            distanceMoved = abs(leftFrontPosition - initialPosition);
        }
        else
        {
            distanceMoved = abs(rightFrontPosition - initialPosition);
        }
        double distanceMovedInCM = distanceMoved / COUNTS_PER_CM;
        telemetry.addData("distanceMoved", distanceMoved);

        double currentPower = 0.1;

        if (distanceMovedInCM <= 0.1 * distance){
            currentPower += 0.0001;
            currentPower = Range.clip(currentPower, 0.1, 1.0);
        } else if (distanceMovedInCM > 0.9 * distance){
            currentPower -= 0.0001;
            currentPower = Range.clip(currentPower, 0.1, 1.0);
        } else {
            currentPower=power;
        }

        // calculates required speed to adjust to gyStartAngle
        double angleError = (startAngle - currentAngle) / 25;
        // Setting range of adjustments
        angleError = Range.clip(angleError, -1, 1);

        if (distanceMovedInCM >= distance)
        {
            // Stops when at the specified distance
            stop();
            moving = false;
            return true;
        }
        else
        {
            // Continues if not at the specified distance
            telemetry.addData("y value", yValue);
            telemetry.addData("x value", xValue);
            telemetry.addData("angle error", angleError);

            joystickDrive(yValue, xValue, angleError);
            return false;
        }
    }

    public boolean turn(double degree, double power) {
        imu.resetYaw();

        leftFrontPosition = leftFront.getCurrentPosition();
        rightFrontPosition = rightFront.getCurrentPosition();
        leftRearPosition = leftRear.getCurrentPosition();
        rightRearPosition = rightRear.getCurrentPosition();

        imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        if (updateHeading() <= 180) {
            leftFront.setPower(-power);
            rightFront.setPower(power);
            leftRear.setPower(-power);
            rightRear.setPower(power);
        } else if (updateHeading() >= 181) {
            leftFront.setPower(power);
            rightFront.setPower(-power);
            leftRear.setPower(power);
            rightRear.setPower(-power);
        } else {
            stop();
        }
        return true;

    }

    public boolean strafe(double distance, double power) {
        leftFrontPosition = leftFront.getCurrentPosition();
        rightFrontPosition = rightFront.getCurrentPosition();
        leftRearPosition = leftRear.getCurrentPosition();
        rightRearPosition = rightRear.getCurrentPosition();

        double targetPosition = distance * COUNTS_PER_CM;

        if (distance > 0) {
            if (leftFrontPosition < targetPosition) {
                leftFront.setPower(power);
                rightFront.setPower(-power);
                leftRear.setPower(-power);
                rightRear.setPower(power);
            } else {
                stop();
            }
        }
        if (distance < 0) {
            if (leftFrontPosition > targetPosition) {
                leftFront.setPower(-power);
                rightFront.setPower(power);
                leftRear.setPower(power);
                rightRear.setPower(-power);
            } else {
                stop();
            }
        }
        return true;
    }

    // updatin heading
    /**
     * Get the heading angle from the imu and convert it to degrees.
     * @return the heading angle
     */
    /**
     * Read the Z axis angle, accounting for the transition from +180 <-> -180.
     * Store the current angle in globalHeading.
     * Positive angles (+) are counterclockwise/CCW, negative angles (-) are clockwise/CW.
     * @return the current heading of the robot.
     */
//    public double getHeading()
//    {
//        double rawImuAngle =  imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
//        double delta = rawImuAngle - lastAngle;
//
//        // An illustrative example: assume the robot is facing +179 degrees (last angle) and makes a +2 degree turn.
//        // The raw IMU value will roll over from +180 to -180, so the final raw angle will be -179.
//        // So delta = -179 - (+179) = -358.
//        // Since delta is less than -180, add 360 to it: -358 + 360 = +2 (the amount we turned!)
//        // This works the same way in the other direction.
//
//        if(delta > 180) delta -= 360;
//        else if(delta < -180) delta += 360;
//
//        heading += delta; // change the global state
//        lastAngle = rawImuAngle; // save the current raw Z state
//        return heading;
//    }
    public double updateHeading()
    {
        Orientation angles = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
                AngleUnit.DEGREES);
        return -AngleUnit.DEGREES.normalize(
                AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle));
    }
    public void update() {
        for (LynxModule module : allHubs) {
            module.clearBulkCache();
        }

        leftFrontPosition = leftFront.getCurrentPosition();
        rightFrontPosition = rightFront.getCurrentPosition();
        leftRearPosition = leftRear.getCurrentPosition();
        rightRearPosition = rightRear.getCurrentPosition();

        allMotors = (leftFrontPosition + rightFrontPosition + leftRearPosition + rightRearPosition) / 4;

        telemetry.addData("Motor Position", allMotors);

        heading = updateHeading();

        telemetry.addData("Heading: ", heading);

            lift.update();
            intake.update();
            delivery.update();

    }

    public void start(){}

    public void stop() {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
    }

    /**
     * Stop all the motors.
     */
//    public void stop()
//    {
//        leftFront.set(0);
//        rightFront.set(0);
//        leftRear.set(0);
//        rightRear.set(0);
//      }

}

