package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class Intake
{
    Telemetry telemetry;
    Servo leftGripper = null;
    Servo rightGripper = null;

    Servo intakeElbow = null;

    public double leftGripPos = 0;
    public double rightGripPos = 0;
    public double intakeElbowPos = 0;


    static public double LEFTGRIP_INIT = 0.1;
    static public double RIGHTGRIP_INIT = 0.1;
    static public double INTAKEELBOW_INIT = 0.06;
    // 0.185 is the down position ready to pick up the pixel
    // 0.225 is the inside the pixel and ready to activate the grippers
    // 0 is the drop off point
    //
    static public double GRIP_DROP = 0;
    static public double LEFT_GRIP_HOLD = 0.5;
    static public double RIGHT_GRIP_HOLD = 0.6;
    private Positions currentPosition = Positions.INIT;
    private Positions previousPosition = Positions.INIT;
    private boolean moveSlowly = false;
    private boolean clearOfTransferZone = true;


    // TODO: define these Positions to help with intake control
    public enum Positions
    {
        TRANSFER( 0, GRIP_DROP, GRIP_DROP),
        READY_TO_TRANSFER(0, LEFT_GRIP_HOLD, RIGHT_GRIP_HOLD),
        INIT(INTAKEELBOW_INIT, LEFT_GRIP_HOLD, RIGHT_GRIP_HOLD),
        WAIT_TO_INTAKE(0.185, LEFT_GRIP_HOLD, RIGHT_GRIP_HOLD),
        DOWN_TO_PIXEL(0.25, GRIP_DROP, GRIP_DROP ),
        INTAKE( 0.25, LEFT_GRIP_HOLD, RIGHT_GRIP_HOLD);

        public final double elbowPos;
        public final double leftGripPos;
        public final double rightGripPos;
        Positions(double elbow, double leftGrip, double rightGrip)
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
            leftGripper.setDirection(Servo.Direction.REVERSE);
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
        goTo(Positions.INIT, true);
    }

    public void setElbowPos(double elbow)
    {
        if ( intakeElbow != null ) {
            intakeElbow.setPosition(elbow);
        }
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
        setLeftGripPos(GRIP_DROP);
    }
    public void dropRight() {
        setRightGripPos(GRIP_DROP);
    }

    public void holdPixelsBoth() {
        holdPixelLeft();
        holdPixelRight();
    }

    public void holdPixelLeft()
    {
        setLeftGripPos(LEFT_GRIP_HOLD);
    }

    public void holdPixelRight() {
        setRightGripPos(RIGHT_GRIP_HOLD);
    }
    public void goTo(Positions pos, boolean gripperToo)
    {
        previousPosition = currentPosition;
        currentPosition = pos;
        setElbowPos(pos.elbowPos);
        if ( gripperToo)
        {
            setLeftGripPos(pos.leftGripPos);
            setRightGripPos(pos.rightGripPos);
        }
    }

    public void toggleDown()
    {
        switch (currentPosition)
        {
            case TRANSFER:
            case READY_TO_TRANSFER:
                goTo(Positions.INIT, false);
                break;
            case INIT:
                goTo(Positions.WAIT_TO_INTAKE, false);
                break;
            case WAIT_TO_INTAKE:
                goTo(Positions.DOWN_TO_PIXEL, false);
                break;
            case DOWN_TO_PIXEL:
                goTo(Positions.INTAKE, false);
                break;
            default:
                break;

        }
    }

    public void toggleUp()
    {
        switch (currentPosition)
        {
            case INTAKE:
            case DOWN_TO_PIXEL:
                goTo(Positions.WAIT_TO_INTAKE, false);
                break;
            case WAIT_TO_INTAKE:
                goTo(Positions.INIT, false);
                break;
            case INIT:
                goTo(Positions.READY_TO_TRANSFER, false);
                break;
            case READY_TO_TRANSFER:
                goTo(Positions.TRANSFER, false);
                break;
            default:
                break;
        }
    }

    public void toggleGripper()
    {
        if ( leftGripPos != GRIP_DROP ) { dropLeft(); }
        else { holdPixelLeft(); }

        if ( rightGripPos != GRIP_DROP ) { dropRight(); }
        else { holdPixelRight(); }
    }
    public boolean driveSlowly() { return moveSlowly; }
    public boolean clearedTransferZone() { return clearOfTransferZone; }

    public void update()
    {
        if (intakeElbow != null) {
            intakeElbowPos = intakeElbow.getPosition();
            moveSlowly = (intakeElbowPos >= Positions.WAIT_TO_INTAKE.elbowPos);
            clearOfTransferZone = (intakeElbowPos >= Positions.INIT.elbowPos);
        }
        if (leftGripper != null) { leftGripPos = leftGripper.getPosition(); }
        if (rightGripper != null) { rightGripPos = rightGripper.getPosition(); }
        telemetry.addData("Intake Right Gripper Position =", rightGripPos);
        telemetry.addData("Intake Left Gripper Position =", leftGripPos);
        telemetry.addData("Intake Position: ", currentPosition);
        telemetry.addData("Drive Slowly: ", moveSlowly);
        telemetry.addData("Intake Clear of Transfer Zone: ", clearOfTransferZone);
    }
}
