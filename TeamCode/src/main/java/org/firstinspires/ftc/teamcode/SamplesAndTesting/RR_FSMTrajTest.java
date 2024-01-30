package org.firstinspires.ftc.teamcode.SamplesAndTesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.ThunderbotAuto2023;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class RR_FSMTrajTest extends OpMode
{
    ThunderbotAuto2023 robot;
    SampleMecanumDrive drive;

    // This enum defines our "state"
    // This is essentially just defines the possible steps our program will take
    enum State {
        TRAJECTORY_1,   // First, follow a splineTo() trajectory
        SPIKE_DROP,
        TRAJECTORY_2,   // Then, follow a lineTo() trajectory
        PIXEL_DROP,         // Then we want to do a point turn
        TRAJECTORY_3,   // Then, we follow another lineTo() trajectory
        IDLE            // Our bot will enter the IDLE state when done
    }

    // We define the current state we're on
    // Default to IDLE
    RR_FSMTrajTest.State currentState = RR_FSMTrajTest.State.IDLE;

    // Define our start pose
    // This assumes we start at x: 15, y: 10, heading: 0 degrees
    Pose2d startPose = new Pose2d(12, -63, Math.toRadians(0));

    Trajectory trajectory1, trajectory2, trajectory3;
    double pixelDropTime, pixelPlaceTime;
    ElapsedTime spikeTimer;
    ElapsedTime placeTimer;

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
        trajectory1 = drive.trajectoryBuilder(startPose)
                           .splineTo(new Vector2d(24, -43), Math.toRadians(0))
                           .build();

        // Second trajectory
        // Ensure that we call trajectory1.end() as the start for this one
        trajectory2 = drive.trajectoryBuilder(trajectory1.end())
                           .splineTo(new Vector2d(49, -36), Math.toRadians(-90))
                           .build();

        // Third trajectory

        trajectory3 = drive.trajectoryBuilder(trajectory2.end())
                .splineTo(new Vector2d(49, -30), Math.toRadians(-90))
                           .splineTo(new Vector2d(40, -52), Math.toRadians(-90))
                           .lineToConstantHeading( new Vector2d(50, -52))
                           .build();

        pixelDropTime = 0.5;
        spikeTimer = new ElapsedTime();

        pixelPlaceTime = 1.5;
        placeTimer = new ElapsedTime();


    }

    @Override
    public void start()
    {
        super.start();
        robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);
        robot.drive.followTrajectoryAsync(trajectory1);
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
        switch (currentState) {
            case TRAJECTORY_1:
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
                // Check if the drive class is busy turning
                // If not, move onto the next state, TRAJECTORY_3, once finished
                if (spikeTimer.seconds() >= pixelDropTime) {
                    currentState = RR_FSMTrajTest.State.TRAJECTORY_2;
                    robot.intake.goTo(Intake.Positions.INIT, false);
                    robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                    drive.followTrajectoryAsync(trajectory2);
                }
                break;
            case TRAJECTORY_2:
                // Check if the drive class is busy following the trajectory
                // Move on to the next state, TURN_1, once finished
                if (!drive.isBusy()) {
                    currentState = State.PIXEL_DROP;
                    placeTimer.reset();
                    robot.delivery.dropBoth();
                }
                break;
            case PIXEL_DROP:
                // Check if the timer has exceeded the specified wait time
                // If so, move on to the TURN_2 state
                if (placeTimer.seconds() >= pixelPlaceTime) {
                    currentState = State.TRAJECTORY_3;
                    robot.delivery.goTo(Delivery.Positions.TELE_INIT);
                    drive.followTrajectoryAsync(trajectory3);
                }
                break;
            case TRAJECTORY_3:
                // Check if the drive class is busy following the trajectory
                // If not, move onto the next state, WAIT_1
                if (!drive.isBusy()) {
                    currentState = RR_FSMTrajTest.State.IDLE;
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
