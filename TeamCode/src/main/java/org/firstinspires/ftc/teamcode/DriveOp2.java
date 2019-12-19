package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class DriveOp2
{

    Gamepad gamepad;

    double lastTarget;

    public DriveOp2(Gamepad gamepad)
    {
        this.gamepad = gamepad;
    }

    public double getAngle()
    {
        return atan2(-gamepad.left_stick_y , gamepad.left_stick_x) - PI / 2;
    }

    public double getPower()
    {
        double power =  sqrt(gamepad.left_stick_x * gamepad.left_stick_x + gamepad.left_stick_y * gamepad.left_stick_y);
        return gamepad.x ? power / 2 : power;
    }

    public double setHeading()
    {
        double target = atan2(-gamepad.right_stick_y , gamepad.right_stick_x);
        if (target == 0)
        {
            return 0;
        }
        return lastTarget = target - PI / 2;
    }

    public boolean alignWithStone()
    {
        return gamepad.a;
    }
}
