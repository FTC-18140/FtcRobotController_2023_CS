package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.ThunderbotAuto2023;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(group = "redright")
@Config
public class AutoRedRight_RoadRunner_ParkLeft extends OpMode {

    SampleMecanumDrive drive;

    int bot_w = 8;
    int tagNum = 2;

    double spike_x;
    double spike_y;
    double spike_heading;
    double spike_tangent;

    double backdrop_x;
    double backdrop_y;

    final int TRUSS_IN_X = -36;
    final int TRUSS_IN_Y = -2;
    final int TRUSS_OUT_X = -36;
    final int TRUSS_OUT_Y = 26;

    final int STACK_X = -36;
    final int STACK_Y = 60;

    enum State{
        PURPLE,
        SPIKE_DROP,
        TO_BACKDROP,
        DROP_ON_BACKDROP,
        PARK,
        IDLE
    }

    boolean done = false;

    ElapsedTime spiketimer;

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

    Trajectory origin_x;
    Trajectory purple;
    Trajectory yellow;

    Trajectory truss1;
    Trajectory park;

    State step = State.PURPLE;

    String tag = "RIGHT";
    ThunderbotAuto2023 robot = new ThunderbotAuto2023();

    @Override
    public void init(){

        robot.init(hardwareMap, telemetry, true);
        drive = robot.drive;
        spiketimer = new ElapsedTime();
        //0.9083333
    }

    @Override
    public void init_loop(){
        super.init_loop();
        switch (robot.eyes.getSpikePos())
        {
            case "LEFT":
            case "NOT FOUND":
                tagNum = 1;
                telemetry.addData("ZONE = LEFT", 0);
                break;
            case "RIGHT":
                tagNum = 3;
                telemetry.addData("ZONE = RIGHT", 0);
                break;
            default: // default CENTER
                tagNum = 2;
                telemetry.addData("ZONE = CENTER", 0);
                break;
        }
        telemetry.addData("Tag Number: ", tagNum );
    }

    @Override
    public void start(){
        switch(tagNum){
            case(1):
                spike_x = FieldConstants.RedRight.SPIKE_LEFT.x;
                spike_y = FieldConstants.RedRight.SPIKE_LEFT.y;
                spike_heading = FieldConstants.RedRight.SPIKE_LEFT.h;
                backdrop_x = FieldConstants.RedRight.BACKDROP_LEFT.x;
                backdrop_y = FieldConstants.RedRight.BACKDROP_LEFT.y;
                spike_tangent = Math.toRadians(140);
                break;
            case(2):
                spike_x = FieldConstants.RedRight.SPIKE_CENTER.x;
                spike_y = FieldConstants.RedRight.SPIKE_CENTER.y;
                spike_heading = FieldConstants.RedRight.SPIKE_CENTER.h;
                backdrop_x = FieldConstants.RedRight.BACKDROP_CENTER.x;
                backdrop_y = FieldConstants.RedRight.BACKDROP_CENTER.y;
                spike_tangent = Math.toRadians(100);
                break;
            case(3):
                spike_x = FieldConstants.RedRight.SPIKE_RIGHT.x;
                spike_y = FieldConstants.RedRight.SPIKE_RIGHT.y;
                spike_heading = FieldConstants.RedRight.SPIKE_RIGHT.h;
                backdrop_x = FieldConstants.RedRight.BACKDROP_RIGHT.x;
                backdrop_y = FieldConstants.RedRight.BACKDROP_RIGHT.y;
                spike_tangent = Math.toRadians(100);
                break;


        }

        Pose2d start = new Pose2d(FieldConstants.RedRight.START.x ,FieldConstants.RedRight.START.y, FieldConstants.RedRight.START.h);

        drive.setPoseEstimate(start);
        origin_x = drive.trajectoryBuilder(start)
                .splineToLinearHeading(new Pose2d(24, -34, Math.toRadians(120)), Math.toRadians(100))
                .build();


        purple = drive.trajectoryBuilder(start)
                .splineToLinearHeading(new Pose2d(spike_x, spike_y, spike_heading), spike_tangent)
                .build();

        yellow = drive.trajectoryBuilder(purple.end(), true)
                .splineToLinearHeading(new Pose2d(backdrop_x, backdrop_y, FieldConstants.RedRight.BACKDROP_RIGHT.h), Math.toRadians(0))
                .build();

        truss1 = drive.trajectoryBuilder(yellow.end())
                .splineToSplineHeading(new Pose2d(TRUSS_IN_X, TRUSS_IN_Y, Math.toRadians(90)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(TRUSS_OUT_X, TRUSS_OUT_Y), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(STACK_X, STACK_Y, Math.toRadians(0)), Math.toRadians(0))
                .build();

        park = drive.trajectoryBuilder(yellow.end())
                .splineToConstantHeading(new Vector2d(FieldConstants.RedRight.PARK_LEFT.x, FieldConstants.RedRight.PARK_LEFT.y), Math.toRadians(0))
                .build();

        drive.followTrajectoryAsync(purple);
    }
    @Override
    public void loop(){
        switch (step){
            case PURPLE:
                robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);
                robot.intake.mandibleOpen();
                if(!drive.isBusy()){
                    step = State.SPIKE_DROP;
                    spiketimer.reset();
                }
                break;
            case SPIKE_DROP:
                robot.intake.dropBoth();
                if(spiketimer.seconds() >= 0.5){
                    step = State.TO_BACKDROP;
                    drive.followTrajectoryAsync(yellow);
                }
                break;
            case TO_BACKDROP:
                robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                if(!drive.isBusy()){
                    robot.intake.mandibleClose();
                    robot.delivery.dropBoth();
                    step = State.DROP_ON_BACKDROP;
                    spiketimer.reset();
                }
                break;
            case DROP_ON_BACKDROP:
                if(spiketimer.seconds() >= 1){
                    step = State.PARK;
                    spiketimer.reset();
                    drive.followTrajectoryAsync(park);
                }
                break;
            case PARK:
                if(spiketimer.seconds() >= 0.5){
                    robot.delivery.goTo(Delivery.Positions.TELE_INIT);
                }
                if(!drive.isBusy()){
                    step = State.IDLE;
                }
                break;
            default:
                break;
        }

        //drive.update();
        robot.update();

    }
}

