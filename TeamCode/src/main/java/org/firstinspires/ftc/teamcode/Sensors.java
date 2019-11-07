package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Sensors
{
    ColorSensor bottomC;

    ColorSensor sideLC;
    ColorSensor sideRC;
    DistanceSensor sideLD;
    DistanceSensor sideRD;

    DistanceSensor frontD;

    public Sensors(HardwareMap hardwareMap)
    {
        frontD = hardwareMap.get(DistanceSensor.class, "frontDist");
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

    public boolean getClawAlignment()
    {
        return false;
    }
}
