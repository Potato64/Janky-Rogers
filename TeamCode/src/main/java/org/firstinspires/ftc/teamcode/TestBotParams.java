package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;

@TeleOp
public class TestBotParams extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private NewPIDDriveBase driveBase;
    private Sensors sensors;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new NewPIDDriveBase(hardwareMap);
        sensors = new Sensors(hardwareMap);

        driveBase.setImuStabililzed(true);
        driveBase.setHeadless(true);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        driveBase.drive(-1, 0, 0);

        sleep(500);

        double lastDist = sensors.getBackDistance();
        double accumError = 0;
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 1)
        {
            driveBase.drive(-1, 0, 0);

            double currentDist = sensors.getBackDistance();
            double error = currentDist - lastDist;
            error += (error > PI / 2) ? -2*PI : (error < -PI / 2) ? 2*PI : 0;

            accumError += error;
            lastDist = currentDist;

        }

        double fullPowerVeloc = accumError / 1;

        telemetry.addData("Full Power Veloc: ", fullPowerVeloc);

        driveBase.setStablilizedHeading(PI);

        while (opModeIsActive() && runtime.seconds() < 10)
        {
            driveBase.drive(0, 0, 0);
        }

        while (opModeIsActive() && runtime.seconds() < 10.5)
        {
            driveBase.drive(0.6, 0, 0);
        }

        lastDist = sensors.getBackDistance();
        accumError = 0;
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 1)
        {
            driveBase.drive(0.6, 0, 0);

            double currentDist = sensors.getBackDistance();
            double error = currentDist - lastDist;
            error += (error > PI / 2) ? -2*PI : (error < -PI / 2) ? 2*PI : 0;

            accumError += error;
            lastDist = currentDist;

        }

        double point6PowerVeloc = accumError / 1;

        telemetry.addData("0.6 Power Veloc: ", point6PowerVeloc);

        driveBase.drive(0, 0, 0);
        driveBase.setStablilizedHeading(0);

        while (opModeIsActive() && runtime.seconds() < 10)
        {
            driveBase.drive(0, 0, 0);
        }

        while (opModeIsActive() && runtime.seconds() < 10.5)
        {
            driveBase.drive(-0.3, 0, 0);
        }

        lastDist = sensors.getBackDistance();
        accumError = 0;
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 1)
        {
            driveBase.drive(-0.3, 0, 0);

            double currentDist = sensors.getBackDistance();
            double error = currentDist - lastDist;
            error += (error > PI / 2) ? -2*PI : (error < -PI / 2) ? 2*PI : 0;

            accumError += error;
            lastDist = currentDist;

        }

        double point3PowerVeloc = accumError / 10;

        telemetry.addData("0.3 Power Veloc: ", point3PowerVeloc);

        telemetry.update();

        driveBase.drive(0, 0, 0);

        while (opModeIsActive())
        {
            driveBase.drive(0, 0, 0);
        }
    }
}
