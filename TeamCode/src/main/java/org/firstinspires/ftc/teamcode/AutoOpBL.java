package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;

@Autonomous
public class AutoOpBL extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DriveOp driveOp;
    private DriveBase driveBase;

    private Lift lift;
    private DcMotor claw;

    private Sensors sensors;

    private int skystonePos;

    private final double innerStoneDist = 2.75;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new DriveBase(hardwareMap);
        lift = new Lift(hardwareMap);

        driveBase.setImuStabililzed(true);
        driveBase.setHeadless(true);

        claw = hardwareMap.get(DcMotor.class, "claw");

        sensors = new Sensors(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive() && sensors.getFrontDistance() > 7)
        {
            driveBase.drive(0.1 + (sensors.getFrontDistance() - 7) * 0.07, 0, 0);
        }

        driveBase.drive(0, 0, 0);

        sleep(500);

        getSkystone();

        placeStone();

        double targetPos;

        switch(skystonePos)
        {
            case 1:
                targetPos = 16;
                break;
            case 2:
                targetPos = 8;
                break;
            default:
                targetPos = 8;
        }

//        while (opModeIsActive() && sensors.getBackDistance() > 48)
//        {
//            driveBase.drive(-0.5, PI/2, 0);
//        }

        while (opModeIsActive() && sensors.getBackDistance() > targetPos + 1)
        {
            driveBase.drive(-(sensors.getBackDistance() - targetPos + 5) * 0.02, PI/2 + 0.1, 0);
        }

        driveBase.drive(0, 0, 0);

//        getSkystone();

//        placeStone();

//        driveBase.drive(0.3, PI/2, 0);

//        while(opModeIsActive() && sensors.getBottomC() < 50);
    }

    private void getSkystone()
    {

        while (sensors.getFrontRed() > 18 && opModeIsActive())
        {
            driveBase.drive(0.5, -PI/2 + 0.1 * (sensors.getFrontDistance() - 3.5), 0);
        }

        sleep(500);

        driveBase.drive(0, 0, 0);

        pickSkystone();
    }

    private void placeStone()
    {
        driveBase.drive(0.5, PI, 0);
        sleep(600);
        driveBase.drive(0, 0, 0.5);

        while (opModeIsActive() && driveBase.getHeading() < PI/2 - 0.1);

        driveBase.setStablilizedHeading(PI/2);

        double backDist = sensors.getBackDistance();

        if (backDist < 22)
        {
            skystonePos = 3;
        }
        else if (backDist < 30)
        {
            skystonePos = 2;
        }
        else
        {
            skystonePos = 1;
        }

        telemetry.addData("backD: ", backDist);
        telemetry.addData("Skystone Pos: ", skystonePos);
        telemetry.update();

        driveBase.drive(0.5, PI/2, 0);

        lift.setPower(0.3);

        sleep(1000);

        driveBase.drive(1, PI/2, 0);

        lift.setPower(0);

        sleep(1000);

        driveBase.drive(0, PI/2, 0);

        claw.setPower(0.5);
        sleep(200);
        claw.setPower(0);

        lift.setPower(-1);
        sleep(750);
        lift.setPower(0);
    }

    private void pickSkystone()
    {

        if (sensors.getFrontDistance() > innerStoneDist)
        {
            driveBase.drive(0.2, 0, 0);
            while (opModeIsActive() && sensors.getFrontDistance() > innerStoneDist);
        }
        else if (sensors.getFrontDistance() < innerStoneDist)
        {
            driveBase.drive(-0.2, 0, 0);
            while (opModeIsActive() && sensors.getFrontDistance() < innerStoneDist);
        }

        driveBase.drive(0, 0, 0);

        claw.setPower(1);
        sleep(1000);
        claw.setPower(0);

        lift.setPower(0.3);
        sleep(1500);
        lift.setPower(0);

        claw.setPower(-0.5);
        sleep(1200);
        claw.setPower(0);

        lift.setPower(-1);
        sleep(1000);
        lift.setPower(0);
    }
}
