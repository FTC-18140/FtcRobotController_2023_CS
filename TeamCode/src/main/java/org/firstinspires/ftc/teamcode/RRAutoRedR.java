package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
public class RRAutoRedR extends OpMode {

    SampleMecanumDrive drive;

    int spike = 1;
    final int START_X = -58;
    final int START_Y = -12;

    final int END_X = -58;
    final int END_Y = -58;

    final int SPIKE_L_X = -28;
    final int SPIKE_M_X = -24;
    final int SPIKE_R_X = -28;

    final int SPIKE_L_Y = 0;
    final int SPIKE_M_Y = -12;
    final int SPIKE_R_Y = -24;

    int spike_x;
    int spike_y;

    final int BACKDROP_L_X = -30;
    final int BACKDROP_M_X = -36;
    final int BACKDROP_R_X = -42;

    final int BACKDROP_L_Y = -48;
    final int BACKDROP_M_Y = -48;
    final int BACKDROP_R_Y = -48;

    int backdrop_x;
    int backdrop_y;

    final int TRUSS_IN_X = -36;
    final int TRUSS_IN_Y = -2;
    final int TRUSS_OUT_X = -36;
    final int TRUSS_OUT_Y = 26;

    boolean done = false;

    /*
    * START
    * Place Purple^
    * Place Yellow^+>
    * >BONUS:
    *  Go through truss
    *  Exit truss
    *  Go to pixel stack
    *  run into stack
    *  grab pixel(s)
    *  transfer pixel(s)
    *  travel through stage door
    *  go to backdrop
    *  place pixel
    * PARK
    * */

    Trajectory purple;
    Trajectory yellow;

    Trajectory truss1;
    Trajectory park;
    //Thunderbot2023 robot = new Thunderbot2023();

    @Override
    public void init(){
        drive = new SampleMecanumDrive(hardwareMap);
        //robot.init(hardwareMap, telemetry, true);
        //0.9083333
    }
    @Override
    public void start(){
        switch(spike){
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
        purple = drive.trajectoryBuilder(new Pose2d(START_X ,START_Y, Math.toRadians(0)))
                .strafeTo(new Vector2d(spike_x, spike_y))
                .build();

        yellow = drive.trajectoryBuilder(purple.end())
                .lineToSplineHeading(new Pose2d(backdrop_x, backdrop_y, Math.toRadians(-90)))
                .build();

        truss1 = drive.trajectoryBuilder(yellow.end())
                .splineToSplineHeading(new Pose2d(TRUSS_IN_X, TRUSS_IN_Y, Math.toRadians(90)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(TRUSS_OUT_X, TRUSS_OUT_Y), Math.toRadians(0))
                .build();

        park = drive.trajectoryBuilder(new Pose2d(START_X, START_Y, Math.toRadians(0)))
                .strafeTo(new Vector2d(END_X, END_Y))
                .build();

        drive.followTrajectoryAsync(park);
    }
    @Override
    public void loop(){
        drive.update();
        //robot.update();

    }
}