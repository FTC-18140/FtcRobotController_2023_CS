package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting
{


    static int bot_w = 8;
    int tagNum = 2;
    static final double START_X = 12;
    static final double START_Y = 62;

    final int END_X = -58;
    final int END_Y = -58;

    final int SPIKE_L_X = 12;
    final int SPIKE_M_X = -25;
    static final int SPIKE_R_X = 26;

    final int SPIKE_L_Y = 0;
    final int SPIKE_M_Y = -12;
    static final int SPIKE_R_Y = -36;

    static int spike_x;
    static int spike_y;

    final int BACKDROP_L_X = -30;
    final int BACKDROP_M_X = -36;
    final int BACKDROP_R_X = -42;

    final int BACKDROP_L_Y = -48;
    final int BACKDROP_M_Y = -48;
    final int BACKDROP_R_Y = -48;

    int backdrop_x;
    int backdrop_y;

    final int TRUSS_IN_X = -36;
    final int TRUSS_IN_Y = -2;
    final int TRUSS_OUT_X = -36;
    final int TRUSS_OUT_Y = 26;

    final int STACK_X = -36;
    final int STACK_Y = 60;

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        Pose2d start = new Pose2d(START_X ,START_Y, Math.toRadians(-90));
        spike_x = SPIKE_R_X;
        spike_y = SPIKE_R_Y;

        RoadRunnerBotEntity myBotRight = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                                                  drive.trajectorySequenceBuilder(start)
                                                       .lineTo(new Vector2d(-38, -38))
                                                          .splineToConstantHeading( new Vector2d(-38, -18), Math.toRadians(0))
                                                          .splineToConstantHeading( new Vector2d( 54, -60), 0)
                                                       .build()
                                         );

        RoadRunnerBotEntity myBotCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                                                  drive.trajectorySequenceBuilder(start)
                                                          .lineTo(new Vector2d(-38, -38))
                                                          .splineToConstantHeading(new Vector2d(-38, -20), Math.toRadians(90))
                                                          .turn(150)
                                                       .build()
                                         );

        RoadRunnerBotEntity myBotLeft = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                                                  drive.trajectorySequenceBuilder(start)
                                                          .splineToLinearHeading(new Pose2d(22, 30, Math.toRadians(-120)), Math.toRadians(-90))
                                                          .setReversed(true)
                                                          .splineToLinearHeading(new Pose2d(52.5, 37.5, Math.toRadians(180)), Math.toRadians(0))
                                                          .setReversed(false)
                                                          .splineToConstantHeading(new Vector2d(24, 63), Math.toRadians(180))
                                                          .splineToConstantHeading(new Vector2d(-40, 63), Math.toRadians(180))
                                                          .splineToConstantHeading(new Vector2d(-52, 40), Math.toRadians(-100))
                                                          .splineToConstantHeading(new Vector2d(-57, 41.5), Math.toRadians(150))
                                                          .setReversed(true)
                                                          .splineToConstantHeading(new Vector2d(-52, 52), Math.toRadians(45))
                                                          .setReversed(true)
                                                          .splineToConstantHeading(new Vector2d(-40, 63), Math.toRadians(0))
                                                          .splineToConstantHeading(new Vector2d(24, 63), Math.toRadians(0))
                                                          .splineToConstantHeading(new Vector2d(50, 38), Math.toRadians(0))
                                                          .setReversed(false)
                                                          .splineToConstantHeading(new Vector2d(50, 62), Math.toRadians(0))
                                                          //.splineToConstantHeading(new Vector2d(-32, 12), Math.toRadians(-90))
                                                          //.splineToConstantHeading(new Vector2d(-30, 8), Math.toRadians(0))
                                                          //.splineToSplineHeading(new Pose2d(-48, 12, Math.toRadians(180)), Math.toRadians(180))
                                                          //.splineToConstantHeading(new Vector2d(-58, 12), Math.toRadians(180))
                                                       .build()

                                         );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                //.addEntity(myBotLeft)
                .start();
    }
}