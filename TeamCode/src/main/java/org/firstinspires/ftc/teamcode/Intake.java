package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake
{
    private DcMotor left;
    private DcMotor right;

    Intake(HardwareMap hardwareMap)
    {
        left = hardwareMap.get(DcMotor.class, "intakeL");
        right = hardwareMap.get(DcMotor.class, "intakeR");

        right.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void intake()
    {
        left.setPower(1);
        right.setPower(1);
    }

    public void expel()
    {
        left.setPower(-1);
        right.setPower(-1);
    }
}
