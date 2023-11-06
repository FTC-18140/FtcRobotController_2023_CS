package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class Delivery {

    Telemetry telemetry;
    HardwareMap hardwaremap = null;

    // All the Attachment Defining


    Servo wrist = null;
    Servo deliver = null;
    CRServo shooter = null;


    public void init(HardwareMap hwMap, Telemetry telem) {
        try {
            wrist = hwMap.servo.get("wrist");

            deliver = hwMap.servo.get("drop");

            shooter = hwMap.crservo.get("launcher");
        } catch (Exception e) {
            telemetry.addData("wrist, drop, or launcher not found", 0);
        }
    }



    public void wristMove(double position) {
        wrist.setPosition(position);
    }

    public void drop(double position) {
        deliver.setPosition(position);
    }

    public void launch(double power) {
        shooter.setPower(power);
    }

    public void update() {
        wrist.getPosition();
        deliver.getPosition();
        shooter.getPower();
    }

}

