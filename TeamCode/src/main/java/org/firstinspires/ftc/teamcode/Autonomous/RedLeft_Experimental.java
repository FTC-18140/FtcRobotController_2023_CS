package org.firstinspires.ftc.teamcode.Autonomous;

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

    boolean stepdone = false;
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

    TrajectorySequence stack_left;
    TrajectorySequence stack_center;
    TrajectorySequence stack_right;

    Trajectory align_to_stack;
    Trajectory to_stack;
    Trajectory move_to_transfer;
    Trajectory yellow;
    Trajectory to_backdrop;
    Trajectory park;

    Trajectory go_to_stack;

    TrajectorySequence back_and_turn;

    TrajectorySequence spike;

    enum State{
        TEST,
        PURPLE,
        SPIKE_DROP,
        BACKUP,
        ALIGN_TO_STACK,
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

    enum Spike_Left {
        TO_SPIKE,
        DROP_PIXEL,
        BACKUP,
        IDLE
    }

    Spike_Left step_left;

    enum Spike_Center {
        TO_SPIKE,
        DROP_PIXEL,
        BACKUP,
        IDLE
    }

    Spike_Center step_center;

    enum Spike_Right {
        TO_SPIKE,
        DROP_PIXEL,
        BACKUP,
        IDLE
    }

    Spike_Right step_right;


    State step = State.SPIKE_DROP;
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
    public void init() {
        robot.init(hardwareMap, telemetry, true);
        drive = robot.drive;
        spiketimer = new ElapsedTime();
    }

    public boolean spike_drop_left() {
        boolean done = false;
        switch(step_left){
            case TO_SPIKE:
                robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);

                if(!drive.isBusy()){
                    robot.intake.mandibleOpen();
                    robot.intake.dropBoth();
                    step_left = Spike_Left.DROP_PIXEL;
                    drive.followTrajectorySequenceAsync(back_and_turn);
                    spiketimer.reset();
                }

                break;
            case DROP_PIXEL:
                if(!drive.isBusy()){
                    step_left = Spike_Left.BACKUP;
                    drive.followTrajectoryAsync(go_to_stack);

                }

                break;
            case BACKUP:
                if(!drive.isBusy()){
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
    }

    public boolean spike_drop_center() {
        boolean done = false;
        switch(step_center){
            case TO_SPIKE:
                robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);

                if(!drive.isBusy()){
                    robot.intake.mandibleOpen();
                    robot.intake.dropBoth();
                    step_center = Spike_Center.DROP_PIXEL;
                    drive.followTrajectorySequenceAsync(back_and_turn);
                    spiketimer.reset();
                }

                break;
            case DROP_PIXEL:
                if(!drive.isBusy()){
                    step_center = Spike_Center.BACKUP;
                    drive.followTrajectoryAsync(go_to_stack);

                }

                break;
            case BACKUP:
                if(!drive.isBusy()){
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
    }

    public boolean spike_drop_right() {
        boolean done = false;
        switch(step_right){
            case TO_SPIKE:
                robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);

                if(!drive.isBusy()){
                    robot.intake.mandibleOpen();
                    robot.intake.dropBoth();
                    step_right = Spike_Right.DROP_PIXEL;
                    drive.followTrajectorySequenceAsync(back_and_turn);
                    spiketimer.reset();
                }

                break;
            case DROP_PIXEL:
                if(!drive.isBusy()){
                    step_right = Spike_Right.BACKUP;
                    drive.followTrajectoryAsync(go_to_stack);

                }

                break;
            case BACKUP:
                if(!drive.isBusy()){
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
    }

    @Override
    public void start() {

        //Add in mini state machines
        //conciceify!!

        switch(tagNum){
            case(1):
                spike = stack_left;
                break;
            case(2):
                spike = stack_center;
                break;
            case(3):
                spike = stack_right;
                break;


        }

        drive.setPoseEstimate(start);

        //test trajectory for tuning the starting position

        origin_test = drive.trajectoryBuilder(start)
                .lineTo(new Vector2d(-36, 0))
                .build();

        //purple:
        //drives around to the spike mark, pushing the game element out of the way.
        purple = drive.trajectorySequenceBuilder(start)
                .splineToConstantHeading(new Vector2d(-56, -34), Math.toRadians(90))
                .lineTo(new Vector2d(-44, -33))
                .build();

        stack_left = drive.trajectorySequenceBuilder(start)
                .splineToConstantHeading(new Vector2d(-56, -34), Math.toRadians(90))
                .lineTo(new Vector2d(-44, -33))
                .build();

        stack_center = drive.trajectorySequenceBuilder(start)
                .splineToConstantHeading(new Vector2d(-56, -34), Math.toRadians(90))
                .lineTo(new Vector2d(-44, -33))
                .build();

        stack_right = drive.trajectorySequenceBuilder(start)
                .splineToConstantHeading(new Vector2d(-56, -34), Math.toRadians(90))
                .lineTo(new Vector2d(-44, -33))
                .build();



        //backs up and aligns to the stack and backdrop
        back_and_turn = drive.trajectorySequenceBuilder(spike.end())
                .lineTo(new Vector2d(-42, -42))
                .turn(Math.toRadians(90))
                .build();

        //splines to the stack, then slows down as it aligns more accurately with the stack
        go_to_stack = drive.trajectoryBuilder(back_and_turn.end())
                .splineToConstantHeading(new Vector2d(-52, -38), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-59, -41), Math.toRadians(180), SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        //backs away to knock extra pixels less
        move_to_transfer = drive.trajectoryBuilder(go_to_stack.end())
                .lineTo(new Vector2d(-48, -38))
                .build();

        //aligns to the truss
        yellow = drive.trajectoryBuilder(move_to_transfer.end(), true)
                .splineToConstantHeading(new Vector2d(-36, -58), Math.toRadians(0))
                .build();
        to_backdrop = drive.trajectoryBuilder(yellow.end(), true)
                .splineToConstantHeading(new Vector2d(backdrop_x, backdrop_y), Math.toRadians(0))
                .build();

        park = drive.trajectoryBuilder(to_backdrop.end())
                .splineToConstantHeading(new Vector2d(FieldConstants.RedLeft2.PARK.x, FieldConstants.RedLeft2.PARK.y), Math.toRadians(0))
                .build();


        drive.followTrajectorySequenceAsync(spike);
        spiketimer.reset();

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
            case SPIKE_DROP:
                if(!stepdone){
                    stepdone = spike_drop_left();
                }else{
                    step = State.IDLE;
                }
                break;
            case TO_STACK:
                if(!drive.isBusy()){
                    spiketimer.reset();
                    robot.intake.leftMandibleClose();
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
