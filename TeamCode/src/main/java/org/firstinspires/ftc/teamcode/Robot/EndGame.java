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
    DcMotor oX = null;
    DcMotor oY = null;
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
            oX = hwMap.dcMotor.get("lLift");
            oX.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            telemetry.addData("leftLift not found", 0);
        }
        try {
            oY = hwMap.dcMotor.get("rLift");
            oX.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
        oX.setPower(power);
        oY.setPower(power);
    }
}
