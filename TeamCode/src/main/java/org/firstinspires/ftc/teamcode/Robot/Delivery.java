package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class Delivery
{
    Telemetry telemetry;

    // All the Attachment Defining
    public Servo wrist = null;
    public Servo leftGripper = null;
    public Servo rightGripper = null;
    public Servo fLeftGripper = null;
    public Servo fRightGripper = null;
    public Servo twist = null;
    public Servo lElbow = null;
    public Servo rElbow = null;
    public Servo intakeLift =  null;


    public double wristPos = 0;
    public double leftGripPos = 0;
    public double rightGripPos = 0;
    public double twistPos = 0;
    public double lElbowPos = 0;
    public double rElbowPos = 0;

    static public double WRIST_INIT = 1;
    static public double LEFTGRIP_INIT = 0.5;
    static public double RIGHTGRIP_INIT = 0.5;
    static public double FLEFTGRIP_INIT = 0.5;
    static public double FRIGHTGRIP_INIT = 0.5;
    static public double TWIST_INIT = 0.8;
    static public double ELBOW_INIT = 0.04;
    static public double INTAKE_INIT = 0;
    // 0.225 is the position to get ready to pick up

    public enum DepositorPositions
    {
        HOME(0, 0, 0),
        INIT(ELBOW_INIT, WRIST_INIT, TWIST_INIT),
        DROP(90, 90, 90);

        public final double elbowPos;
        public final double wristPos;
        public final double twisterPos;

        DepositorPositions( double elbow, double wrist, double twist)
        {
            elbowPos = elbow;
            wristPos = wrist;
            twisterPos = twist;
        }
    }

    public enum GripperPositions
    {
        CLOSED( 0,0 ),
        OPEN( 1, 1),
        INIT( LEFTGRIP_INIT, RIGHTGRIP_INIT);

        public final double leftGripPos;
        public final double rightGripPos;

        GripperPositions( double leftGrip, double rightGrip)
        {
            leftGripPos = leftGrip;
            rightGripPos = rightGrip;
        }
    }

    public void init(HardwareMap hwMap, Telemetry telem)
    {
        telemetry = telem;

        try {
            wrist = hwMap.servo.get("wrist");
            wrist.setPosition(WRIST_INIT);
        } catch (Exception e) {
            telemetry.addData("wrist did not initialize", 0);
        }

        try {
            leftGripper = hwMap.servo.get("leftGrip");
            leftGripper.setPosition(LEFTGRIP_INIT);
        } catch (Exception e) {
            telemetry.addData("leftGrip did not initialize", 0);
        }

        try {
            rightGripper = hwMap.servo.get("rightGrip");
            rightGripper.setPosition(RIGHTGRIP_INIT);
        } catch (Exception e) {
            telemetry.addData("rightGrip did not initialize", 0);
        }
        try {
            fLeftGripper = hwMap.servo.get("fLeftGrip");
            fLeftGripper.setPosition(FLEFTGRIP_INIT);
        } catch (Exception e) {
            telemetry.addData("Front Left Gripper not found", 0);
        }
        try {
            fRightGripper = hwMap.servo.get("fRightGrip");
            fRightGripper.setPosition(FRIGHTGRIP_INIT);
        } catch (Exception e) {
            telemetry.addData("Front Right Gripper Not Found", 0);
        }
        try {
            twist = hwMap.servo.get("twist");
            twist.setPosition(TWIST_INIT);
        } catch (Exception e) {
            telemetry.addData("twist did not initialize", 0);
        }

        try {
            lElbow = hwMap.servo.get("lElbow");
            lElbow.setDirection(Servo.Direction.REVERSE);
            lElbow.setPosition(ELBOW_INIT);
        } catch (Exception e) {
            telemetry.addData("lElbow did not initialize", 0);
        }

        try {
            rElbow = hwMap.servo.get("rElbow");
            rElbow.setDirection(Servo.Direction.FORWARD);
            rElbow.setPosition(ELBOW_INIT);
        } catch (Exception e) {
            telemetry.addData("rElbow did not initialize", 0);
        }
        try {
            intakeLift = hwMap.servo.get("intakeLift");
            intakeLift.setPosition(INTAKE_INIT);
        } catch(Exception e ) {
            telemetry.addData("IntakeLift Not Found", 0);
    }
    }

    public void setWristPos(double wristPos)
    {
        if ( wrist != null ) { wrist.setPosition( wristPos); }
        else { telemetry.addData("delivery wrist not initialized.", 0); }
    }

    public void setLeftGripPos(double leftGripPos)
    {
        if ( leftGripper != null) { leftGripper.setPosition(leftGripPos); }
        else { telemetry.addData("delivery left gripper not initialized.", 0); }
    }

    public void setRightGripPos(double rightGripPos)
    {
        if ( rightGripper != null) { rightGripper.setPosition(rightGripPos); }
        else { telemetry.addData("delivery right gripper not initialized.", 0); }
    }

    public void setTwistPos(double twistPos)
    {
        if ( twist != null ) { twist.setPosition( twistPos); }
        else { telemetry.addData("delivery twist not initialized.", 0); }
    }

    public void setlElbowPos(double lElbowPos)
    {
        if (lElbow != null ) { lElbow.setPosition( lElbowPos); }
        else { telemetry.addData("delivery left elbow not initialized.", 0); }
    }

    public void setrElbowPos(double rElbowPos)
    {
        if ( rElbow != null ) { rElbow.setPosition(rElbowPos); }
        else { telemetry.addData("delivery right elbow not initialized.", 0); }
    }

    public void dropBoth() {
        dropLeft();
        dropRight();
    }
    public void dropLeft() {
        setLeftGripPos(GripperPositions.OPEN.leftGripPos);
    }
    public void dropRight() {
        setRightGripPos(GripperPositions.OPEN.rightGripPos);
    }

    public void resetGripperBoth() {
        resetLeftGripper();
        resetRightGripper();
    }

    public void resetLeftGripper()
    {
        setLeftGripPos(GripperPositions.CLOSED.leftGripPos);
    }

    public void resetRightGripper() {
        setRightGripPos(GripperPositions.CLOSED.rightGripPos);
    }

    public void goTo( DepositorPositions thePos )
    {
        setlElbowPos( thePos.elbowPos);
        setrElbowPos( thePos.elbowPos);
        setWristPos(thePos.wristPos);
        setTwistPos( thePos.twisterPos);
    }

    public void update()
    {
        if (wrist != null) { wristPos = wrist.getPosition(); }
        if (leftGripper != null) { leftGripPos = leftGripper.getPosition(); }
        if (rightGripper != null) { rightGripPos = rightGripper.getPosition(); }
        if (twist != null) { twistPos = twist.getPosition(); }
        if (lElbow != null) { lElbowPos = lElbow.getPosition(); }
        if (rElbow != null) {  rElbowPos = rElbow.getPosition(); }
    }

}

