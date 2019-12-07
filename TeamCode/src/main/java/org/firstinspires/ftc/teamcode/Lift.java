package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.SerialNumber;

public class Lift
{
    private DcMotor liftMotor;

    private DigitalChannel downSwitch;
    private DigitalChannel upSwitch;

    Lift(HardwareMap hardwareMap)
    {
        liftMotor = hardwareMap.get(DcMotor.class, "lift");

        downSwitch = hardwareMap.get(DigitalChannel.class, "downSwitch");
        upSwitch = hardwareMap.get(DigitalChannel.class, "upSwitch");
    }

    public void setPower(double power)
    {
        if (downSwitch.getState() && power > 0)
        {
            liftMotor.setPower(0);
        }
        else if (upSwitch.getState() && power < 0)
        {
            liftMotor.setPower(0);
        }
        else
        {
            liftMotor.setPower(power);
        }
    }

    public boolean getDownState()
    {
        return downSwitch.getState();
    }
    public boolean getUpState()
    {
        return upSwitch.getState();
    }
}
