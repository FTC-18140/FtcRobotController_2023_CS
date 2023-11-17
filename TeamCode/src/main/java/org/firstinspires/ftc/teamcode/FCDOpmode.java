//package org.firstinspires.ftc.teamcode;
//
//import com.arcrobotics.ftclib.drivebase.MecanumDrive;
//import com.arcrobotics.ftclib.gamepad.GamepadEx;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//@TeleOp
//public class FCDOpmode extends OpMode {
//
//    Thunderbot2023 robot = new Thunderbot2023();
//    MecanumDrive mecanum = new MecanumDrive(robot.leftFront1, robot.rightFront1, robot.leftRear1, robot.rightRear1);
//    private GamepadEx driverOp;
//    @Override
//    public void init(){
//        driverOp = new GamepadEx(gamepad1);
//    }
//    @Override
//    public void loop() {
//        mecanum.driveFieldCentric(
//                driverOp.getLeftX(),
//                driverOp.getLeftY(),
//                driverOp.getRightX(),
//                robot.heading
//        );
//
//        robot.update();
//    }
//}
//
//
