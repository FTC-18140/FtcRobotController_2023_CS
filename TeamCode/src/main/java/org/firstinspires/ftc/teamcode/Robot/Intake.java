package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class Intake
{
    public static boolean TELEM = false ;
    Telemetry telemetry;
    Servo leftGripper = null;
    Servo rightGripper = null;
    Servo leftMandible = null;
    Servo rightMandible = null;
    Servo rIntake = null;
    Servo lIntake = null;

    public double leftGripPos = 0.2;
    public double rightGripPos = 0;
    public double rIntakeElbowPos = 0;
    public double lIntakeElbowPos = 0;
    public double leftMandiblePos = 0;
    public double rightMandiblePos = 0;

    static public double LEFTGRIP_INIT = 0.2;
    static public double RIGHTGRIP_INIT = 0;
    static public double INTAKEELBOW_INIT = 0.045;
    // 0.185 is the down position ready to pick up the pixel
    // 0.225 is the inside the pixel and ready to activate the grippers
    // 0 is the drop off point
    //
    static public double MANDIBLE_INIT = 0;
    static public double LEFT_GRIP_DROP = 0.2;
    static public double RIGHT_GRIP_DROP = 0;
    static public double LEFT_GRIP_HOLD = 0.58;
    static public double RIGHT_GRIP_HOLD = 0.45;
    static public double LEFT_MANDIBLE_OPEN = 0.58;
    static public double RIGHT_MANDIBLE_OPEN = 0.58;
    static public double LEFT_MANDIBLE_CLOSE = 0.18;
    static public double RIGHT_MANDIBLE_CLOSE = 0.16;
    private Positions currentPosition = Positions.INIT;
    private Positions previousPosition = Positions.INIT;
    private boolean moveSlowly = false;
    private boolean clearOfTransferZone = true;
    private boolean leftGripperClosed = false;
    private boolean rightGripperClosed = false;

    public enum Positions
    {
        // TRANSFER is  the position where it is right above the delivery grippers and drops the pixels into it
        TRANSFER( 0.02, 0.02, LEFT_GRIP_DROP, RIGHT_GRIP_DROP),
        // READY_TO_TRANSFER is where it is right above the  delivery grippers and is about to drop the pixels
        READY_TO_TRANSFER(0, 0, LEFT_GRIP_HOLD, RIGHT_GRIP_HOLD),
        // INIT is where the elbow and grippers initialize to
        TELE_INIT(0.125, 0.125,  LEFT_GRIP_DROP, RIGHT_GRIP_DROP),
        INIT(INTAKEELBOW_INIT, INTAKEELBOW_INIT, LEFT_GRIP_HOLD, RIGHT_GRIP_HOLD),
        // WAIT_TO_INTAKE is right above the pixels with the grippers closed and above the pixels and about to go inside of the pixel
        WAIT_TO_INTAKE(0.1275, 0.1275, LEFT_GRIP_HOLD, RIGHT_GRIP_HOLD),
        // DOWN_TO_PIXEL is where the grippers are inside of the pixels and about to open to grab onto the pixels
        DOWN_TO_PIXEL(0.15, 0.15, LEFT_GRIP_DROP, RIGHT_GRIP_DROP ),
        // INTAKE is where the grippers are in the pixels and open and holding onto the pixel
        INTAKE( 0.15, 0.15, LEFT_GRIP_HOLD, RIGHT_GRIP_HOLD);

        public final double rElbowPos;
        public final double lElbowPos;
        public final double leftGripPos;
        public final double rightGripPos;
        Positions(double rElbow, double lElbow, double leftGrip, double rightGrip)
        {
            lElbowPos = lElbow;
            rElbowPos = rElbow;
            leftGripPos = leftGrip;
            rightGripPos = rightGrip;


        }
    }
    public enum MandiblePositions
    {
        CLOSED(LEFT_MANDIBLE_CLOSE, RIGHT_MANDIBLE_CLOSE),
        OPEN(LEFT_MANDIBLE_OPEN, RIGHT_MANDIBLE_OPEN);
        public final double leftMandiblePos;
        public final double rightMandiblePos;
        MandiblePositions(double leftMandible, double rightMandible) {
            leftMandiblePos = leftMandible;
            rightMandiblePos = rightMandible;
        }
    }


    public void init(HardwareMap hwMap, Telemetry telem, boolean ifAuto)
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
            rIntake = hwMap.servo.get("riElbow");
            rIntake.setDirection(Servo.Direction.FORWARD);
        } catch(Exception e) {
            telemetry.addData("right Intake arm not found", 0);
        }
        try {
            lIntake = hwMap.servo.get("liElbow");
            lIntake.setDirection(Servo.Direction.REVERSE);
        } catch(Exception e) {
            telemetry.addData("left Intake arm not found", 0);
        }
        try {
            leftMandible = hwMap.servo.get("landible");
            leftMandible.setDirection(Servo.Direction.REVERSE);
            leftMandible.setPosition(MANDIBLE_INIT);
        } catch(Exception e) {
            telemetry.addData("landible not found", 0);
        }
        try {
            rightMandible =  hwMap.servo.get("randible");
            rightMandible.setDirection(Servo.Direction.FORWARD);
            rightMandible.setPosition(MANDIBLE_INIT);
        } catch(Exception e) {
            telemetry.addData("randible not found", 0);
        }
        if (ifAuto) {
            goTo(Positions.INIT, true);
        } else {
            goTo(Positions.TELE_INIT, true);
        }
    }
    public void setLElbowPos(double elbow) {
        if (lIntake != null) {
            lIntake.setPosition(elbow);
        } else {
            telemetry.addData("intake right elbow not initialized", 0);
        }
     }
    public void setRElbowPos(double elbow)
    {
        if ( rIntake != null ) {
            rIntake.setPosition(elbow);
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
        setLeftGripPos(LEFT_GRIP_DROP);
    }
    public void dropRight() {
        setRightGripPos(RIGHT_GRIP_DROP);
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
        setRElbowPos(pos.rElbowPos);
        setLElbowPos(pos.lElbowPos);
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
              //  goTo(Positions.INIT, false);

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
        if ( leftGripPos != LEFT_GRIP_DROP ) { dropLeft(); }
        else { holdPixelLeft(); }

        if ( rightGripPos != LEFT_GRIP_DROP ) { dropRight(); }
        else { holdPixelRight(); }
    }
    public boolean driveSlowly() { return moveSlowly; }
    public boolean clearedTransferZone() { return clearOfTransferZone; }

    public boolean gripperClosed() { return leftGripperClosed || rightGripperClosed; }

    public void setLeftMandiblePos(double leftMandPos) {
        leftMandible.setPosition(leftMandPos);
    }
    public void setRightMandiblePos(double rightMandPos) {
        rightMandible.setPosition(rightMandPos);
    }
    public void mandibleOpen() {
        setLeftMandiblePos(LEFT_MANDIBLE_OPEN);
        setRightMandiblePos(RIGHT_MANDIBLE_OPEN);
    }
    public void mandibleClose() {
        setLeftMandiblePos(LEFT_MANDIBLE_CLOSE);
        setRightMandiblePos(RIGHT_MANDIBLE_CLOSE);
    }
    public void leftMandibleOpen(){
        setLeftMandiblePos(LEFT_MANDIBLE_OPEN);
    }
    public void leftMandibleClose(){
        setLeftMandiblePos(LEFT_MANDIBLE_CLOSE);
    }
    public void rightMandibleOpen(){
        setRightMandiblePos(RIGHT_MANDIBLE_OPEN);
    }
    public void rightMandibleClose(){
        setRightMandiblePos(RIGHT_MANDIBLE_CLOSE);
    }
    public void leftMandibleToggle() {
        if (leftMandiblePos== LEFT_MANDIBLE_OPEN) { mandibleClose();}
        else {  mandibleOpen(); }
    }
    public void rightMandibleToggle()
    {
        if (rightMandiblePos != RIGHT_MANDIBLE_CLOSE) { mandibleClose();}
        else {  mandibleOpen(); }
    }


    public void update()
    {
        if (rIntake != null) {
            rIntakeElbowPos = rIntake.getPosition();
            lIntakeElbowPos = lIntake.getPosition();
            telemetry.addData("rintake position = ", rIntake.getPosition());
            telemetry.addData("lintake position = ", lIntake.getPosition());
            moveSlowly = (rIntakeElbowPos >= Positions.DOWN_TO_PIXEL.rElbowPos);
            clearOfTransferZone = (rIntakeElbowPos >= Positions.INIT.rElbowPos);
        }
        if (leftGripper != null) {
            double tempPos = leftGripper.getPosition();
            leftGripperClosed = tempPos != leftGripPos && tempPos == LEFT_GRIP_HOLD;
            leftGripPos = tempPos;
        }
        if (rightGripper != null) {
            double tempPos = rightGripper.getPosition();
            rightGripperClosed = tempPos != rightGripPos && tempPos == RIGHT_GRIP_HOLD;
            rightGripPos = tempPos;
        }
        if (leftMandible != null )
        {
            leftMandiblePos = leftMandible.getPosition();
        }
        if (rightMandible != null )
        {
            rightMandiblePos = rightMandible.getPosition();
        }
        if ( TELEM)
        {
            telemetry.addData("Intake Right Gripper Position =", rightGripPos);
            telemetry.addData("Intake Left Gripper Position =", leftGripPos);
            telemetry.addData("Intake Position: ", currentPosition);
            telemetry.addData("Drive Slowly: ", moveSlowly);
            telemetry.addData("Intake Clear of Transfer Zone: ", clearOfTransferZone);
        }
    }
}
