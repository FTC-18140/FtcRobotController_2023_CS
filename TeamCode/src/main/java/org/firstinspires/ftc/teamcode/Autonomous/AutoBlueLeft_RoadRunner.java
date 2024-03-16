package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.TGEVisionProcessor;
import org.firstinspires.ftc.teamcode.Robot.ThunderbotAuto2023;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(group = "autoblueleft")
public class AutoBlueLeft_RoadRunner extends OpMode {
    SampleMecanumDrive drive;
    int tagNum = 2;

    double spike_x;
    double spike_y;
    double spike_heading;
    double spike_tangent;

    double backdrop_x;
    double backdrop_y;

    enum State{
        PURPLE,
        SPIKE_DROP,
        TO_BACKDROP,
        DROP_ON_BACKDROP,
        PARK,
        IDLE
    }
    ElapsedTime spiketimer;

    Trajectory purple;
    Trajectory yellow;
    Trajectory park;
    State step = State.PURPLE;

    String tag = "RIGHT";
    ThunderbotAuto2023 robot = new ThunderbotAuto2023();

    @Override
    public void init(){

        robot.init(hardwareMap, telemetry, true);
        drive = robot.drive;
        TGEVisionProcessor.theColor = "BLUE";
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
                spike_x = FieldConstants.BlueLeft.SPIKE_LEFT.x;
                spike_y = FieldConstants.BlueLeft.SPIKE_LEFT.y;
                spike_heading = FieldConstants.BlueLeft.SPIKE_LEFT.h;
                backdrop_x = FieldConstants.BlueLeft.BACKDROP_LEFT.x;
                backdrop_y = FieldConstants.BlueLeft.BACKDROP_LEFT.y;
                spike_tangent = Math.toRadians(-90);
                break;
            case(2):
                spike_x = FieldConstants.BlueLeft.SPIKE_CENTER.x;
                spike_y = FieldConstants.BlueLeft.SPIKE_CENTER.y;
                spike_heading = FieldConstants.BlueLeft.SPIKE_CENTER.h;
                backdrop_x = FieldConstants.BlueLeft.BACKDROP_CENTER.x;
                backdrop_y = FieldConstants.BlueLeft.BACKDROP_CENTER.y;
                spike_tangent = Math.toRadians(-90);
                break;
            case(3):
                spike_x = FieldConstants.BlueLeft.SPIKE_RIGHT.x;
                spike_y = FieldConstants.BlueLeft.SPIKE_RIGHT.y;
                spike_heading = FieldConstants.BlueLeft.SPIKE_RIGHT.h;
                backdrop_x = FieldConstants.BlueLeft.BACKDROP_RIGHT.x;
                backdrop_y = FieldConstants.BlueLeft.BACKDROP_RIGHT.y;
                spike_tangent = Math.toRadians(180);
                break;


        }

        Pose2d start = new Pose2d(FieldConstants.BlueLeft.START.x ,FieldConstants.BlueLeft.START.y, FieldConstants.BlueLeft.START.h);

        drive.setPoseEstimate(start);


        purple = drive.trajectoryBuilder(start)
                .splineToLinearHeading(new Pose2d(spike_x, spike_y, spike_heading), spike_tangent)
                .build();

        yellow = drive.trajectoryBuilder(purple.end(), true)
                .splineToLinearHeading(new Pose2d(backdrop_x, backdrop_y, FieldConstants.BlueLeft.BACKDROP_RIGHT.h), Math.toRadians(0))
                .build();

        park = drive.trajectoryBuilder(yellow.end())
                .splineToConstantHeading(new Vector2d(FieldConstants.BlueLeft.PARK.x, FieldConstants.BlueLeft.PARK.y), Math.toRadians(0))
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
