package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
@Config
@Disabled
public class TrajectoryTestR1 extends LinearOpMode {
    int bot_w = 8;
    int tagNum = 3;
    final int START_X = -71 + bot_w;
    final int START_Y = -12;
    Pose2d start = new Pose2d(START_X,START_Y,Math.toRadians(0));

    final int END_X = -58;
    final int END_Y = -58;

    final int SPIKE_L_X = -34;
    final int SPIKE_M_X = -25 - bot_w;
    final int SPIKE_R_X = -31 - bot_w;

    final int SPIKE_L_Y = 0;
    final int SPIKE_M_Y = -12;
    final int SPIKE_R_Y = -24;

    int spike_x;
    int spike_y;

    final double BACKDROP_L_X = FieldConstants.RedRight.BACKDROP_LEFT.x;
    final double BACKDROP_M_X = FieldConstants.RedRight.BACKDROP_CENTER.x;
    final double BACKDROP_R_X = FieldConstants.RedRight.BACKDROP_RIGHT.x;

    final double BACKDROP_L_Y = FieldConstants.RedRight.BACKDROP_LEFT.y;
    final int BACKDROP_M_Y = -48;
    final int BACKDROP_R_Y = -48;

    double backdrop_x;
    double backdrop_y;

    final int TRUSS_IN_X = -36;
    final int TRUSS_IN_Y = -12;
    final int TRUSS_OUT_X = -36;
    final int TRUSS_OUT_Y = 26;

    final int STACK_X = -36;
    final int STACK_Y = 60;

    Trajectory origin_x;
    Trajectory purple;
    Trajectory yellow;

    Trajectory truss1;
    Trajectory park;
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        switch(tagNum){
            case(1):
                spike_x = SPIKE_L_X;
                spike_y = SPIKE_L_Y;
                backdrop_x = BACKDROP_L_X;
                backdrop_y = BACKDROP_L_Y;
                break;
            case(2):
                spike_x = SPIKE_M_X;
                spike_y = SPIKE_M_Y;
                backdrop_x = BACKDROP_M_X;
                backdrop_y = BACKDROP_M_Y;
                break;
            case(3):
                spike_x = SPIKE_R_X;
                spike_y = SPIKE_R_Y;
                backdrop_x = BACKDROP_R_X;
                backdrop_y = BACKDROP_R_Y;
                break;


        }

        drive.setPoseEstimate(start);
        origin_x = drive.trajectoryBuilder(start)
                .lineTo(new Vector2d(0,START_Y))
                .build();

        purple = drive.trajectoryBuilder(start)
                .strafeTo(new Vector2d(spike_x, spike_y))
                .build();

        yellow = drive.trajectoryBuilder(purple.end())
                .back(12)
                .splineToSplineHeading(new Pose2d(backdrop_x, backdrop_y, Math.toRadians(90)), Math.toRadians(0))
                .build();

        truss1 = drive.trajectoryBuilder(yellow.end())
                .splineToConstantHeading(new Vector2d(TRUSS_IN_X, TRUSS_IN_Y), Math.toRadians(-90))
                .build();

        park = drive.trajectoryBuilder(new Pose2d(START_X, START_Y, Math.toRadians(0)))
                .strafeTo(new Vector2d(END_X, END_Y))
                .build();

        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectory(purple);
        drive.followTrajectory(yellow);
        drive.followTrajectory(truss1);

        //drive.followTrajectory(step2);
    }
}
