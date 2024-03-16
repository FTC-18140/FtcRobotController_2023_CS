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

@Autonomous(group = "autoredleft")
public class AutoRedLeft_StageDoor extends OpMode {

    boolean stepdone = false;
    ThunderbotAuto2023 robot = new ThunderbotAuto2023();
    SampleMecanumDrive drive;

    Pose2d start = new Pose2d(FieldConstants.RedLeft2.START.x,FieldConstants.RedLeft2.START.y, FieldConstants.RedLeft2.START.h);

    ElapsedTime spiketimer;
    String spikePos = "RIGHT";

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

    Trajectory to_backdrop_left;
    Trajectory to_backdrop_center;
    Trajectory to_backdrop_right;

    Trajectory second_pixel;
    TrajectorySequence back_and_turn;
    TrajectorySequence back_and_turn_right;

    TrajectorySequence spikeTrajectory;
    Trajectory backdropTrajectory;

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
        SECOND_PIXEL,
        SECOND_PIXEL_DROP,
        PARK,
        AUTO_INTAKE,
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

    enum Stack_State {
        TO_STACK,
        GRAB_FROM_STACK,
        MOVE_TO_TRANSFER,
        INTAKE,
        INTAKE_TO_TRANSFER,
        TRANSFER,
        GRIP,
        IDLE
    }

    Stack_State stack_step = Stack_State.TO_STACK;
    State step = State.SPIKE_DROP;

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, true);
        drive = robot.drive;
        spiketimer = new ElapsedTime();
    }
    @Override
    public void init_loop(){
        super.init_loop();
        switch (robot.eyes.getSpikePos())
        {
            case "LEFT":
            case "NOT FOUND": // not found hopefully means the prop is out of view to the left
                spikePos = "LEFT";
                telemetry.addData("ZONE = ", spikePos);
                break;
            case "RIGHT":
                spikePos = "RIGHT";
                telemetry.addData("ZONE = RIGHT", spikePos);
                break;
            default:
            case "CENTER": // default CENTER
                spikePos = "CENTER";
                telemetry.addData("ZONE = CENTER", spikePos);
                break;
        }
    }

    @Override
    public void start() {



        // Set start position
        drive.setPoseEstimate(start);

        //test trajectory for tuning the starting position
        origin_test = drive.trajectoryBuilder(start)
                .lineTo(new Vector2d(-36, 0))
                .build();

        //purple:
        //drives around to the spike mark, pushing the game element out of the way.
//        purple = drive.trajectorySequenceBuilder(start)
//                .splineToConstantHeading(new Vector2d(-56, -34), Math.toRadians(90))
//                .lineTo(new Vector2d(-44, -33))
//                .build();

        stack_left = drive.trajectorySequenceBuilder(start)
                .splineToConstantHeading(new Vector2d(-58, -32), Math.toRadians(90))
                .lineTo(new Vector2d(-46, -33))
                .build();

        stack_center = drive.trajectorySequenceBuilder(start)
                .splineToConstantHeading(new Vector2d(-54, -34), Math.toRadians(90))
                .lineTo(new Vector2d(-40, -33))
                .build();

        stack_right = drive.trajectorySequenceBuilder(start)
                .splineToConstantHeading(new Vector2d(-42, -42), Math.toRadians(90))
                .splineTo(new Vector2d(-32, -36), Math.toRadians(0))
                .build();


        // Select starting substate based on vision processing result.
        // Also, initialize the substate step
        switch(spikePos){
            case("LEFT"):
                spikeTrajectory = stack_left;
                backdropTrajectory = to_backdrop_left;
                step_left = Spike_Left.TO_SPIKE;
                break;
            case("CENTER"):
                spikeTrajectory = stack_center;
                backdropTrajectory = to_backdrop_center;
                step_center = Spike_Center.TO_SPIKE;
                break;
            case("RIGHT"):
                spikeTrajectory = stack_right;
                backdropTrajectory = to_backdrop_right;
                step_right = Spike_Right.TO_SPIKE;
                break;
        }
//backs up and aligns to the stack and backdrop
        back_and_turn = drive.trajectorySequenceBuilder(spikeTrajectory.end())
                .lineTo(new Vector2d(-44, -42))
                .turn(Math.toRadians(90))
                .build();

        back_and_turn_right = drive.trajectorySequenceBuilder(spikeTrajectory.end())
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(-44, -42), Math.toRadians(-90))
                .turn(Math.toRadians(180))
                .build();
//splines to the stack, then slows down as it aligns more accurately with the stack
        if (spikePos == "RIGHT"){
            go_to_stack = drive.trajectoryBuilder(back_and_turn_right.end())
                    .splineToConstantHeading(new Vector2d(-52, -38), Math.toRadians(180))
                    .splineToConstantHeading(new Vector2d(-60.5, -41.5), Math.toRadians(150), SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
        }else{
            go_to_stack = drive.trajectoryBuilder(back_and_turn.end())
                    .splineToConstantHeading(new Vector2d(-52, -38), Math.toRadians(180))
                    .splineToConstantHeading(new Vector2d(-60.5, -41.5), Math.toRadians(150), SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();
        }


        //backs away to knock extra pixels less
        move_to_transfer = drive.trajectoryBuilder(go_to_stack.end())
                .lineTo(new Vector2d(-50, -38))
                .build();

        //aligns to the truss
        yellow = drive.trajectoryBuilder(move_to_transfer.end())
                .splineToConstantHeading(new Vector2d(-55, -36), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-56, -20), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-42, -13), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(16, -13), Math.toRadians(0))
                .build();

        to_backdrop = drive.trajectoryBuilder(yellow.end(), true)
                .splineToConstantHeading(new Vector2d(backdrop_x, backdrop_y), Math.toRadians(0))
                .build();
        to_backdrop_left = drive.trajectoryBuilder(yellow.end(), true)
                .splineToConstantHeading(new Vector2d(48, -31), Math.toRadians(0))
                .build();
        to_backdrop_right = drive.trajectoryBuilder(yellow.end(), true)
                .splineToConstantHeading(new Vector2d(47.5, -44), Math.toRadians(0))
                .build();
        to_backdrop_center = drive.trajectoryBuilder(yellow.end(), true)
                .splineToConstantHeading(new Vector2d(47.5, -36), Math.toRadians(0))
                .build();
        switch(spikePos){
            case("LEFT"):
                backdropTrajectory = to_backdrop_left;
                step_left = Spike_Left.TO_SPIKE;
                break;
            case("CENTER"):
                backdropTrajectory = to_backdrop_center;
                step_center = Spike_Center.TO_SPIKE;
                break;
            case("RIGHT"):
                backdropTrajectory = to_backdrop_right;
                step_right = Spike_Right.TO_SPIKE;

                break;
        }
        if (spikePos == "LEFT"){
            second_pixel = drive.trajectoryBuilder(backdropTrajectory.end())
                    .splineToConstantHeading(new Vector2d(48, -44), Math.toRadians(0))
                    .build();

            park = drive.trajectoryBuilder(second_pixel.end())
                    .splineToConstantHeading(new Vector2d(-48, -20), Math.toRadians(0))
                    .build();
        }else{
            second_pixel = drive.trajectoryBuilder(backdropTrajectory.end())
                    .splineToConstantHeading(new Vector2d(47.5, -31), Math.toRadians(0))
                    .build();

            park = drive.trajectoryBuilder(second_pixel.end())
                    .splineToConstantHeading(new Vector2d(-48, -20), Math.toRadians(0))
                    .build();
        }


        drive.followTrajectorySequenceAsync(spikeTrajectory);
        spiketimer.reset();

//        drive.followTrajectoryAsync(origin_test);
//        step = State.TEST;
    }

    // Sub state machine for dropping the pixel on the left spike mark
    public boolean spike_drop_left() {
        boolean done = false;
        switch(step_left){
            case TO_SPIKE:
                robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);

                if(!drive.isBusy()){
                    robot.intake.mandibleOpen();
                    robot.intake.dropBoth();
                    step_left = Spike_Left.DROP_PIXEL;
                    spiketimer.reset();
                }

                break;
            case DROP_PIXEL:
                if ( spiketimer.seconds() > 0.5) {
                    step_left = Spike_Left.BACKUP;
                    drive.followTrajectorySequenceAsync(back_and_turn);
                }

                break;
            case BACKUP:
                if(!drive.isBusy()){
                    step_left = Spike_Left.IDLE;
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
    }

    // Sub state machine for dropping the pixel on the center spike mark
    public boolean spike_drop_center() {
        boolean done = false;
        switch(step_center){
            case TO_SPIKE:
                robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);

                if(!drive.isBusy()){
                    robot.intake.mandibleOpen();
                    robot.intake.dropBoth();
                    step_center = Spike_Center.DROP_PIXEL;
                    spiketimer.reset();
                }
                break;
            case DROP_PIXEL:
                if ( spiketimer.seconds() > 0.5) {
                    drive.followTrajectorySequenceAsync(back_and_turn);
                    step_center = Spike_Center.BACKUP;
                }
                break;
            case BACKUP:
                if(!drive.isBusy()){
                    step_center = Spike_Center.IDLE;
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
    }

    // Sub state machine for dropping the pixel on the right spike mark
    public boolean spike_drop_right() {
        boolean done = false;
        switch(step_right){
            case TO_SPIKE:
                robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);

                if(!drive.isBusy()){
                    robot.intake.mandibleOpen();
                    robot.intake.dropBoth();
                    step_right = Spike_Right.DROP_PIXEL;
                    spiketimer.reset();
                }
                break;
            case DROP_PIXEL:
                if ( spiketimer.seconds() > 0.5) {
                    drive.followTrajectorySequenceAsync(back_and_turn_right);
                    step_right = Spike_Right.BACKUP;
                    done = true;
                }
                break;
            case BACKUP:
                if(!drive.isBusy()){
                    step_right = Spike_Right.IDLE;
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
    }

    public boolean stack_and_transfer() {
        boolean done = false;
        switch(stack_step){
            case TO_STACK:
                if(!drive.isBusy()){
                    robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);
                    robot.intake.dropBoth();
                    robot.intake.leftMandibleClose();
                    drive.followTrajectoryAsync(go_to_stack);
                    stack_step = Stack_State.GRAB_FROM_STACK;
                    spiketimer.reset();
                }
                break;
            case GRAB_FROM_STACK:
                if (!drive.isBusy()) {

                    robot.intake.rightMandibleClose();
                    robot.delivery.dropLeft();
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
                if(spiketimer.seconds() >= 0.5){
                    robot.delivery.holdPixelLeft();
                    done = true;
                }
                break;
            default:
                break;
        }
        return done;
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
                switch( spikePos) {
                    case "LEFT":
                        stepdone = spike_drop_left();
                        break;
                    case "CENTER":
                        stepdone = spike_drop_center();
                        break;
                    case "RIGHT":
                        stepdone = spike_drop_right();
                        break;
                }

                if ( stepdone ) {
//                    step = State.TO_STACK;
//                    robot.drive.followTrajectoryAsync(go_to_stack);
                    step = State.TO_STACK;
                    stepdone = false;
                }
                break;
            case TO_STACK:
                stepdone = stack_and_transfer();
                if(stepdone){
                    step = State.DELIVERY_GRIP;
                }
                break;
//            case GRAB_FROM_STACK:
//                if(spiketimer.seconds() >= 0.3){
//                    robot.intake.rightMandibleClose();
//                    step = State.MOVE_TO_TRANSFER;
//                    drive.followTrajectoryAsync(move_to_transfer);
//                }
//                break;
//            case MOVE_TO_TRANSFER:
//                if(!drive.isBusy()){
//                    robot.intake.goTo(Intake.Positions.DOWN_TO_PIXEL, false);
//                    step = State.TRANSFER_INTAKE;
//                    spiketimer.reset();
//                }
//                break;
            case MOVE_TO_TRANSFER:
                if(!drive.isBusy()){
                    step = State.AUTO_INTAKE;
                    robot.intake.autoIntake(true);
                    robot.intake.dropBoth();
                    spiketimer.reset();
                }
                break;
            case AUTO_INTAKE:
                stepdone = robot.intake.autoIntake(false);

                if ( stepdone )  {
                    robot.delivery.holdPixelsBoth();
                    step = State.DELIVERY_GRIP;
                    stepdone = false;
//                    step = State.TO_BACKDROP;
//                    drive.followTrajectoryAsync(yellow);
                }
            break;
//            case TRANSFER_INTAKE:
//                if(spiketimer.seconds() >= 0.6){
//                    robot.intake.holdPixelRight();
//                    spiketimer.reset();
//                    step = State.INTAKE_RELEASE;
//                }
//                break;
//            case INTAKE_RELEASE:
//                if(spiketimer.seconds() >= 1){
//                    robot.intake.goTo(Intake.Positions.TRANSFER, false);
//                    robot.delivery.dropLeft();
//                    spiketimer.reset();
//                    step = State.TRANSFER_DELIVERY;
//                }
//                break;
//            case TRANSFER_DELIVERY:
//                if(spiketimer.seconds() >= 1){
//                    robot.intake.dropBoth();
//                    spiketimer.reset();
//                    step = State.DELIVERY_GRIP;
//                }
//                break;
            case DELIVERY_GRIP:
                if (!drive.isBusy()){
                    drive.followTrajectoryAsync(yellow);
                    spiketimer.reset();
                    step = State.TO_BACKDROP;

                }
                break;
            case TO_BACKDROP:
                if(!drive.isBusy()){
                    robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                    step = State.DELIVERY_LIFT;
                    spiketimer.reset();
                    drive.followTrajectoryAsync(backdropTrajectory);

                }
                break;
            case DELIVERY_LIFT:
                if(!drive.isBusy()){

                    spiketimer.reset();
                    step = State.SECOND_PIXEL;
                    robot.delivery.dropRight();
                }
                break;
            case SECOND_PIXEL:
                if(spiketimer.seconds() >= 1){
                    drive.followTrajectoryAsync(second_pixel);
                    step = State.SECOND_PIXEL_DROP;
                }
                break;
            case SECOND_PIXEL_DROP:
                if(!drive.isBusy()){
                    robot.delivery.dropBoth();
                    step = State.DROP_ON_BACKDROP;
                    spiketimer.reset();
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

                }
                if(!drive.isBusy()){
                    robot.delivery.goTo(Delivery.Positions.TELE_INIT);
                    step = State.IDLE;
                }
                break;
            default:
                break;
        }
        robot.update();
    }
}
