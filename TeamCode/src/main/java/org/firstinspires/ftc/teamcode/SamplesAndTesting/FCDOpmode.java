package org.firstinspires.ftc.teamcode.SamplesAndTesting;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;

@TeleOp
public class FCDOpmode extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    @Override
    public void init(){
    }
    @Override
    public void loop() {


        robot.update();
    }
}


