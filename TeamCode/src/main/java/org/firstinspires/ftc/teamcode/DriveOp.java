package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class DriveOp
{

    Gamepad gamepad;

    public DriveOp(Gamepad gamepad)
    {
        this.gamepad = gamepad;
    }

    public double getAngle()
    {
        return atan2(-gamepad.left_stick_y , gamepad.left_stick_x) - PI / 2;
    }

    public double getPower()
    {
        return sqrt(gamepad.left_stick_x * gamepad.left_stick_x + gamepad.left_stick_y * gamepad.left_stick_y);
    }

    public double getRot()
    {
        return -gamepad.right_stick_x;
    }

    public boolean alignWithStone()
    {
        return gamepad.a;
    }
}
