package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Arrays;

@Config
public class TBDGamepad
{
    public Gamepad gamepad;

    public static double expoYValue = 2.5;
    public static double expoXValue = 2.5;
    public boolean buttons[] = new boolean[14];
    public boolean oldButtons[] = new boolean[14];
    public boolean changed[] = new boolean[14];


    public enum Button {
        Y(0), X(1), A(2), B(3), LEFT_BUMPER(4), RIGHT_BUMPER(5), BACK(6),
        START(7), DPAD_UP(8), DPAD_DOWN(9), DPAD_LEFT(10), DPAD_RIGHT(11),
        LEFT_STICK_BUTTON(12), RIGHT_STICK_BUTTON(13);
        final int index;
        Button( int ind )
        {
            this.index = ind;
        }
    }

    public enum Trigger {
        LEFT_TRIGGER, RIGHT_TRIGGER
    }

    public enum Stick {
        LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y
    }

    public TBDGamepad(Gamepad gamepad)
    {
        this.gamepad = gamepad;
        Arrays.fill(buttons, false);
        Arrays.fill(oldButtons, false);
        Arrays.fill(changed, false);
    }

    /**
     * @param button the button object
     * @return the boolean value as to whether the button is active or not
     */
    public boolean getButton(Button button) {
        boolean buttonValue = false;
        switch (button) {
            case A:
                buttonValue = gamepad.a;
                break;
            case B:
                buttonValue = gamepad.b;
                break;
            case X:
                buttonValue = gamepad.x;
                break;
            case Y:
                buttonValue = gamepad.y;
                break;
            case LEFT_BUMPER:
                buttonValue = gamepad.left_bumper;
                break;
            case RIGHT_BUMPER:
                buttonValue = gamepad.right_bumper;
                break;
            case DPAD_UP:
                buttonValue = gamepad.dpad_up;
                break;
            case DPAD_DOWN:
                buttonValue = gamepad.dpad_down;
                break;
            case DPAD_LEFT:
                buttonValue = gamepad.dpad_left;
                break;
            case DPAD_RIGHT:
                buttonValue = gamepad.dpad_right;
                break;
            case BACK:
                buttonValue = gamepad.back;
                break;
            case START:
                buttonValue = gamepad.start;
                break;
            case LEFT_STICK_BUTTON:
                buttonValue = gamepad.left_stick_button;
                break;
            case RIGHT_STICK_BUTTON:
                buttonValue = gamepad.right_stick_button;
                break;
            default:
                buttonValue = false;
                break;
        }
        return buttonValue;
    }

    /**
     * @param trigger the trigger object
     * @return the value returned by the trigger in question
     */
    public double getTrigger(Trigger trigger) {
        double triggerValue = 0;
        switch (trigger) {
            case LEFT_TRIGGER:
                triggerValue = gamepad.left_trigger;
                break;
            case RIGHT_TRIGGER:
                triggerValue = gamepad.right_trigger;
                break;
            default:
                break;
        }
        return triggerValue;
    }

    /**
     * @return the y-value on the left analog stick
     */
    public double getLeftY() {
        return -gamepad.left_stick_y;
    }


    /**
     * @return the y-value on the right analog stick
     */
    public double getRightY() {
        return -gamepad.right_stick_y;
    }

    /**
     * @return the x-value on the left analog stick
     */
    public double getLeftX() {
        return gamepad.left_stick_x;
    }

    /**
     * @return the x-value on the right analog stick
     */
    public double getRightX() {
        return gamepad.right_stick_x;
    }

    public double getExpo( Stick stick )
    {
        switch (stick)
        {
            case LEFT_X:
                return Math.pow(getLeftX(), expoXValue);
            case LEFT_Y:
                return Math.pow(getLeftY(), expoYValue);
            case RIGHT_X:
                return Math.pow(getRightX(), expoXValue);
            case RIGHT_Y:
                return Math.pow(getRightY(), expoYValue);
            default:
                return 0;
        }
    }

    public void update() {
        boolean val_A;
        boolean val_b;
        boolean val_X;
        boolean val_Y;
        boolean val_leftBumper;
        boolean val_right_bumper;
        boolean val_dpadUp;
        boolean val_dpadDown:
        boolean val_dpadRight;
        boolean val_dpadLeft;
        boolean val_back;
        boolean val_start;
        boolean val_leftStick;
        boolean val_rightStick;

        System.arraycopy(buttons, 0, oldButtons, 0, 14);

        buttons[Button.A.index] = gamepad.a;

        buttons[Button.B.index] = gamepad.b;

        buttons[Button.X.index] = gamepad.x;

        buttons[Button.Y.index] = gamepad.y;

        buttonValue = gamepad.left_bumper;

        buttonValue = gamepad.right_bumper;

        buttonValue = gamepad.dpad_up;

        buttonValue = gamepad.dpad_down;

        buttonValue = gamepad.dpad_left;

        buttonValue = gamepad.dpad_right;

        buttonValue = gamepad.back;

        buttonValue = gamepad.start;

        buttonValue = gamepad.left_stick_button;

        buttonValue = gamepad.right_stick_button;

        for ( int i = 0; i < 14; i++ )
        {
            changed[i] = oldButtons[i] != buttons[i];
        }
    }


}
