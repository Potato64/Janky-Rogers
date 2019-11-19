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

    public Sensors(HardwareMap hardwareMap)
    {
        frontD = hardwareMap.get(DistanceSensor.class, "frontDistD");
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

    public boolean getFrontProximity()
    {
        return !Double.isNaN(frontD.getDistance(DistanceUnit.CM));
    }

    public boolean getClawAlignment()
    {
        return false;
    }
}
