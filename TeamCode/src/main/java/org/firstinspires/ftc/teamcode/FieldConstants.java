package org.firstinspires.ftc.teamcode;

public class FieldConstants {
    public enum RedRight {
        START(-63,-12, 0),
        SPIKE_LEFT(-23,-3,0),
        SPIKE_CENTER(-17,-12,0),
        SPIKE_RIGHT(-23,-21,0),
        BACKDROP_LEFT(-30,-48,90),
        BACKDROP_CENTER(-36,-48,90),
        BACKDROP_RIGHT(-42,-48,Math.toRadians(90)),
        TRUSS_IN(-36,-12,Math.toRadians(90)),
        TRUSS_OUT(-36,36,Math.toRadians(90)),
        STACK(-24,52,Math.toRadians(90)),
        PARK(-58,-58,Math.toRadians(90));

        public final int x;
        public final int y;
        public final double h;
        RedRight(int x_val, int y_val, double head){
            x=x_val;
            y=y_val;
            h=head;
        }
    }

    public enum RedLeft {
        START(-63,36, 0),
        SPIKE_LEFT(-23,48,0),
        SPIKE_CENTER(-17,36,0),
        SPIKE_RIGHT(-23,24,0),
        BACKDROP_LEFT(-30,-48,90),
        BACKDROP_CENTER(-36,-48,90),
        BACKDROP_RIGHT(-42,-48,Math.toRadians(90)),
        TRUSS_IN(-36,-12,Math.toRadians(90)),
        TRUSS_OUT(-36,36,Math.toRadians(90)),
        STACK(-24,52,Math.toRadians(90)),
        PARK(-58,-58,Math.toRadians(90));

        public final int x;
        public final int y;
        public final double h;
        RedLeft(int x_val, int y_val, double head){
            x=x_val;
            y=y_val;
            h=head;
        }
    }

}
