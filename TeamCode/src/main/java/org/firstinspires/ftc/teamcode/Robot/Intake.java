package org.firstinspires.ftc.teamcode.Robot;

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


    static public double LEFTGRIP_INIT = 0.5;
    static public double RIGHTGRIP_INIT = 0.5;
    static public double INTAKEELBOW_INIT = 0;


    public enum IntakePositions
    {
        HOME(0, 0, 0),
        INIT( INTAKEELBOW_INIT, LEFTGRIP_INIT, RIGHTGRIP_INIT),
        STANDBY(0.25, 0.25, 0.25 ),
        TRANSFER(1, 1, 1);

        public final double elbowPos;
        public final double leftGripPos;
        public final double rightGripPos;
        IntakePositions( double elbow, double leftGrip, double rightGrip)
        {
            elbowPos = elbow;
            leftGripPos = leftGrip;
            rightGripPos = rightGrip;
        }
    }

    public void init(HardwareMap hwMap, Telemetry telem)
    {
        telemetry = telem;

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
            intakeElbow = hwMap.servo.get("iElbow");
        } catch(Exception e) {
            telemetry.addData("intakeArm not found", 0);
        }
    }

    public void setElbowPos(double elbow)
    {
        if ( intakeElbow != null ) { intakeElbow.setPosition( elbow); }
        else { telemetry.addData("intake elbow not initialized.", 0); }
    }

    public void setLeftGripPos(double leftGripPos)
    {
        if ( leftGripper != null) { leftGripper.setPosition(leftGripPos); }
        else { telemetry.addData("intake left gripper not initialized.", 0); }
    }

    public void setRightGripPos(double rightGripPos)
    {
        if ( rightGripper != null) { rightGripper.setPosition(rightGripPos); }
        else { telemetry.addData("intake right gripper not initialized.", 0); }
    }

    public void dropBoth() {
        dropLeft();
        dropRight();
    }
    public void dropLeft() {
        setLeftGripPos(1);
    }
    public void dropRight() {
        setRightGripPos(1);
    }

    public void resetGripperBoth() {
        resetLeftGripper();
        resetRightGripper();
    }

    public void resetLeftGripper()
    {
        setLeftGripPos(0);
    }

    public void resetRightGripper() {
        setRightGripPos(0);
    }
    public void goTo( IntakePositions pos)
    {
        setElbowPos(pos.elbowPos);
        setLeftGripPos(pos.leftGripPos);
        setRightGripPos(pos.rightGripPos);
    }

    public void update()
    {
        if (intakeElbow != null) { intakeElbowPos = intakeElbow.getPosition(); }
        if (leftGripper != null) { leftGripPos = leftGripper.getPosition(); }
        if (rightGripper != null) { rightGripPos = rightGripper.getPosition(); }
    }
}
