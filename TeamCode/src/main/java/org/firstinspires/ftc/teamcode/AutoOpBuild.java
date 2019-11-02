package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.PI;

@Autonomous
public class AutoOpBuild extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DriveOp driveOp;
    private DriveBase driveBase;

    private DcMotor arm;

    private BNO055IMU imu;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveOp = new DriveOp(gamepad1);
        driveBase = new DriveBase(hardwareMap);

        arm = hardwareMap.get(DcMotor.class, "arm");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

//        arm.setPower(0.5);
//
//        sleep(1000);

        driveBase.drive(0.5, PI/2, 0);

        sleep(2000);

        driveBase.drive(0.5, PI, 0);

        sleep(500);
    }

}
