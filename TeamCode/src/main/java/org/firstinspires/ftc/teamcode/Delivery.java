package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class Delivery
{
    Telemetry telemetry;

    // All the Attachment Defining
    Servo wrist = null;
    Servo leftGripper = null;
    Servo rightGripper = null;
    Servo twist = null;
    Servo lElbow = null;
    Servo rElbow = null;


    public double wristPos = 0;
    public double leftGripPos = 0;
    public double rightGripPos = 0;
    public double twistPos = 0;
    public double lElbowPos = 0;
    public double rElbowPos = 0;

    static public double WRIST_INIT = 0;
    static public double LEFTGRIP_INIT = 0;
    static public double RIGHTGRIP_INIT = 0;
    static public double TWIST_INIT = 0;
    static public double LELBOW_INIT = 0;
    static public double RELBOW_INIT = 0;

    public enum DepositorPositions
    {
        HOME(0, 0, 0),
        INIT( LELBOW_INIT, WRIST_INIT, TWIST_INIT),
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

    public void init(HardwareMap hwMap, Telemetry telem)
    {
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
            twist = hwMap.servo.get("twist");
            twist.setPosition(TWIST_INIT);

        } catch (Exception e) {
            telemetry.addData("twist did not initialize", 0);
        }

        try {
            lElbow = hwMap.servo.get("lElbow");
            lElbow.setPosition(LELBOW_INIT);

        } catch (Exception e) {
            telemetry.addData("lElbow did not initialize", 0);
        }

        try {
            rElbow = hwMap.servo.get("rElbow");
            rElbow.setPosition(RELBOW_INIT);

        } catch (Exception e) {
            telemetry.addData("rElbow did not initialize", 0);
        }

        telemetry = telem;
    }

    public void setWristPos(double wristPos)
    {
        if ( wrist != null ) { wrist.setPosition( wristPos); }
    }

    public void setLeftGripPos(double leftGripPos)
    {
        if ( leftGripper != null) { leftGripper.setPosition(leftGripPos); }
    }

    public void setRightGripPos(double rightGripPos)
    {
        if ( rightGripper != null) { rightGripper.setPosition(rightGripPos); }
    }

    public void setTwistPos(double twistPos)
    {
        if ( twist != null ) { twist.setPosition( twistPos); }
    }

    public void setlElbowPos(double lElbowPos)
    {
        if (lElbow != null ) { lElbow.setPosition( lElbowPos); }
    }

    public void setrElbowPos(double rElbowPos)
    {
        if ( rElbow != null ) { rElbow.setPosition(rElbowPos); }
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

