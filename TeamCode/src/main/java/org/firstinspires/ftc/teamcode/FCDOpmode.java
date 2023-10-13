package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import kotlin.OverloadResolutionByLambdaReturnType;

@TeleOp
public class FCDOpmode extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    MecanumDrive mecanum = new MecanumDrive(robot.leftFront, robot.rightFront, robot.leftRear, robot.rightRear);
    private GamepadEx driverOp;
    @Override
    public void init(){
        driverOp = new GamepadEx(gamepad1);
    }
    @Override
    public void loop() {
//        mecanum.driveRobotCentric(
//                driverOp.getLeftX(),
//                driverOp.getLeftY(),
//                driverOp.getRightX()
//        );
    }
}


