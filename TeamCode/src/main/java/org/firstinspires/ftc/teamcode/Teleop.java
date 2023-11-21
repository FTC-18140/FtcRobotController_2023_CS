package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    Thunderbot2023 robot = new Thunderbot2023();

    @Override
    public void init()
    {
        telemetry.addData("Init", "Start");
        
        robot.update();

        robot.init(hardwareMap, telemetry, false);

        String spikePos = robot.eyes.getSpikePos();
        telemetry.addData("Spike Pos = ", spikePos);
        telemetry.addData("Tag Number: ", robot.eyes.getTagPos() );
        telemetry.addData("Tag X:", robot.eyes.tgeFinder.xPos);
        telemetry.addData("Tag Y:",  robot.eyes.tgeFinder.yPos);


        telemetry.addData("Init", "Done");
    }

    @Override
    public void start()
    {
    }

    public void loop()
    {
        robot.update();
        robot.orientedDrive(-gamepad1.left_stick_y * 0.6, gamepad1.left_stick_x * 0.6, gamepad1.right_stick_x);
        String spikePos = robot.eyes.getSpikePos();
        telemetry.addData("Spike Pos = ", spikePos);
        telemetry.addData("Tag Number: ", robot.eyes.getTagPos() );
        telemetry.addData("Tag X:", robot.eyes.tgeFinder.xPos);
        telemetry.addData("Tag Y:",  robot.eyes.tgeFinder.yPos);

    }
}