package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class DriveBase
{
    DcMotor frMotor;
    DcMotor flMotor;
    DcMotor brMotor;
    DcMotor blMotor;

    private static final double wCoef = 1/(sin(23 * PI/36));

    public DriveBase(HardwareMap hardwareMap)
    {
        frMotor = (DcMotor)hardwareMap.get("frMotor");
        flMotor = (DcMotor)hardwareMap.get("flMotor");
        brMotor = (DcMotor)hardwareMap.get("brMotor");
        blMotor = (DcMotor)hardwareMap.get("blMotor");

        frMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        brMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(double speed, double angle, double rot)
    {
        double flbr = flbrPower(angle);
        double frbl = frblPower(angle);

        double maxPower = speed * (abs(flbr) > abs(frbl) ? abs(flbr) : abs(frbl)) + abs(rot);

        frMotor.setPower((frbl * speed - rot) / maxPower);
        flMotor.setPower((flbr * speed + rot) / maxPower);
        blMotor.setPower((frbl * speed + rot) / maxPower);
        brMotor.setPower((flbr * speed - rot) / maxPower);
    }

    private double flbrPower(double angle)
    {
        return wCoef * sin(angle - 23 * PI / 36);
    }

    private double frblPower(double angle)
    {
        return wCoef * cos(angle - 5 * PI / 36);
    }
}
