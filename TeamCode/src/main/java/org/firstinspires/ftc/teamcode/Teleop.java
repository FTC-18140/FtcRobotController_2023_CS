package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.TBDGamepad.Stick.LEFT_X;
import static org.firstinspires.ftc.teamcode.TBDGamepad.Stick.LEFT_Y;
import static org.firstinspires.ftc.teamcode.TBDGamepad.Stick.RIGHT_X;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    Thunderbot2023 robot = new Thunderbot2023();
    TBDGamepad thegamepad1 = new TBDGamepad(gamepad1);

    @Override
    public void init()
    {
        telemetry.addData("Init", "Start");
        robot.init(hardwareMap, telemetry, false);
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
    public void init_loop()
    {
        super.init_loop();
        String spikePos = robot.eyes.getSpikePos();
        telemetry.addData("Spike Pos = ", spikePos);
        telemetry.addData("Tag Number: ", robot.eyes.getTagNumber(4) );
        telemetry.addData("Tag X:", robot.eyes.tgeFinder.xPos);
        telemetry.addData("Tag Y:",  robot.eyes.tgeFinder.yPos);

    }

    @Override
    public void start()
    {
    }

    public void loop()
    {
        robot.update();
        robot.orientedDrive(thegamepad1.getExpo(LEFT_Y), thegamepad1.getExpo(LEFT_X), thegamepad1.getExpo(RIGHT_X));

        String spikePos = robot.eyes.getSpikePos();
        telemetry.addData("Spike Pos = ", spikePos);
        telemetry.addData("Tag Number: ", robot.eyes.getTagNumber(4) );
        telemetry.addData("Tag X:", robot.eyes.tgeFinder.xPos);
        telemetry.addData("Tag Y:",  robot.eyes.tgeFinder.yPos);

    }
}