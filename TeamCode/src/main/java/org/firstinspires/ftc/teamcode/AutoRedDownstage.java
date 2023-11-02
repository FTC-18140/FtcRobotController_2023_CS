package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous
public class AutoRedDownstage {

    Thunderbot2023 robot = new Thunderbot2023();
    int step = 0;
    boolean done = false;


    public void init(HardwareMap hwMap, Telemetry telem) {
        robot.init(hwMap, telem, false);

    }
    public void start() {

    }
    public void loop() {

        if(!done) {



        } else {
            done = false;
            step++;
        }
    }
}
