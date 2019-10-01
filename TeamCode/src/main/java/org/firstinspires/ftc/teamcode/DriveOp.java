package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
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
        if (gamepad.left_stick_x > 0)
        {
            return atan(gamepad.left_stick_y / gamepad.left_stick_x) - PI / 2;
        }
        else if (gamepad.left_stick_x < 0)
        {
            return atan(gamepad.left_stick_y / gamepad.left_stick_x) + PI / 2;
        }
        else
        {
            if (gamepad.left_stick_y < 0)
            {
                return 0;
            }
            else
            {
                return PI;
            }
        }
    }

    public double getPower()
    {
        return sqrt(gamepad.left_stick_x * gamepad.left_stick_x + gamepad.left_stick_y * gamepad.left_stick_y);
    }

    public double getRot()
    {
        return gamepad.right_stick_x;
    }
}
