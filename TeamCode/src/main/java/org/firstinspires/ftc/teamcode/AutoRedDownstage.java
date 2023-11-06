package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous
public class AutoRedDownstage extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    Telemetry telemetry;
    HardwareMap hwMap = null;
    int state = 0;
    boolean done = false;

    @Override
    public void init() {
        robot.init(hwMap, telemetry, false);

    }
    @Override
    public void start() {

    }
    @Override
    public void loop() {
        switch (state) {
            case 0:
            if (!done) {
                done = robot.drive(80, 0.5);
            } else {
                robot.stop();
                done = false;
                state++;
            }

            // TODO Add the drop pixel command

            if (!done) {
                done = robot.turn(90, 0.5);
            } else {
                robot.stop();
                done = false;
                state++;
            }
                if (!done) {
                    done = robot.drive(75, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                // TODO test code then add other steps from notebook

        }
        telemetry.addData("step: ", state);
    }
}
