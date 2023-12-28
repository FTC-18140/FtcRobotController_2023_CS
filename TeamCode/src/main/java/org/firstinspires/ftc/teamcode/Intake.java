package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake
{
    Telemetry telemetry;
    Servo leftGripper = null;
    Servo rightGripper = null;

    Servo intakeElbow = null;

    public double leftGripPos = 0;
    public double rightGripPos = 0;
    public double intakeElbowPos = 0;


    static public double LEFTGRIP_INIT = 0;
    static public double RIGHTGRIP_INIT = 0;
    static public double INTAKEELBOW_INIT = 0;


    public enum IntakePositions
    {
        HOME(0, 0),
        INIT( INTAKEELBOW_INIT, LEFTGRIP_INIT),
        TRANSFER(90, 90);

        public final double elbowPos;
        public final double gripPos;
        IntakePositions( double elbow, double grip)
        {
            elbowPos = elbow;
            gripPos = grip;
        }
    }

    public void init(HardwareMap hwMap, Telemetry telem) {
        try {
            leftGripper = hwMap.servo.get("gLintake");
        } catch (Exception e) {
            telemetry.addData("gripperLintake not found", 0);
        }

        try {
            rightGripper = hwMap.servo.get("gRintake");
        } catch (Exception e) {
            telemetry.addData("gripperRintake not found", 0);
        }

        try {
            intakeElbow = hwMap.servo.get("iArm");
        } catch(Exception e) {
            telemetry.addData("intakeArm not found", 0);
        }

        telemetry = telem;
    }

    public void setElbowPos(double elbow)
    {
        if ( intakeElbow != null ) { intakeElbow.setPosition( elbow); }
    }

    public void setLeftGripPos(double leftGripPos)
    {
        if ( leftGripper != null) { leftGripper.setPosition(leftGripPos); }
    }

    public void setRightGripPos(double rightGripPos)
    {
        this.rightGripPos = rightGripPos;
    }

    public void dropBoth() {
        dropLeft();
        dropRight();
    }
    public void dropLeft() {
        leftGripper.setPosition(90);
    }
    public void dropRight() {
        rightGripper.setPosition(90);
    }

    public void goTo( IntakePositions pos)
    {
        intakeElbow.setPosition(pos.elbowPos);
        leftGripper.setPosition(pos.gripPos);
        rightGripper.setPosition(pos.gripPos);
    }

    public void update()
    {
        if (intakeElbow != null) { intakeElbowPos = intakeElbow.getPosition(); }
        if (leftGripper != null) { leftGripPos = leftGripper.getPosition(); }
        if (rightGripper != null) { rightGripPos = rightGripper.getPosition(); }

    }
}
