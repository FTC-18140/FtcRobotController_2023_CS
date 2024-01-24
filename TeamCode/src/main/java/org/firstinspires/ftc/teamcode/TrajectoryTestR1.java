package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
public class TrajectoryTestR1 extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Trajectory step1 = drive.trajectoryBuilder(new Pose2d())
                .strafeTo(new Vector2d(30, 30))
                .splineToSplineHeading(new Pose2d(45, 0, Math.toRadians(-90)), Math.toRadians(0))
                .build();

        Trajectory step2 = drive.trajectoryBuilder(step1.end())
                .splineToSplineHeading(new Pose2d(0, 0, Math.toRadians(0)), Math.toRadians(180))
                .build();

        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectory(step1);

        drive.followTrajectory(step2);
    }
}
