package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

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

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new DriveBase(hardwareMap);
        lift = new Lift(hardwareMap);

        driveBase.setImuStabililzed(true);

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

//        getSkystone();

//        placeStone();

//        driveBase.drive(0.3, PI/2, 0);

//        while(opModeIsActive() && sensors.getBottomC() < 50);
    }

    private void getSkystone()
    {

        while (sensors.getFrontRed() > 18 && opModeIsActive())
        {
            driveBase.drive(0.4, -PI/2 + 0.1 * (sensors.getFrontDistance() - 3.5), 0);
        }

        sleep(500);

        driveBase.drive(0, 0, 0);

        sleep(600);

        pickSkystone();
    }

    private void placeStone()
    {
        driveBase.drive(0.5, -11*PI/8, 0);
        sleep(1500);
        driveBase.drive(0, 0, 0.5);

        while (opModeIsActive() && driveBase.getAngle() < 5*PI/8);

        driveBase.drive(0.5, 0, 0);

        lift.setPower(0.3);

        sleep(1000);

        lift.setPower(0);

        sleep(3000);

        driveBase.drive(0, 0, 0);

        claw.setPower(0.5);
        sleep(200);
        claw.setPower(0);

        lift.setPower(-1);
        sleep(500);
        lift.setPower(0);

        driveBase.drive(-0.5, PI, 0);

        sleep(1500);
    }

    private void pickSkystone()
    {

        if (sensors.getFrontDistance() > 3.5)
        {
            driveBase.drive(0.2, 0, 0);
            while (opModeIsActive() && sensors.getFrontDistance() > 3.5);
        }
        else if (sensors.getFrontDistance() < 3.5)
        {
            driveBase.drive(-0.2, 0, 0);
            while (opModeIsActive() && sensors.getFrontDistance() < 3.5);
        }

        driveBase.drive(0, 0, 0);

        claw.setPower(1);
        sleep(575);
        claw.setPower(0);

        lift.setPower(0.3);
        sleep(2000);
        lift.setPower(0);

        claw.setPower(-0.5);
        sleep(875);
        claw.setPower(0);

        lift.setPower(-1);
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
