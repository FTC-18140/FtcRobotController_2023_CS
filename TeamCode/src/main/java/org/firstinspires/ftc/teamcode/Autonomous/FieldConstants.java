package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;

@Config
public class FieldConstants {
    public enum RedRight {
        START(12,-62, Math.toRadians(90)),
        SPIKE_LEFT(8,-37,Math.toRadians(140)),
        SPIKE_CENTER(23,-31.5,Math.toRadians(120)),
        SPIKE_RIGHT(25,-39,Math.toRadians(100)),
        BACKDROP_LEFT(48.5,-32.5,90),
        BACKDROP_CENTER(49,-38,90),
        BACKDROP_RIGHT(50,-45,Math.toRadians(179.9)),
        TRUSS_IN(-36,-12,Math.toRadians(90)),
        TRUSS_OUT(-36,36,Math.toRadians(90)),
        STACK(-24,52,Math.toRadians(90)),
        PARK(54,-61,Math.toRadians(0)),
        PARK_LEFT(54,-10,Math.toRadians(0));

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
        START(-36,-62, Math.toRadians(90)),
        SPIKE_ALIGN(-45,-38, Math.toRadians(90)),
        SPIKE_LEFT(-45,-22, Math.toRadians(150)),
        SPIKE_CENTER(-38,-17, Math.toRadians(150)),
        SPIKE_RIGHT(-34,-36.5, Math.toRadians(-100)),
        BACKUP_RIGHT(-38,-36, Math.toRadians(-100)),
        BACKUP_CENTER(-38,-14, Math.toRadians(-100)),
        BACKUP_LEFT(-42,-18, Math.toRadians(-100)),

        BACKDROP_LEFT(48,-30,90),
        BACKDROP_CENTER(48,-42,90),
        BACKDROP_RIGHT(48,-46.5,Math.toRadians(179.9)),
        DOOR(32,-14,Math.toRadians(90)),
        ALIGN_TO_STACK(-48,-14,Math.toRadians(90)),
        STACK(-58,-14,Math.toRadians(180)),
        PARK(54,-64,Math.toRadians(0));

        public final double x;
        public final double y;
        public final double h;
        RedLeft(double x_val, double y_val, double head){
            x=x_val;
            y=y_val;
            h=head;
        }
    }
    public enum RedLeft2 {
        START(-36,-61.5, Math.toRadians(90)),
        SPIKE_ALIGN(-46,-30, Math.toRadians(90)),
        SPIKE_LEFT(-45,-22, Math.toRadians(150)),
        SPIKE_CENTER(-36,-33, Math.toRadians(150)),
        SPIKE_RIGHT(-34,-36.5, Math.toRadians(-100)),
        BACKUP_RIGHT(-38,-36, Math.toRadians(-100)),
        BACKUP_CENTER(-36,-42, Math.toRadians(-100)),
        BACKUP_LEFT(-42,-18, Math.toRadians(-100)),

        BACKDROP_LEFT(48,-30,90),
        BACKDROP_CENTER(48,-42,90),
        BACKDROP_RIGHT(48,-46.5,Math.toRadians(179.9)),
        DOOR(32,-12,Math.toRadians(90)),
        TRUSS_IN(-36, -57,Math.toRadians(90)),
        TRUSS_OUT(32,-12,Math.toRadians(90)),
        ALIGN_TO_STACK(-52, -38,Math.toRadians(90)),
        STACK(-55.5, -40,Math.toRadians(180)),
        PARK(54,-64,Math.toRadians(0));

        public final double x;
        public final double y;
        public final double h;
        RedLeft2(double x_val, double y_val, double head){
            x=x_val;
            y=y_val;
            h=head;
        }
    }

    public enum BlueLeft {
        START(12,62, Math.toRadians(-90)),
        SPIKE_RIGHT(11,36,Math.toRadians(-150)),
        SPIKE_CENTER(22,30,Math.toRadians(-120)),
        SPIKE_LEFT(33,40,Math.toRadians(-130)),
        BACKDROP_RIGHT(52,27.5,Math.toRadians(180)),
        BACKDROP_CENTER(52.5,37.5,90),
        BACKDROP_LEFT(54,42,Math.toRadians(179.9)),
        TRUSS_IN(-36,-12,Math.toRadians(90)),
        TRUSS_OUT(-36,36,Math.toRadians(90)),
        STACK(-24,52,Math.toRadians(90)),
        PARK(58,58,Math.toRadians(0));

        public final double x;
        public final double y;
        public final double h;
        BlueLeft(double x_val, double y_val, double head){
            x=x_val;
            y=y_val;
            h=head;
        }
    }

    public enum BlueRight {
        START(-36,62, Math.toRadians(-90)),
        SPIKE_ALIGN(-38,38, Math.toRadians(-90)),
        SPIKE_LEFT(-33,36, Math.toRadians(100)),
        SPIKE_CENTER(-37,13.5, Math.toRadians(-160)),
        SPIKE_RIGHT(-43,16, Math.toRadians(-160)),
        BACKUP_RIGHT(-38,10, Math.toRadians(-100)),
        BACKUP_CENTER(-38,10, Math.toRadians(-100)),
        BACKUP_LEFT(-38,36, Math.toRadians(-100)),

        BACKDROP_LEFT(48,40,90),
        BACKDROP_CENTER(48,36,90),
        BACKDROP_RIGHT(48,32, Math.toRadians(179.9)),
        DOOR(32,10, Math.toRadians(90)),
        ALIGN_TO_STACK(-48,11,Math.toRadians(90)),
        STACK(-56,16,Math.toRadians(180)),
        PARK(54,62,Math.toRadians(0));

        public final double x;
        public final double y;
        public final double h;
        BlueRight(double x_val, double y_val, double head){
            x=x_val;
            y=y_val;
            h=head;
        }
    }

}
