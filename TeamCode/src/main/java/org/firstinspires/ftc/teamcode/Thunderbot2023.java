package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.toRadians;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.List;


public class Thunderbot2023
{
    // defines all variables
    BNO055IMU imu = null;
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
    List<LynxModule> allHubs;

    double initialPosition = 0;
    boolean moving = false;
    double startAngle = 0;
    boolean angleWrap = false;

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
    public Thunderbot2023()
    {

    }

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
            // Set up the parameters with which we will use our IMU. Note that integration
            // algorithm here just reports accelerations to the logcat log; it doesn't actually
            // provide positional information.
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            // Retrieve and initialize the IMU.
            imu = ahwMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
            imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
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
    }

    /**
     * This code go's through the math behind the mecanum wheel drive.  Given the joystick values,
     * it will calculate the motor commands needed for the mecanum drive.
     *
     * @param foward    - Any forward motion including backwards
     * @param right     - Any movement from left to right
     * @param clockwise - Any turning movements
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

    /**
     * Get the heading angle from the imu and convert it to degrees.
     * @return the heading angle
     */
    public double updateHeading()
    {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
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
    }

    public void start() {
    }

    /**
     * Stop all the motors.
     */
    public void stop()
    {
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
    }

}

