package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;

@Autonomous
public class Park extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DriveBase driveBase;

    private DcMotor lift;

    private final double innerStoneDist = 2.75;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new DriveBase(hardwareMap);

        driveBase.setImuStabililzed(true);
        driveBase.setHeadless(true);

        lift = hardwareMap.get(DcMotor.class, "lift");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        driveBase.drive(1, PI, 0);

        sleep(750);

        DriveBase.correction = driveBase.getHeading();
    }
}
