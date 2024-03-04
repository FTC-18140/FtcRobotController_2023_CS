package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.ThunderbotAuto2023;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(group = "test")
public class RedLeft_Experimental extends OpMode {

    ThunderbotAuto2023 robot = new ThunderbotAuto2023();
    SampleMecanumDrive drive;

    Pose2d start = new Pose2d(FieldConstants.RedLeft2.START.x,FieldConstants.RedLeft2.START.y, FieldConstants.RedLeft2.START.h);

    ElapsedTime spiketimer;
    int tagNum = 2;

    double spike_x;
    double spike_y;
    double spike_heading;
    double spike_tangent;

    double backup_x;
    double backup_y;
    double backup_heading = Math.toRadians(90);

    double backdrop_x;
    double backdrop_y;

    Trajectory origin_test;

    TrajectorySequence purple;
    Trajectory align_to_stack;
    Trajectory to_stack;
    Trajectory move_to_transfer;
    Trajectory yellow;
    Trajectory to_backdrop;
    Trajectory park;

    Trajectory backup;
    Trajectory knockover;
    Trajectory backfromstack;

    enum State{
        TEST,
        PURPLE,
        SPIKE_DROP,
        BACKUP,
        ALIGN_TO_STACK,
        TO_STACK,
        KNOCK_OVER,
        BACK_TO_STACK,
        GRAB_FROM_STACK,
        MOVE_TO_TRANSFER,
        TRANSFER_INTAKE,
        INTAKE_RELEASE,
        TRANSFER_DELIVERY,
        DELIVERY_GRIP,
        TO_BACKDROP,
        DELIVERY_LIFT,
        DROP_ON_BACKDROP,
        PARK,
        IDLE
    }
    State step = State.PURPLE;

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, true);
        drive = robot.drive;
        spiketimer = new ElapsedTime();
    }

    @Override
    public void start() {
        switch(tagNum){
            case(1):
                spike_x = FieldConstants.RedLeft2.SPIKE_LEFT.x;
                spike_y = FieldConstants.RedLeft2.SPIKE_LEFT.y;
                spike_heading = FieldConstants.RedLeft2.SPIKE_LEFT.h;
                backdrop_x = FieldConstants.RedLeft2.BACKDROP_LEFT.x;
                backdrop_y = FieldConstants.RedLeft2.BACKDROP_LEFT.y;

                backup_x = FieldConstants.RedLeft2.BACKUP_LEFT.x;
                backup_y = FieldConstants.RedLeft2.BACKUP_LEFT.y;

                spike_tangent = Math.toRadians(180);
                break;
            case(2):
                spike_x = FieldConstants.RedLeft2.SPIKE_CENTER.x;
                spike_y = FieldConstants.RedLeft2.SPIKE_CENTER.y;
                spike_heading = FieldConstants.RedLeft2.SPIKE_CENTER.h;
                backdrop_x = FieldConstants.RedLeft2.BACKDROP_CENTER.x;
                backdrop_y = FieldConstants.RedLeft2.BACKDROP_CENTER.y;

                backup_x = FieldConstants.RedLeft2.BACKUP_CENTER.x;
                backup_y = FieldConstants.RedLeft2.BACKUP_CENTER.y;
                backup_heading = Math.toRadians(-15);
                spike_tangent = Math.toRadians(0);
                break;
            case(3):
                spike_x = FieldConstants.RedLeft2.SPIKE_RIGHT.x;
                spike_y = FieldConstants.RedLeft2.SPIKE_RIGHT.y;
                spike_heading = FieldConstants.RedLeft2.SPIKE_RIGHT.h;
                backdrop_x = FieldConstants.RedLeft2.BACKDROP_RIGHT.x;
                backdrop_y = FieldConstants.RedLeft2.BACKDROP_RIGHT.y;

                backup_x = FieldConstants.RedLeft2.BACKUP_RIGHT.x;
                backup_y = FieldConstants.RedLeft2.BACKUP_RIGHT.y;
                backup_heading = Math.toRadians(180);
                spike_tangent = Math.toRadians(90);
                break;


        }

        drive.setPoseEstimate(start);

        origin_test = drive.trajectoryBuilder(start)
                .lineTo(new Vector2d(-36, 0))
                .build();

        purple = drive.trajectorySequenceBuilder(start)
                .lineTo(new Vector2d(-46, -30))
                .lineTo(new Vector2d(-36, -32))
                .lineTo(new Vector2d(-36, -39))
                .turn(Math.toRadians(90))
                .build();

        backup = drive.trajectoryBuilder(purple.end())
                .splineToConstantHeading(new Vector2d(-52, -38), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-56, -38), Math.toRadians(180), SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))

                .build();

        align_to_stack = drive.trajectoryBuilder(backup.end(), true)
                .splineToSplineHeading(new Pose2d(-30, -12, Math.toRadians(210)), Math.toRadians(0))
                .build();

        to_stack = drive.trajectoryBuilder(align_to_stack.end())
                .splineToSplineHeading(new Pose2d(FieldConstants.RedLeft2.ALIGN_TO_STACK.x, FieldConstants.RedLeft2.ALIGN_TO_STACK.y, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(FieldConstants.RedLeft2.STACK.x, FieldConstants.RedLeft2.STACK.y), FieldConstants.RedLeft2.STACK.h, SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        knockover = drive.trajectoryBuilder(to_stack.end())
                .strafeLeft(12)
                .build();
        backfromstack = drive.trajectoryBuilder(knockover.end(), true)
                .splineToConstantHeading(new Vector2d(FieldConstants.RedLeft2.ALIGN_TO_STACK.x, FieldConstants.RedLeft2.ALIGN_TO_STACK.y), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(-54, -24), Math.toRadians(180))
                .build();

        move_to_transfer = drive.trajectoryBuilder(backup.end())
                .lineTo(new Vector2d(-48, -38))
                .build();

        yellow = drive.trajectoryBuilder(move_to_transfer.end(), true)
                .splineToConstantHeading(new Vector2d(-36, -57), Math.toRadians(0))
                .build();
        to_backdrop = drive.trajectoryBuilder(yellow.end(), true)
                .splineToConstantHeading(new Vector2d(backdrop_x, backdrop_y), Math.toRadians(0))
                .build();

        park = drive.trajectoryBuilder(to_backdrop.end())
                .splineToConstantHeading(new Vector2d(FieldConstants.RedLeft2.PARK.x, FieldConstants.RedLeft2.PARK.y), Math.toRadians(0))
                .build();
        drive.followTrajectorySequenceAsync(purple);
//        drive.followTrajectoryAsync(origin_test);
//        step = State.TEST;
    }
    @Override
    public void loop() {
        switch (step){
            case TEST:
                if(!drive.isBusy()){
                    step = State.IDLE;
                }
                break;
            case PURPLE:
                robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);

                if(!drive.isBusy()){
                    robot.intake.mandibleOpen();
                    step = State.SPIKE_DROP;
                    spiketimer.reset();
                }
                break;
            case SPIKE_DROP:
                robot.intake.dropBoth();
                if(!drive.isBusy()){
                    step = State.TO_STACK;
                    drive.followTrajectoryAsync(backup);

                }
                break;
            case BACKUP:
                if(!drive.isBusy()){
                    step = State.ALIGN_TO_STACK;
                    drive.followTrajectoryAsync(align_to_stack);
                }
                break;
            case ALIGN_TO_STACK:
                if(!drive.isBusy()){
                    step = State.TO_STACK;
                    drive.followTrajectoryAsync(to_stack);
                }
                break;
            case TO_STACK:
                if(!drive.isBusy()){
                    spiketimer.reset();
                    robot.intake.leftMandibleClose();
                    step = State.GRAB_FROM_STACK;
                }
                break;
            case KNOCK_OVER:
                if(spiketimer.seconds() >= 0.4){
                    drive.followTrajectoryAsync(knockover);
                    step = State.BACK_TO_STACK;
                }
                break;
            case BACK_TO_STACK:
                if(!drive.isBusy()){
                    drive.followTrajectoryAsync(backfromstack);
                    step = State.GRAB_FROM_STACK;
                }
                break;
            case GRAB_FROM_STACK:
                if(spiketimer.seconds() >= 0.3){
                    robot.intake.rightMandibleClose();
                    step = State.MOVE_TO_TRANSFER;
                    drive.followTrajectoryAsync(move_to_transfer);
                }
                break;
            case MOVE_TO_TRANSFER:
                if(!drive.isBusy()){
                    robot.intake.goTo(Intake.Positions.DOWN_TO_PIXEL, false);
                    step = State.TRANSFER_INTAKE;
                    spiketimer.reset();
                }
                break;
            case TRANSFER_INTAKE:
                if(spiketimer.seconds() >= 0.6){
                    robot.intake.holdPixelRight();
                    spiketimer.reset();
                    step = State.INTAKE_RELEASE;
                }
                break;
            case INTAKE_RELEASE:
                if(spiketimer.seconds() >= 1){
                    robot.intake.goTo(Intake.Positions.TRANSFER, false);
                    robot.delivery.dropLeft();
                    spiketimer.reset();
                    step = State.TRANSFER_DELIVERY;
                }
                break;
            case TRANSFER_DELIVERY:
                if(spiketimer.seconds() >= 1){
                    robot.intake.dropBoth();
                    spiketimer.reset();
                    step = State.DELIVERY_GRIP;
                }
                break;
            case DELIVERY_GRIP:
                if (spiketimer.seconds() >= 0.5){
                    robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);
                    robot.delivery.holdPixelsBoth();
                    drive.followTrajectoryAsync(yellow);
                    spiketimer.reset();
                    step = State.IDLE;

                }
                break;
            case TO_BACKDROP:
                if(spiketimer.seconds() >= 5){
                    //robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                }
                if(!drive.isBusy()){
                    robot.intake.mandibleClose();
                    robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                    step = State.DELIVERY_LIFT;
                    spiketimer.reset();
                    drive.followTrajectoryAsync(to_backdrop);

                }
                break;
            case DELIVERY_LIFT:
                if(!drive.isBusy()){
                    robot.delivery.dropBoth();
                    spiketimer.reset();
                    step = State.DROP_ON_BACKDROP;
                }
                break;
            case DROP_ON_BACKDROP:
                if(spiketimer.seconds() >= 1){
                    step = State.PARK;
                    spiketimer.reset();
                    //drive.followTrajectoryAsync(park);
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
        robot.update();
    }
}
