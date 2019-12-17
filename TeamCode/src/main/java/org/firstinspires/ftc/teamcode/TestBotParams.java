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

    private DriveBase driveBase;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new DriveBase(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        driveBase.drive(0, 0, 1);

        sleep(1000);

        double lastHeading = driveBase.getHeading();
        double accumError = 0;
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 10)
        {
            double currentHeading = driveBase.getHeading();
            double error = currentHeading - lastHeading;
            error += (error > PI / 2) ? -2*PI : (error < -PI / 2) ? 2*PI : 0;

            accumError += error;
            lastHeading = currentHeading;

        }

        double fullPowerVeloc = accumError / 10;

        telemetry.addData("Full Power Veloc: ", fullPowerVeloc);

        driveBase.drive(0, 0, 0.6);

        sleep(1000);

        lastHeading = driveBase.getHeading();
        accumError = 0;
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 10)
        {
            double currentHeading = driveBase.getHeading();
            double error = currentHeading - lastHeading;
            error += (error > PI / 2) ? -2*PI : (error < -PI / 2) ? 2*PI : 0;

            accumError += error;
            lastHeading = currentHeading;

        }

        double point6PowerVeloc = accumError / 10;

        telemetry.addData("0.6 Power Veloc: ", point6PowerVeloc);

        driveBase.drive(0, 0, 0.3);

        sleep(1000);

        lastHeading = driveBase.getHeading();
        accumError = 0;
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 10)
        {
            double currentHeading = driveBase.getHeading();
            double error = currentHeading - lastHeading;
            error += (error > PI / 2) ? -2*PI : (error < -PI / 2) ? 2*PI : 0;

            accumError += error;
            lastHeading = currentHeading;

        }

        double point3PowerVeloc = accumError / 10;

        telemetry.addData("0.3 Power Veloc: ", point3PowerVeloc);

        telemetry.update();

        driveBase.drive(0, 0, 0);

        while (opModeIsActive());
    }
}
