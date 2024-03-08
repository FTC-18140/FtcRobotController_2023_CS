package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.ThunderbotAuto2023;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@Autonomous
public class RRAttachmentTest extends OpMode{

    ThunderbotAuto2023 robot;
    SampleMecanumDrive drive;

    enum State {
        IDLE_1,
        FORWARD_1,
        ALIGN_1,
        GRAB_1,
        LIFT_1,
        TRANSFER_1
    }

    final int START_X = -58;
    final int START_Y = -12;

    final int END_X = 0;
    final int END_Y = -12;

    RRAttachmentTest.State step = State.IDLE_1;

    Trajectory forward_1;

    public void init(){
        robot = new ThunderbotAuto2023();
        robot.init(hardwareMap, telemetry, false);
        drive = robot.drive;

        forward_1 = drive.trajectoryBuilder(new Pose2d(START_X,START_Y, Math.toRadians(0)))
                .lineToConstantHeading(new Vector2d(END_X,END_Y))
                .build();

    }

    public void loop(){
        robot.update();

        switch (step){
            case IDLE_1:
                step = State.FORWARD_1;
                break;
            case FORWARD_1:
                if(!drive.isBusy()){
                    drive.followTrajectoryAsync(forward_1);
                    step = State.ALIGN_1;
                }
                break;
            default:
                break;
        }
    }
}
