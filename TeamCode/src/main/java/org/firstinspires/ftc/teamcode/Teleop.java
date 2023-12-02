package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    Thunderbot2023 robot = new Thunderbot2023();
    TBDGamepad gp1= new TBDGamepad(gamepad1);
    @Override
    public void init() {
        telemetry.addData("Init", "Start");

        robot.init(hardwareMap, telemetry, false);

        telemetry.addData("Init", "Done");
    }

    @Override
    public void start() {}

    public void loop() {

        robot.update();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////// GAMEPAD 1 //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //////////////
        // DRIVING
        //////////////
        if (gp1.getButton(TBDGamepad.Button.LEFT_STICK_BUTTON) && gp1.getButton(TBDGamepad.Button.RIGHT_STICK_BUTTON)) {
            robot.imu.resetYaw();
            telemetry.addData("imu: ", "reset");
        }

        if (gp1.getTrigger(TBDGamepad.Trigger.LEFT_TRIGGER) > 0.5) {
            //Expo
            robot.orientedDrive(gp1.getExpo(TBDGamepad.Stick.LEFT_Y), gp1.getExpo(TBDGamepad.Stick.LEFT_X), gp1.getRightX());
        } else if (gp1.getButton(TBDGamepad.Button.A)){
            //Oriented
            robot.orientedDrive(gp1.getLeftY() * 0.6, gp1.getLeftX() * 0.6, gp1.getRightX());
        } else {
            robot.joystickDrive(gp1.getExpo(TBDGamepad.Stick.LEFT_Y), gp1.getExpo(TBDGamepad.Stick.LEFT_X), gp1.getRightX());
        }
    }
}