package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.ThunderbotAuto2023;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class RR_FSMTrajTest_KL extends OpMode
{
    ThunderbotAuto2023 robot;
    SampleMecanumDrive drive;

    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    enum State {
        TO_SPIKE,   // First, follow a splineTo() trajectory
        SPIKE_DROP,
        TO_BACKDROP,   // Then, follow a lineTo() trajectory
        PIXEL_DROP,         // Then we want to do a point turn
        PARK,   // Then, we follow another lineTo() trajectory
        IDLE            // Our bot will enter the IDLE state when done
    }

    // We define the current state we're on
    // Default to IDLE
    RR_FSMTrajTest_KL.State currentState = RR_FSMTrajTest_KL.State.IDLE;

    // Define our start pose
    Pose2d startPose = new Pose2d(12, -63, Math.toRadians(0));

    Trajectory driveToSpike, driveToBackdrop, park;
    double spikeDropTime, backdropDropTime;
    ElapsedTime spikeTimer;
    ElapsedTime backdropTimer;

    /**
     * User-defined init method
     * <p>
     * This method will be called once, when the INIT button is pressed.
     */
    @Override
    public void init()
    {
        robot = new ThunderbotAuto2023();
        robot.init(hardwareMap, telemetry, false);

        // Grab a reference to the Mecanum Drive chassis inside the robot object for convenience
        // in handling the RR trajectories.
        drive = robot.drive;

        // Set inital pose
        drive.setPoseEstimate(startPose);

        // Let's define our trajectories
        driveToSpike = drive.trajectoryBuilder(startPose)
                            .splineTo(new Vector2d(24, -43), Math.toRadians(30))
                            .build();

        // Second trajectory
        // Ensure that we call trajectory1.end() as the start for this one
        driveToBackdrop = drive.trajectoryBuilder(driveToSpike.end())
                               .splineTo(new Vector2d(49, -36), Math.toRadians(-90))
                               .build();

        // Third trajectory

        park = drive.trajectoryBuilder(driveToBackdrop.end())
                    .splineTo(new Vector2d(43, -43), Math.toRadians(-90))
                    .splineTo(new Vector2d(49, -52), Math.toRadians(-90))
                    .build();

        spikeDropTime = 0.5;
        spikeTimer = new ElapsedTime();

        backdropDropTime = 1.5;
        backdropTimer = new ElapsedTime();


    }

    @Override
    public void start()
    {
        super.start();
//        robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);
        robot.drive.followTrajectoryAsync(driveToSpike);
    }

    /**
     * User-defined loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the play button is pressed and when the OpMode is stopped.
     */
    @Override
    public void loop()
    {
        robot.update();
        switch (currentState) {
            case TO_SPIKE:
                // Check if the drive class isn't busy
                // `isBusy() == true` while it's following the trajectory
                // Once `isBusy() == false`, the trajectory follower signals that it is finished
                // We move on to the next state
                // Make sure we use the async follow function
                if (!drive.isBusy()) {
                    currentState = State.SPIKE_DROP;
                    spikeTimer.reset();
                }
                break;
            case SPIKE_DROP:
//                robot.intake.goTo(Intake.Positions.INIT, false);
//                robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                if (spikeTimer.seconds() >= spikeDropTime) {
                    currentState = RR_FSMTrajTest_KL.State.TO_BACKDROP;
                    drive.followTrajectoryAsync(driveToBackdrop);
                }
                break;
            case TO_BACKDROP:
                // Check if the drive class is busy following the trajectory
                // Move on to the next state, TURN_1, once finished
                if (!drive.isBusy()) {
                    currentState = State.PIXEL_DROP;
                    backdropTimer.reset();
                }
                break;
            case PIXEL_DROP:
                // Check if the timer has exceeded the specified wait time
                // If so, move on to the TURN_2 state
//                robot.delivery.dropBoth();
                if (backdropTimer.seconds() >= backdropDropTime) {
                    currentState = State.PARK;
//                    robot.delivery.goTo(Delivery.Positions.TELE_INIT);
                    drive.followTrajectoryAsync(park);
                }
                break;
            case PARK:
                // Check if the drive class is busy following the trajectory
                // If not, move onto the next state, WAIT_1
                if (!drive.isBusy()) {
                    currentState = RR_FSMTrajTest_KL.State.IDLE;
                }
                break;
            case IDLE:
                // Do nothing in IDLE
                // currentState does not change once in IDLE
                // This concludes the autonomous program
                break;
        }
    }
}
