package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Sensors
{
    private ColorSensor bottomC;

    private ColorSensor sideLC;
    private ColorSensor sideRC;
    private DistanceSensor sideLD;
    private DistanceSensor sideRD;

    private DistanceSensor frontD;
    private ColorSensor frontC;

    public Sensors(HardwareMap hardwareMap)
    {
        frontD = hardwareMap.get(DistanceSensor.class, "fDist");
        frontC = hardwareMap.get(ColorSensor.class, "fColorDist");
    }

    public double getBottomC ()
    {
        double red = bottomC.red();
        double blue = bottomC.blue();
        return (red > blue) ? red : blue;
    }

    public double getFrontDistance()
    {
        return frontD.getDistance(DistanceUnit.INCH);
    }

    public double getFrontRed ()
    {
        return frontC.red();
    }

    public double getFrontGreen ()
    {
        return frontC.green();
    }

    public double getFrontBlue ()
    {
        return frontC.blue();
    }


    public boolean getClawAlignment()
    {
        return false;
    }
}
