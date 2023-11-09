package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class Delivery {

    Telemetry telemetry;
    HardwareMap hardwaremap = null;

    // All the Attachment Defining


    CRServo wrist = null;
    Servo deliver = null;
    CRServo shooter = null;
    ElapsedTime matchTime;

//    private final double WRIST_MIN = 0;
//    private final double WRIST_MAX = 1;
//
//    public double getWRIST_MIN() {
//        return WRIST_MIN;
//    }
//    public double getWRIST_MAX() {
//        return WRIST_MAX;
//    }
//

    public void init(HardwareMap hwMap, Telemetry telem) {
        try {
            wrist = hwMap.crservo.get("wrist");

            deliver = hwMap.servo.get("drop");

            shooter = hwMap.crservo.get("launcher");
        } catch (Exception e) {
            telemetry.addData("wrist, drop, or launcher not found", 0);
        }
    }



    public void wristMove(double power) {
        wrist.setPower(power);
    }

    public void drop(double position) {
        deliver.setPosition(position);
    }


    public void launch(double power) {
        shooter.setPower(power);
    }

    public void update() {
        wrist.getPower();
        deliver.getPosition();
        shooter.getPower();
    }

}

