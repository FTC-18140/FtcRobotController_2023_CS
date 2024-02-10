package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
public class RRTestRedRight extends OpMode {

    SampleMecanumDrive drive;

    int bot_w = 8;
    int tagNum = 2;
    final int START_X = -60;
    final int START_Y = -12;

    final int END_X = -58;
    final int END_Y = -58;

    final int SPIKE_L_X = -34;
    final int SPIKE_M_X = -24 - bot_w;
    final int SPIKE_R_X = -34;

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

    final int STACK_X = -36;
    final int STACK_Y = 60;

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

    int step = 0;

    String tag = "RIGHT";
    Thunderbot2023 robot = new Thunderbot2023();

    @Override
    public void init(){
        drive = new SampleMecanumDrive(hardwareMap);
        robot.init(hardwareMap, telemetry, true, true);
        //0.9083333
    }

    @Override
    public void init_loop(){
        switch (tag)
        {
            case "LEFT":
                tagNum = 1;
                break;
            case "RIGHT":
                tagNum = 3;
                break;
            default: // default CENTER
                tagNum = 2;
                break;
        }
        telemetry.addData("Tag Number: ", tagNum );
    }

    @Override
    public void start(){
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

        Pose2d start = new Pose2d(START_X ,START_Y, Math.toRadians(0));

        drive.setPoseEstimate(start);
        purple = drive.trajectoryBuilder(start)
                .strafeTo(new Vector2d(spike_x, spike_y))
                .build();

        yellow = drive.trajectoryBuilder(purple.end())
                .lineToSplineHeading(new Pose2d(backdrop_x, backdrop_y, Math.toRadians(-90)))
                .build();

        truss1 = drive.trajectoryBuilder(yellow.end())
                .splineToSplineHeading(new Pose2d(TRUSS_IN_X, TRUSS_IN_Y, Math.toRadians(90)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(TRUSS_OUT_X, TRUSS_OUT_Y), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(STACK_X, STACK_Y, Math.toRadians(0)), Math.toRadians(0))
                .build();

        park = drive.trajectoryBuilder(new Pose2d(START_X, START_Y, Math.toRadians(0)))
                .strafeTo(new Vector2d(END_X, END_Y))
                .build();


    }
    @Override
    public void loop(){
        if(!drive.isBusy() && !done){
            drive.followTrajectoryAsync(purple);
            done = true;
        }
        drive.update();
        robot.update();

    }
}

