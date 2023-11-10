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


    Servo wrist = null;
    Servo deliver = null;
    CRServo shooter = null;
    ElapsedTime matchTime;

    private final double WRIST_MIN = 0;
    private final double WRIST_MAX = 1;

    public double getWRIST_MIN() {
        return WRIST_MIN;
    }
    public double getWRIST_MAX() {
        return WRIST_MAX;
    }

    public void init(HardwareMap hwMap, Telemetry telem) {

            wrist = hwMap.servo.get("wrist");

            deliver = hwMap.servo.get("drop");

            shooter = hwMap.crservo.get("launcher");

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

