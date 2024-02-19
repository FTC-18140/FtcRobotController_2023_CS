package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
public class FieldConstants {
    public enum RedRight {
        START(12,-63, Math.toRadians(90)),
        SPIKE_LEFT(7,-36,Math.toRadians(130)),
        SPIKE_CENTER(23,-32.5,Math.toRadians(120)),
        SPIKE_RIGHT(26,-40,Math.toRadians(120)),
        BACKDROP_LEFT(48,-33.5,90),
        BACKDROP_CENTER(48,-40.5,90),
        BACKDROP_RIGHT(48,-46.5,Math.toRadians(179.9)),
        TRUSS_IN(-36,-12,Math.toRadians(90)),
        TRUSS_OUT(-36,36,Math.toRadians(90)),
        STACK(-24,52,Math.toRadians(90)),
        PARK(54,-62,Math.toRadians(0));

        public final double x;
        public final double y;
        public final double h;
        RedRight(double x_val, double y_val, double head){
            x=x_val;
            y=y_val;
            h=head;
        }
    }

    public enum RedLeft {
        START(-36,-63, Math.toRadians(90)),
        SPIKE_LEFT(-38,-24, Math.toRadians(90)),
        SPIKE_CENTER(23,-32.5, Math.toRadians(120)),
        SPIKE_RIGHT(26,-40, Math.toRadians(120)),
        BACKDROP_LEFT(48,-33.5,90),
        BACKDROP_CENTER(48,-40.5,90),
        BACKDROP_RIGHT(48,-46.5,Math.toRadians(179.9)),
        TRUSS_IN(-36,-12,Math.toRadians(90)),
        TRUSS_OUT(-36,36,Math.toRadians(90)),
        STACK(-24,52,Math.toRadians(90)),
        PARK(54,-62,Math.toRadians(0));

        public final double x;
        public final double y;
        public final double h;
        RedLeft(double x_val, double y_val, double head){
            x=x_val;
            y=y_val;
            h=head;
        }
    }

}
