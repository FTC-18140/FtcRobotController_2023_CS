package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class LinearSlide
{

    Telemetry telemetry;

    // Defining Motors
    DcMotor leftLinear = null;
    DcMotor rightLinear = null;

    public double leftSlidePosition = 0;
    public double rightSlidePosition = 0;

    final private double COUNTS_PER_MOTOR_REV = 28; // REV HD Hex motor
    final private double DRIVE_GEAR_REDUCTION = 3.61 * 5.23;  // actual gear ratios of the 4:1 and 5:1 UltraPlanetary gear box modules
    final private double SPOOL_DIAMETER_CM = 3.5;  // slide spool is 35mm in diameter
    final private double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)
            / (SPOOL_DIAMETER_CM * Math.PI);

    public static double SCALE_FACTOR = 1;

    // Initialize
    public void init(HardwareMap hwMap, Telemetry telem)
    {
        telemetry = telem;
        try {
            leftLinear = hwMap.dcMotor.get("lSlide");
            leftLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftLinear.setDirection(DcMotorSimple.Direction.FORWARD);
            leftLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch(Exception e) {
            telemetry.addData("lSlide not found", 0);
        }
        try {
            rightLinear = hwMap.dcMotor.get("rSlide");
            rightLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightLinear.setDirection(DcMotorSimple.Direction.REVERSE);
            rightLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch(Exception e) {
            telemetry.addData("rSlide not found", 0);
        }

    }

    public void linearPower(double power) {
        if (leftLinear != null ) {leftLinear.setPower(power); }
        else { telemetry.addData("linear slide left motor not initialized.", 0); }
        if (rightLinear != null ) { rightLinear.setPower(power); }
        else { telemetry.addData("linear slide right  motor not initialized.", 0); }
    }

    // Math behind the positions of the linear slides and seeing where it needs to stop and start
    public void linearMove(double power) {
        if (getLiftPosition() > 7.5) {
            linearPower(power);
        } else if (getLiftPosition() <= 7.5 &&  getLiftPosition() >= 0) {
            linearPower(power * 0.5);
        } else if (getLiftPosition() > 36) {
            linearPower(-0.5);
        } else if (getLiftPosition() < 0){
            linearPower(0.5);
        } else {
            linearPower(0);
        }
    }

    public void goToLinear(double position) {

    }

    public void update() {
        if (leftLinear != null ) { leftSlidePosition = leftLinear.getCurrentPosition(); }
        if (rightLinear != null ) { rightSlidePosition = rightLinear.getCurrentPosition(); }
        telemetry.addData("Slide Position = ", getLiftPosition());
    }

    public double getLiftPosition() {
        return SCALE_FACTOR *  (0.5 * (leftSlidePosition + rightSlidePosition)) / COUNTS_PER_CM;
    }
    

//    public void linearToPosition( double cm, double power )
//    {
//        int targetCounts = (int) (cm * COUNTS_PER_CM);
//        if ( leftLinear != null  && rightLinear != null ) {
//            leftLinear.setTargetPosition(targetCounts);
//            rightLinear.setTargetPosition(targetCounts);
//            leftLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            rightLinear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            leftLinear.setVelocity(power * MAX_SPEED * COUNTS_PER_CM);
//            rightLinear.setVelocity(power * MAX_SPEED * COUNTS_PER_CM);
//        }
//        else
//        {
//            telemetry.addData("linear slide motor not initialized.", 0);
//        }
//    }
//
//    public double toggleUp( double cmToggle )
//    {
//        double targetCM = getLiftPosition() + cmToggle;
//        targetCM = clip( targetCM, MIN_POS, MAX_POS);
//        linearToPosition( targetCM, DEFAULT_POWER);
//        return targetCM;
//    }
//
//    public double toggleDown( double cmToggle )
//    {
//        double targetCM = getLiftPosition() - cmToggle;
//        targetCM = clip( targetCM, MIN_POS, MAX_POS);
//        linearToPosition( targetCM, DEFAULT_POWER);
//        return targetCM;
//    }

}

