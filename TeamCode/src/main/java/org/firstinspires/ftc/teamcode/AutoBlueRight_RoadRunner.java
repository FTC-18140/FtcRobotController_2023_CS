package org.firstinspires.ftc.teamcode;

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
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(group = "blueright")
public class AutoBlueRight_RoadRunner extends OpMode {

    ThunderbotAuto2023 robot = new ThunderbotAuto2023();
    SampleMecanumDrive drive;

    double spike_x;
    double spike_y;
    double spike_heading;
    double spike_tangent;

    double backup_x;
    double backup_y;
    double backup_heading = Math.toRadians(-90);

    double backdrop_x;
    double backdrop_y;

    ElapsedTime spiketimer;
    int tagNum = 2;
    String tag = "CENTER";

    TrajectorySequence purple;
    Trajectory align_to_stack;
    Trajectory to_stack;
    Trajectory move_to_transfer;
    Trajectory yellow;
    Trajectory to_backdrop;
    Trajectory park;
    Trajectory through_door;
    Trajectory to_backdrop_left;

    Trajectory backup;

    enum State{
        PURPLE,
        SPIKE_DROP,
        BACKUP,
        ALIGN_TO_STACK,
        THROUGH_DOOR_LEFT,
        TO_BACKDROP_LEFT,
        TO_STACK,
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
        TGEVisionProcessor.theColor = "BLUE";
    }

    @Override
    public void init_loop() {
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
    public void start() {
        switch(tagNum){
            case(1):
                spike_x = FieldConstants.BlueRight.SPIKE_LEFT.x;
                spike_y = FieldConstants.BlueRight.SPIKE_LEFT.y;
                spike_heading = FieldConstants.BlueRight.SPIKE_LEFT.h;
                backdrop_x = FieldConstants.BlueRight.BACKDROP_LEFT.x;
                backdrop_y = FieldConstants.BlueRight.BACKDROP_LEFT.y;

                backup_x = FieldConstants.BlueRight.BACKUP_LEFT.x;
                backup_y = FieldConstants.BlueRight.BACKUP_LEFT.y;
                backup_heading = Math.toRadians(180);
                spike_tangent = Math.toRadians(180);
                break;
            case(2):
                spike_x = FieldConstants.BlueRight.SPIKE_CENTER.x;
                spike_y = FieldConstants.BlueRight.SPIKE_CENTER.y;
                spike_heading = FieldConstants.BlueRight.SPIKE_CENTER.h;
                backdrop_x = FieldConstants.BlueRight.BACKDROP_CENTER.x;
                backdrop_y = FieldConstants.BlueRight.BACKDROP_CENTER.y;

                backup_x = FieldConstants.BlueRight.BACKUP_CENTER.x;
                backup_y = FieldConstants.BlueRight.BACKUP_CENTER.y;
                backup_heading = Math.toRadians(-90);
                spike_tangent = Math.toRadians(-30);
                break;
            case(3):
                spike_x = FieldConstants.BlueRight.SPIKE_RIGHT.x;
                spike_y = FieldConstants.BlueRight.SPIKE_RIGHT.y;
                spike_heading = FieldConstants.BlueRight.SPIKE_RIGHT.h;
                backdrop_x = FieldConstants.BlueRight.BACKDROP_RIGHT.x;
                backdrop_y = FieldConstants.BlueRight.BACKDROP_RIGHT.y;

                backup_x = FieldConstants.BlueRight.BACKUP_RIGHT.x;
                backup_y = FieldConstants.BlueRight.BACKUP_RIGHT.y;
                backup_heading = Math.toRadians(-90);
                spike_tangent = Math.toRadians(90);
                break;
        }
        Pose2d start = new Pose2d(FieldConstants.BlueRight.START.x ,FieldConstants.BlueRight.START.y, FieldConstants.BlueRight.START.h);

        drive.setPoseEstimate(start);
        purple = drive.trajectorySequenceBuilder(start)
                .lineTo(new Vector2d(FieldConstants.BlueRight.SPIKE_ALIGN.x, FieldConstants.BlueRight.SPIKE_ALIGN.y))
                .splineToConstantHeading(new Vector2d(spike_x, spike_y), spike_tangent)
                .turn(spike_heading)
                .build();

        backup = drive.trajectoryBuilder(purple.end(), true)
                .splineToConstantHeading(new Vector2d(backup_x, backup_y), backup_heading)
                .build();

        align_to_stack = drive.trajectoryBuilder(backup.end(), true)
                .splineToSplineHeading(new Pose2d(-30, 8, Math.toRadians(180)), Math.toRadians(0))
                .build();

        to_stack = drive.trajectoryBuilder(align_to_stack.end())
                .splineToSplineHeading(new Pose2d(FieldConstants.BlueRight.ALIGN_TO_STACK.x, FieldConstants.BlueRight.ALIGN_TO_STACK.y, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(FieldConstants.BlueRight.STACK.x, FieldConstants.BlueRight.STACK.y), FieldConstants.BlueRight.STACK.h, SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        move_to_transfer = drive.trajectoryBuilder(to_stack.end())
                .lineTo(new Vector2d(-52, 12))
                .build();

        yellow = drive.trajectoryBuilder(move_to_transfer.end(), true)
                .splineToConstantHeading(new Vector2d(FieldConstants.BlueRight.DOOR.x, FieldConstants.BlueRight.DOOR.y), Math.toRadians(0))
                .build();

        to_backdrop = drive.trajectoryBuilder(yellow.end(), true)
                .splineToConstantHeading(new Vector2d(backdrop_x, backdrop_y), Math.toRadians(0))
                .build();

        park = drive.trajectoryBuilder(to_backdrop.end())
                .splineToConstantHeading(new Vector2d(FieldConstants.BlueRight.PARK.x, FieldConstants.BlueRight.PARK.y), Math.toRadians(0))
                .build();
        drive.followTrajectorySequenceAsync(purple);
    }
    @Override
    public void loop() {

        switch (step){
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
                if(spiketimer.seconds() >= 0.3){
                    step = State.BACKUP;
                    drive.followTrajectoryAsync(backup);

                }
                break;
            case BACKUP:
                if(!drive.isBusy()){
                    drive.followTrajectoryAsync(align_to_stack);
                    step = State.ALIGN_TO_STACK;
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
                    robot.intake.rightMandibleClose();
                    step = State.GRAB_FROM_STACK;
                }
                break;
            case GRAB_FROM_STACK:
                if(spiketimer.seconds() >= 0.4){
                    robot.intake.leftMandibleClose();
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
                if(spiketimer.seconds() >= 0.5){
                    robot.intake.holdPixelLeft();
                    spiketimer.reset();
                    step = State.INTAKE_RELEASE;
                }
                break;
            case INTAKE_RELEASE:
                if(spiketimer.seconds() >= 0.5){
                    robot.intake.goTo(Intake.Positions.TRANSFER, false);
                    robot.delivery.dropRight();
                    spiketimer.reset();
                    step = State.TRANSFER_DELIVERY;
                }
                break;
            case TRANSFER_DELIVERY:
                if(spiketimer.seconds() >= 2){
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
                    step = State.TO_BACKDROP;

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
        telemetry.addData("step: ", step);

    }

}
