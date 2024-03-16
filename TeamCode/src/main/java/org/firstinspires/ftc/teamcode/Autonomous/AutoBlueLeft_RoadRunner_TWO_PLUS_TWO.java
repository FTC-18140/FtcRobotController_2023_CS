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
import org.firstinspires.ftc.teamcode.Robot.TGEVisionProcessor;
import org.firstinspires.ftc.teamcode.Robot.ThunderbotAuto2023;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(group = "autoblueleft")
@Config
public class AutoBlueLeft_RoadRunner_TWO_PLUS_TWO extends OpMode {

    SampleMecanumDrive drive;

    int bot_w = 8;
    int tagNum = 2;

    double spike_x;
    double spike_y;
    double spike_heading;
    double spike_tangent;

    static double backdrop_x;
    static double backdrop_y;

    static double backdrop2_x;
    static double backdrop2_y;

    final int TRUSS_IN_X = -36;
    final int TRUSS_IN_Y = -2;
    final int TRUSS_OUT_X = -36;
    final int TRUSS_OUT_Y = 26;

    final int STACK_X = -36;
    final int STACK_Y = 60;

    enum State{
        TEST,
        PURPLE,
        SPIKE_DROP,
        TO_BACKDROP,
        DROP_ON_BACKDROP,
        TRUSS1,
        STACK,
        TRUSS2,
        DROP_ON_BACKDROP2,
        PARK,
        IDLE
    }

    boolean done = false;
    enum Stack_State {
        TO_STACK,
        GRAB_FROM_STACK,
        MOVE_TO_TRANSFER,
        INTAKE_TO_TRANSFER,
        TRANSFER,
        GRIP,
        IDLE
    }
    Stack_State stack_step = Stack_State.TO_STACK;
    ElapsedTime spiketimer;

    Trajectory origin_test;
    Trajectory purple;
    Trajectory yellow;

    Trajectory truss1;
    Trajectory to_stack;
    Trajectory move_to_transfer;

    Trajectory truss2;
    Trajectory backdrop2;

    Trajectory park;

    State step = State.PURPLE;

    String tag = "RIGHT";
    ThunderbotAuto2023 robot = new ThunderbotAuto2023();


    @Override
    public void init(){
        robot.init(hardwareMap, telemetry, true);
        TGEVisionProcessor.theColor = "BLUE";
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
                spike_x = FieldConstants.BlueLeft.SPIKE_LEFT.x;
                spike_y = FieldConstants.BlueLeft.SPIKE_LEFT.y;
                spike_heading = FieldConstants.BlueLeft.SPIKE_LEFT.h;
                backdrop_x = FieldConstants.BlueLeft.BACKDROP_LEFT.x;
                backdrop_y = FieldConstants.BlueLeft.BACKDROP_LEFT.y;

                backdrop2_x = 51;
                backdrop2_y = 45;
                spike_tangent = Math.toRadians(-90);
                break;
            case(2):
                spike_x = FieldConstants.BlueLeft.SPIKE_CENTER.x;
                spike_y = FieldConstants.BlueLeft.SPIKE_CENTER.y;
                spike_heading = FieldConstants.BlueLeft.SPIKE_CENTER.h;
                backdrop_x = FieldConstants.BlueLeft.BACKDROP_CENTER.x;
                backdrop_y = FieldConstants.BlueLeft.BACKDROP_CENTER.y;

                backdrop2_x = 50.5;
                backdrop2_y = 32;
                spike_tangent = Math.toRadians(-90);
                break;
            case(3):
                spike_x = FieldConstants.BlueLeft.SPIKE_RIGHT.x;
                spike_y = FieldConstants.BlueLeft.SPIKE_RIGHT.y;
                spike_heading = FieldConstants.BlueLeft.SPIKE_RIGHT.h;
                backdrop_x = FieldConstants.BlueLeft.BACKDROP_RIGHT.x;
                backdrop_y = FieldConstants.BlueLeft.BACKDROP_RIGHT.y;

                backdrop2_x = 50.5;
                backdrop2_y = 32;
                spike_tangent = Math.toRadians(180);
                break;
        }

        Pose2d start = new Pose2d(FieldConstants.BlueLeft.START.x ,FieldConstants.BlueLeft.START.y, FieldConstants.BlueLeft.START.h);
        drive.setPoseEstimate(start);

        origin_test = drive.trajectoryBuilder(start)
                .lineTo(new Vector2d(24, 0))
                .build();

        purple = drive.trajectoryBuilder(start)
                .splineToLinearHeading(new Pose2d(spike_x, spike_y, spike_heading), spike_tangent)
                .build();

        yellow = drive.trajectoryBuilder(purple.end(), true)
                .splineToLinearHeading(new Pose2d(backdrop_x, backdrop_y, FieldConstants.BlueLeft.BACKDROP_RIGHT.h), Math.toRadians(0))
                .build();

        truss1 = drive.trajectoryBuilder(yellow.end())
                .splineToConstantHeading(new Vector2d(24, 55), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-40, 55), Math.toRadians(180))
                .build();

        to_stack = drive.trajectoryBuilder(truss1.end())
                .splineToConstantHeading(new Vector2d(-50, 26), Math.toRadians(100))
                .splineToConstantHeading(new Vector2d(-54, 30), Math.toRadians(-150), SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        move_to_transfer = drive.trajectoryBuilder(to_stack.end(), true)
                .splineToConstantHeading(new Vector2d(-48, 36), Math.toRadians(-45))
                .build();

        truss2 = drive.trajectoryBuilder(move_to_transfer.end(), true)
                .splineToConstantHeading(new Vector2d(-40, 51), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(24, 51), Math.toRadians(0))
                .build();

        backdrop2 = drive.trajectoryBuilder(truss2.end(), true)
                .splineToConstantHeading(new Vector2d(backdrop2_x, backdrop2_y), Math.toRadians(0))
                .build();
        park = drive.trajectoryBuilder(backdrop2.end())
                .splineToConstantHeading(new Vector2d(FieldConstants.BlueLeft.PARK.x, FieldConstants.BlueLeft.PARK.y), Math.toRadians(0))
                .build();

        drive.followTrajectoryAsync(purple);
    }

    public boolean stack_and_transfer() {
        boolean done = false;
        switch(stack_step){
            case TO_STACK:
                if(!drive.isBusy()){
                    robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);
                    robot.intake.dropBoth();
                    drive.followTrajectoryAsync(to_stack);
                    stack_step = Stack_State.GRAB_FROM_STACK;
                    spiketimer.reset();
                }
                break;
            case GRAB_FROM_STACK:
                if (!drive.isBusy()) {

                    robot.intake.mandibleClose();
                    robot.delivery.dropBoth();
                    drive.followTrajectoryAsync(move_to_transfer);
                    stack_step = Stack_State.MOVE_TO_TRANSFER;
                }
                break;
            case MOVE_TO_TRANSFER:
                if(!drive.isBusy()){
                    robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);
                    robot.intake.dropBoth();
                    stack_step = Stack_State.INTAKE_TO_TRANSFER;
                    spiketimer.reset();
                }
                break;
            case INTAKE_TO_TRANSFER:
                if(spiketimer.seconds() >= 0.7){
                    robot.intake.autoIntake(true);
                    stack_step = Stack_State.TRANSFER;
                    spiketimer.reset();
                }
                break;
            case TRANSFER:
                done = robot.intake.autoIntake(false);
                if(done){
                    stack_step = Stack_State.GRIP;
                    done = false;
                    spiketimer.reset();
                }
                break;
            case GRIP:
                if(spiketimer.seconds() >= 0.25){
                    robot.delivery.holdPixelsBoth();
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
    }
    @Override
    public void loop(){
        switch (step){
            case TEST:
                if(!drive.isBusy()){
                    step = State.IDLE;
                }
                break;
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
                if(spiketimer.seconds() >= 0.3){
                    step = State.TO_BACKDROP;
                    drive.followTrajectoryAsync(yellow);
                }
                break;
            case TO_BACKDROP:
                robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                if(!drive.isBusy()){
                    robot.delivery.dropBoth();
                    step = State.DROP_ON_BACKDROP;
                    spiketimer.reset();
                }
                break;
            case DROP_ON_BACKDROP:
                if(spiketimer.seconds() >= 1){

                    robot.delivery.goTo(Delivery.Positions.ALIGN_FOR_TRANSFER);
                    robot.intake.mandibleClose();

                    if(spiketimer.seconds() >=1.5){
                        step = State.TRUSS1;
                        spiketimer.reset();
                        drive.followTrajectoryAsync(truss1);
                    }
                }
                break;
            case TRUSS1:
                if(!drive.isBusy()){
                    robot.intake.mandibleOpen();
                    step = State.STACK;
                }
                break;
            case STACK:
                if(!done){
                    done = stack_and_transfer();
                }else{
                    step = State.TRUSS2;
                    drive.followTrajectoryAsync(truss2);
                }
                break;
            case TRUSS2:
                if(!drive.isBusy()){
                    robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                    drive.followTrajectoryAsync(backdrop2);
                    step = State.DROP_ON_BACKDROP2;
                }
                break;
            case DROP_ON_BACKDROP2:
                if(!drive.isBusy()){
                    robot.delivery.dropBoth();
                    step = State.PARK;
                    spiketimer.reset();
                    drive.followTrajectoryAsync(park);
                }
                break;
            case PARK:
                if(spiketimer.seconds() >= 0.25){
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

