package org.firstinspires.ftc.teamcode.SamplesAndTesting;

import static com.qualcomm.robotcore.util.Range.clip;


import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.TBDGamepad;
import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;



@TeleOp(name = "Strafer", group = "teleop")
public class Strafer_Chassis extends OpMode{

    Thunderbot2023 robot = new Thunderbot2023();

    boolean toggle = false;

    TBDGamepad tbdGamepad1;
    TBDGamepad tbdGamepad2;

    @Override
    public void init()
    {
        tbdGamepad1 = new TBDGamepad(gamepad1);
        tbdGamepad2 = new TBDGamepad(gamepad2);
        telemetry.addData("Init", "Start");
        robot.init(hardwareMap, telemetry, false, true);
        telemetry.addData("Init", "Done");
    }

    /**
     * User-defined init_loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the init button is pressed and when the play button is pressed (or the
     * OpMode is stopped).
     * <p>
     * This method is optional. By default, this method takes no action.
     */
    @Override
    public void init_loop() {}

    @Override
    public void start() {}

    public void loop() {
    robot.update();
    tbdGamepad1.update();
    robot.joystickDrive(-tbdGamepad1.getLeftY(), -tbdGamepad1.getLeftX(), -tbdGamepad1.getRightX());
    }
}