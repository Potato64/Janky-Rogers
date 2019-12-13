package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.PI;

@Autonomous
public class AutoOpBB extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DriveOp driveOp;
    private DriveBase driveBase;
    private Lift lift;

    private DcMotor claw;

    private BNO055IMU imu;
    private Sensors sensors;

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

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        sensors = new Sensors(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        double now = runtime.milliseconds();

        while (opModeIsActive() && runtime.milliseconds() - now < 2000)
        {
            driveBase.drive(1, 0, 0);
        }

        driveBase.drive(0, 0, 0.5);

        while(opModeIsActive() && driveBase.getAngle() < PI/2 - 0.1);

        driveBase.setStablilizedHeading(PI/2);

        while(opModeIsActive() && sensors.getFrontDistance() > 4)
        {
            driveBase.drive(0.07 + (sensors.getFrontDistance() - 3) * 0.1, 0, 0);
        }

        lift.setPower(0.3);
        sleep(1500);
        lift.setPower(0);

        now = runtime.milliseconds();
        while (opModeIsActive() && runtime.milliseconds() - now < 5000)
        {
            driveBase.drive(1, PI, 0);
        }

        driveBase.drive(-1, -PI/2, 0);

        sleep(500);
    }
}
