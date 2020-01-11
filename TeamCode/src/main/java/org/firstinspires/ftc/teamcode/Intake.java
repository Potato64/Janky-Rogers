package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake
{
    private DcMotor left;
    private DcMotor right;

    Intake(HardwareMap hardwareMap)
    {
        left = hardwareMap.get(DcMotor.class, "intakeL");
        right = hardwareMap.get(DcMotor.class, "intakeR");

        left.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setPower(double power)
    {
        left.setPower(power);
        right.setPower(power);
    }
}
