package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hooks
{
    private Servo left;
    private Servo right;

    Hooks(HardwareMap hardwareMap)
    {
        left = hardwareMap.get(Servo.class, "clawL");
        right = hardwareMap.get(Servo.class, "clawR");
    }

    public void extend()
    {

    }

    public void retract()
    {

    }
}
