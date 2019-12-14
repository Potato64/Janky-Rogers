package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;

public class AutoOpRB extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DriveOp driveOp;
    private DriveBase driveBase;
    private Lift lift;

    private DcMotor claw;

    private Sensors sensors;

    private final double foundationDist = 2.05;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new DriveBase(hardwareMap);

        driveBase.setImuStabililzed(true);
        driveBase.setHeadless(true);

        lift = new Lift(hardwareMap);
        claw = hardwareMap.get(DcMotor.class, "claw");

        sensors = new Sensors(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while(opModeIsActive() && sensors.getFrontDistance() > 8.5)
        {
            driveBase.drive(0.1 + (sensors.getFrontDistance() - 5) * 0.1, -PI / 8, 0);
        }

        driveBase.drive(0, 0, 0);

        sleep(200);

        double distI = 0;
        double lastDist = sensors.getFrontDistance() - foundationDist;

        while(opModeIsActive() && (Math.abs(sensors.getFrontDistance() - foundationDist) > 0.03 || Math.abs(driveBase.getAngle()) > 0.03))
        {
            double distError = sensors.getFrontDistance() - foundationDist;
            distI += distError;
            driveBase.drive(distError * 0.08 + distI * 0.045 + (distError - lastDist) * 0.055, 0, 0);
            lastDist = distError;
        }

        driveBase.drive(0, 0, 0);
//
        lift.setPower(0.3);
        sleep(1000);
        lift.setPower(0);

        while (opModeIsActive() && sensors.getBackDistance() > 5)
        {
            driveBase.drive(sensors.getBackDistance() * 0.02 + 0.5, 7*PI/8, 0);
        }

        driveBase.drive(0, 0, 0.5);

        while (opModeIsActive() && driveBase.getAngle() < -PI/4);

        driveBase.drive(0, 0, 0);

        lift.setPower(-1);
        sleep(750);
        lift.setPower(0);

        driveBase.setStablilizedHeading(-PI/2);

        double now = runtime.milliseconds();
        while (opModeIsActive() && runtime.milliseconds() - now < 2400)
        {
            driveBase.drive(1, PI/2, 0);
        }
    }
}
