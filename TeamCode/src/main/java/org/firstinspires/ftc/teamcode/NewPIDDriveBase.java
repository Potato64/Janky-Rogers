package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.util.function.Function;

import static java.lang.Math.E;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.signum;
import static java.lang.Math.sin;

public class NewPIDDriveBase
{
    private DcMotor frMotor;
    private DcMotor flMotor;
    private DcMotor brMotor;
    private DcMotor blMotor;

    private BNO055IMU imu;

    private boolean headless;
    private boolean imuStabililzed;
    private boolean imuStabilizedTracker;

    private double targetHeading;
    private static final double rotStabKp = 0.35;
    private static final double rotStabKi = 0.07;
    private static final double baseMaxRot = 3.3;
    private double rotStabAccum;

    private static final Function timeFromError = new Function<Double, Double>()
    {
        @Override
        public Double apply(Double error)
        {
            if (error < Math.pow(E, -baseMaxRot))
            {
                return 0.0;
            }
            else if (error < 1 / rotStabKp)
            {
                return log(error) / baseMaxRot / rotStabKp + 1 / rotStabKp;
            }
            return (error - 1 / rotStabKp) / baseMaxRot + log(1 / rotStabKp) / baseMaxRot / rotStabKp + 1 / rotStabKp;
        }
    };

    private static final Function errorFromTime = new Function<Double, Double>()
    {
        @Override
        public Double apply(Double time)
        {
            if (time < 0)
            {
                return 0.0;
            }
            else if (time < log(1 / rotStabKp) / baseMaxRot / rotStabKp + 1 / rotStabKp)
            {
                return pow(E, baseMaxRot * rotStabKp - baseMaxRot);
            }
            return baseMaxRot * time - log(1 / rotStabKp) / rotStabKp - baseMaxRot / rotStabKp + 1 / rotStabKp;
        }
    };

    private ElapsedTime pidTimer;
    private double initialTime;
    private double initErrorSign;

    private static final double wCoef = 1/(sin(23 * PI/36));

    public NewPIDDriveBase(HardwareMap hardwareMap)
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
        rotStabAccum = 0;

        targetHeading = 0;

        pidTimer = new ElapsedTime();
        initialTime = 0;
    }

    public void drive(double speed, double direction, double rot)
    {
        double currentAngle = getHeading();
        if (headless)
        {
            direction -= currentAngle;
        }
        if (imuStabililzed)
        {
            if (!imuStabilizedTracker && rot == 0)
            {
                double error = currentAngle - targetHeading;
                error += (error > PI) ? -2*PI : (error < -PI) ? 2*PI : 0;

                rotStabAccum += error - initErrorSign * (double)errorFromTime.apply(initialTime - pidTimer.seconds());

                rot -= rotStabKp * error + rotStabKi * rotStabAccum;
            }
            else if (imuStabilizedTracker && rot != 0)
            {
                targetHeading = currentAngle;
                rotStabAccum = 0;
                imuStabilizedTracker = false;
            }
            else
            {
                imuStabilizedTracker = true;
            }
        }

        double flbr = flbrPower(direction);
        double frbl = frblPower(direction);

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

    public void setStablilizedHeading(double heading)
    {
        targetHeading = heading;

        double error = getHeading() - targetHeading;
        error += (error > PI) ? -2*PI : (error < -PI) ? 2*PI : 0;

        if (abs(error) > 1)
        {
            initialTime = (double) timeFromError.apply(abs(error));
            initErrorSign = signum(error);
            pidTimer.reset();
        }
    }

    public double getHeading()
    {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
    }

    public double getTargetHeading()
    {
        return targetHeading;
    }

    private double flbrPower(double angle)
    {
        return wCoef * sin(angle + 23 * PI / 36);
    }

    private double frblPower(double angle)
    {
        return wCoef * cos(angle - 5 * PI / 36);
    }
    public double getPidTime()
    {
        return initialTime - pidTimer.seconds();
    }

    public double getErrorFunct()
    {
        return initErrorSign * (double)errorFromTime.apply(initialTime - pidTimer.seconds());
    }

    public double getRotStabAccum()
    {
        return rotStabAccum;
    }
}
