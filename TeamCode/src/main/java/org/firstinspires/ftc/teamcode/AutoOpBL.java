package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
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

    private BNO055IMU imu;
    private Sensors sensors;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new DriveBase(hardwareMap);
        lift = new Lift(hardwareMap);

        claw = hardwareMap.get(DcMotor.class, "claw");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        sensors = new Sensors(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        driveBase.drive(0.5, 0, 0);

        while (opModeIsActive() && sensors.getFrontDistance() > 300);

        getSkystone();

        placeStone();

        getSkystone();

        placeStone();

        driveBase.drive(0.5, -PI/2, 0);

        while(opModeIsActive() && sensors.getBottomC() < 50);
    }

    private void getSkystone()
    {
        driveBase.drive(1, -PI/2, 0);

        sleep(5000);

        pickSkystone();
    }

    private void pickSkystone()
    {

        while (sensors.getFrontGreen() + sensors.getFrontRed() < 200 && opModeIsActive())
        {
            driveBase.drive(0.5, -PI/2 - (sensors.getFrontDistance() - 3), 0);
        }

        driveBase.drive(0.2, 0, 0);

        while (opModeIsActive() && !sensors.getClawAlignment());

        claw.setPower(-0.3);

        sleep(500);

        claw.setPower(0);
    }

    private void placeStone()
    {
        driveBase.drive(1, PI/2, 0);

        sleep(5000);
    }
}
