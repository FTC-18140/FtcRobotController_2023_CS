package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class EndGame
{

    Telemetry telemetry;

    CRServo launcher = null;
    DcMotor leftLift = null;
    DcMotor rightLift = null;
    double power = 0;

    public void init(HardwareMap hwMap, Telemetry telem)
    {
        telemetry = telem;

        try {
            launcher = hwMap.crservo.get("launcher");
            launcher.setPower(0);
        } catch (Exception e) {
            telemetry.addData("launcher not found", 0);
        }
        try {
            leftLift = hwMap.dcMotor.get("oX");
            leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
            leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            telemetry.addData("leftLift not found", 0);
        }
        try {
            rightLift = hwMap.dcMotor.get("oY");
            rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightLift.setDirection(DcMotorSimple.Direction.FORWARD);
        } catch (Exception e) {
            telemetry.addData("rightLift not found", 0);
        }
    }

    public void update()
    {
        if (launcher != null) { power = launcher.getPower();}
    }

    public void launcherPower(double power)
    {
        if (launcher != null) {launcher.setPower(power);}
        else { telemetry.addData("drone launcher not initialized.", 0); }
    }
    public void pullUp(double power) {
        leftLift.setPower(power * 0.8375);
//        leftLift.setPower(power);
        rightLift.setPower(power);
    }
}
