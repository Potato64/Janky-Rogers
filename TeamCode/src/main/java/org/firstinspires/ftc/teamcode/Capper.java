package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Capper
{
    private CRServo hand;

    Capper(HardwareMap hardwareMap)
    {
        hand = hardwareMap.get(CRServo.class, "intakeL");
    }

    public void setPower(double power)
    {
        hand.setPower(power);
    }
}
