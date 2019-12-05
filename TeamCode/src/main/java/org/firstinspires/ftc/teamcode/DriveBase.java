package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class DriveBase
{
    private DcMotor frMotor;
    private DcMotor flMotor;
    private DcMotor brMotor;
    private DcMotor blMotor;

    private BNO055IMU imu;

    private boolean headless;
    private boolean imuStabililzed;
    private boolean imuStabilizedTracker;

    private double targetAngle;
    private final double rotStabCoef = 0.1;

    private static final double wCoef = 1/(sin(23 * PI/36));

    public DriveBase(HardwareMap hardwareMap)
    {
        frMotor = (DcMotor)hardwareMap.get("frMotor");
        flMotor = (DcMotor)hardwareMap.get("flMotor");
        brMotor = (DcMotor)hardwareMap.get("brMotor");
        blMotor = (DcMotor)hardwareMap.get("blMotor");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        blMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        brMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        headless = false;
        imuStabililzed = false;

        targetAngle = 0;
    }

    public void drive(double speed, double angle, double rot)
    {
        double currentAngle = getAngle();
        if (headless)
        {
            angle -= currentAngle;
        }
        if (imuStabililzed)
        {
            if (!imuStabilizedTracker)
            {
                targetAngle = currentAngle;
                imuStabilizedTracker = true;
            }
            rot += rotStabCoef * (currentAngle - targetAngle);
        }

        double flbr = flbrPower(angle);
        double frbl = frblPower(angle);

        double maxMovePower = abs(flbr) > abs(frbl) ? abs(flbr) : abs(frbl);
        flbr /= maxMovePower;
        frbl /= maxMovePower;

        double maxPower = speed * (maxMovePower + abs(rot));

        if (maxPower > 1)
        {
            frMotor.setPower((frbl * speed + rot) / maxPower);
            flMotor.setPower((flbr * speed - rot) / maxPower);
            blMotor.setPower((frbl * speed - rot) / maxPower);
            brMotor.setPower((flbr * speed + rot) / maxPower);
        }
        else
        {
            frMotor.setPower(frbl * speed + rot);
            flMotor.setPower(flbr * speed - rot);
            blMotor.setPower(frbl * speed - rot);
            brMotor.setPower(flbr * speed + rot);
        }
    }

    public void setHeadless(boolean isHeadless)
    {
        headless = isHeadless;
    }

    public void setImuStabililzed(boolean isImuStabilized)
    {
        imuStabililzed = isImuStabilized;
    }

    public double getAngle()
    {
        return imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).thirdAngle;
    }

    private double flbrPower(double angle)
    {
        return wCoef * sin(angle + 23 * PI / 36);
    }

    private double frblPower(double angle)
    {
        return wCoef * cos(angle - 5 * PI / 36);
    }
}
