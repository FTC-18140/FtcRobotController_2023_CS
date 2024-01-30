package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
public class RRAutoRedR extends OpMode {

    SampleMecanumDrive drive;
    final int START_X = -58;
    final int START_Y = -12;

    final int END_X = -58;
    final int END_Y = -58;

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
    Trajectory step1;
    //Thunderbot2023 robot = new Thunderbot2023();

    @Override
    public void init(){
        drive = new SampleMecanumDrive(hardwareMap);
        //robot.init(hardwareMap, telemetry, true);
        //0.9083333
    }
    @Override
    public void start(){

        step1 = drive.trajectoryBuilder(new Pose2d(START_X, START_Y, Math.toRadians(0)))
                .strafeTo(new Vector2d(END_X, END_Y))
                .build();
        drive.followTrajectoryAsync(step1);
    }
    @Override
    public void loop(){
        drive.update();
        //robot.update();

    }
}
