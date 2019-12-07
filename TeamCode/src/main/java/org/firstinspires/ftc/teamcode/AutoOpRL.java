package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;

@Autonomous
@Disabled
public class AutoOpRL extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DriveOp driveOp;
    private DriveBase driveBase;

    private Lift lift;
    private DcMotor claw;

    private Sensors sensors;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new DriveBase(hardwareMap);
        lift = new Lift(hardwareMap);

//        driveBase.setImuStabililzed(true);

        claw = hardwareMap.get(DcMotor.class, "claw");

        sensors = new Sensors(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive() && sensors.getFrontDistance() > 7)
        {
            driveBase.drive(0.2 + (sensors.getFrontDistance() - 10) * 0.1, 0, 0);
        }

        driveBase.drive(0, 0, 0);

        sleep(500);

        getSkystone();

        placeStone();

        getSkystone();

        placeStone();

//        driveBase.drive(0.3, PI/2, 0);

//        while(opModeIsActive() && sensors.getBottomC() < 50);
    }

    private void getSkystone()
    {

        while (sensors.getFrontGreen() + sensors.getFrontRed() > 65 && opModeIsActive())
        {
            driveBase.drive(0.3, PI/2 - 0.1 * (sensors.getFrontDistance() - 3), -0.02);
        }

        driveBase.drive(0, 0, 0);

        sleep(2000);

//        pickSkystone();
    }

    private void placeStone()
    {
        driveBase.drive(-0.2, 0, 0);
        sleep(500);
        driveBase.drive(0, 0, -0.5);

//        while (opModeIsActive() && driveBase.getAngle() < PI/2);
        sleep(500);

        driveBase.drive(1, 0, 0);

        sleep(3000);

        while (opModeIsActive() && sensors.getFrontDistance() > 12);

        driveBase.drive(0, 0, 0.5);

//        while (opModeIsActive() && driveBase.getAngle() > 0);
        sleep(500);

//        placeSkystone();
    }

    private void pickSkystone()
    {
        driveBase.drive(0.2, 0, 0);

        while (opModeIsActive() && sensors.getFrontDistance() > 1);

        driveBase.drive(0, 0, 0);

        lift.setPower(-1);
        sleep(1000);
        lift.setPower(0);

        claw.setPower(-0.3);
        sleep(500);
        claw.setPower(0);

        lift.setPower(1);
        sleep(1000);
        lift.setPower(0);
    }

    private void placeSkystone()
    {
        while (opModeIsActive() && sensors.getFrontDistance() > 1);

        claw.setPower(0.3);

        sleep(500);

        claw.setPower(0);
    }
}
