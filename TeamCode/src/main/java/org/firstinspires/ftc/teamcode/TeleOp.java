/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;


import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="Iterative Opmode")
public class TeleOp extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private int yar;
    private int ahoy;
    private int avastScurvy;
    private int jank;

    private DriveOp driveOp;
    private NewPIDDriveBase driveBase;
    private Lift lift;
    private Sensors sensors;
    private Intake intake;

    private CRServo capper;

    private DcMotor claw;

    private boolean wasA;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init()
    {
        driveOp = new DriveOp(gamepad1);
        driveBase = new NewPIDDriveBase(hardwareMap);
        lift = new Lift(hardwareMap);
        sensors = new Sensors(hardwareMap);
        intake = new Intake(hardwareMap);

        claw = hardwareMap.get(DcMotor.class, "claw");

        capper = hardwareMap.get(CRServo.class, "capper");

        driveBase.setImuStabililzed(true);
        driveBase.setHeadless(true);

        yar = hardwareMap.appContext.getResources().getIdentifier("yar", "raw", hardwareMap.appContext.getPackageName());
        SoundPlayer.getInstance().preload(hardwareMap.appContext, yar);
        ahoy = hardwareMap.appContext.getResources().getIdentifier("ahoy", "raw", hardwareMap.appContext.getPackageName());
        SoundPlayer.getInstance().preload(hardwareMap.appContext, ahoy);
        avastScurvy = hardwareMap.appContext.getResources().getIdentifier("avast_scurvy", "raw", hardwareMap.appContext.getPackageName());
        SoundPlayer.getInstance().preload(hardwareMap.appContext, avastScurvy);
        jank = hardwareMap.appContext.getResources().getIdentifier("jank", "raw", hardwareMap.appContext.getPackageName());
        SoundPlayer.getInstance().preload(hardwareMap.appContext, jank);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop()
    {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start()
    {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop()
    {
        if (driveOp.alignWithStone())
        {
            driveBase.drive((sensors.getFrontDistance() - 3.5) * 0.1, 0, 0);
        }
        else
        {
            driveBase.drive(driveOp.getPower(), driveOp.getAngle(), driveOp.getRot());
        }

        lift.setPower(gamepad2.left_stick_y);

        claw.setPower(-gamepad2.right_stick_y);

        if (gamepad2.a)
        {
            intake.setPower(1);
        }
        else if (wasA && !gamepad2.a)
        {
            intake.setPower(0);
            playRandomSound();
        }
        else if (gamepad2.b)
        {
            intake.setPower(-1);
        }
        else
        {
            intake.setPower(0);
        }
        wasA = gamepad2.a;

        if (gamepad2.right_bumper)
        {
            capper.setPower(0.7);
        }
        else if (gamepad2.left_bumper)
        {
            capper.setPower(-0.7);
        }

        telemetry.addData("Target Angle: ", driveOp.getAngle());
        telemetry.addData("Current Angle: ", driveBase.getHeading());

        telemetry.addData("frontD: ", sensors.getFrontDistance());
        telemetry.addData("backD: ", sensors.getBackDistance());

        telemetry.addData("Down Switch: ", lift.getDownState());
        telemetry.addData("Up Switch: ", lift.getUpState());
        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {

    }

    private void playRandomSound()
    {
        switch ((int)(4 * Math.random()))
        {
            case 0:
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, yar);
                break;
            case 1:
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, ahoy);
                break;
            case 2:
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, avastScurvy);
                break;
            case 3:
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, jank);
        }
    }

}
