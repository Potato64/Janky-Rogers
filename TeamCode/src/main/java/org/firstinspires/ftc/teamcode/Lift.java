package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lift
{
    private DcMotor liftMotor;

    Lift(HardwareMap hardwareMap)
    {
        liftMotor = hardwareMap.get(DcMotor.class, "arm");
    }

    public void setPower(double power)
    {
        liftMotor.setPower(power);
    }
}
