package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveBase
{
    DcMotor frMotor;
    DcMotor flMotor;
    DcMotor brMotor;
    DcMotor blMotor;

    public DriveBase(HardwareMap hardwareMap)
    {
        frMotor = (DcMotor)hardwareMap.get("frMotor");
        flMotor = (DcMotor)hardwareMap.get("flMotor");
        brMotor = (DcMotor)hardwareMap.get("brMotor");
        blMotor = (DcMotor)hardwareMap.get("blMotor");
    }

    public void drive(double speed, double angle, double rot)
    {

    }
}
