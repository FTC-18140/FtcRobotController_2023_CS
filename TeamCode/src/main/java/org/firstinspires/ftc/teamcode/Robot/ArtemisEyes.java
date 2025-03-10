package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

public class ArtemisEyes
{
    private WebcamName theCamera;
    public TGEVisionProcessor tgeFinder;
    VisionPortal thePortal;
    Telemetry telemetry;
    void init(HardwareMap hardwareMap, Telemetry telem )
    {
        telemetry = telem;
        try
        {
            theCamera = hardwareMap.get(WebcamName.class, "Webcam 1");
        }
        catch (Exception e)
        {
            telemetry.addData("Webcam 1 not found -- theFrontCamera", 0);
        }
        try
        {
            tgeFinder = new TGEVisionProcessor();
            tgeFinder.setTelemetry(telemetry);
            thePortal = VisionPortal.easyCreateWithDefaults(theCamera, tgeFinder);
            thePortal.stopLiveView();
        }
        catch (Exception e)
        {
            telemetry.addData("exception:  ", e.getMessage());
        }


    }

    public String getSpikePos() {
        if (tgeFinder != null) {
            if (thePortal.getProcessorEnabled(tgeFinder)){
                telemetry.addData("TGE Enabled YES!!", 0);
            }
            return tgeFinder.getSpikePos();

        } else {
            return "TGEFINDER NOT INITIALIZED";
        }


    }

    public double getPropX() {
        if (tgeFinder != null) {
            return tgeFinder.xPos;
        } else {
            return -1;
        }
    }
    public double getPropY() {
        if (tgeFinder != null) {
            return tgeFinder.yPos;
        } else {
            return -1;
        }
    }


    public void stopPropVisionProcessor()
    {
        if ( tgeFinder != null )
        {
            thePortal.setProcessorEnabled(tgeFinder, false);
        }
        else {
            telemetry.addData("Can't disable Prop Vision Processor. Not initialized.", 0);
        }
    }

    public void startPropVisionProcessor()
    {
        if ( tgeFinder != null )
        {
            thePortal.setProcessorEnabled(tgeFinder, true);
        }
        else {
            telemetry.addData("Can't enable Prop Vision Processor. Not initialized.", 0);
        }
    }


}
