package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import static java.lang.Math.PI;

public class AutoOpBB extends LinearOpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DriveOp driveOp;
    private DriveBase driveBase;
    private Hooks hooks;

    private DcMotor arm;
    private DcMotor claw;

    private BNO055IMU imu;
    private Sensors sensors;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =  "AdcmR0H/////AAABmX5FCuasR0WJva" +
            "W+WfKBZZY4Mkmr39mE5zeNXTSqAfbDNYf8W53AS1tK2Fgrwh1kN/CVmyZnbLlKxrn" +
            "I9kDObs2/9mddaFBkwb7TCKrCo4Cy/kqF85YWtWYekU3EqHzuMNvw1OonPzQJ7kjgB" +
            "hszai3nQKg5bN2dd8NBPFpScozkSlvIUrWFvbj20K1K7kQ3JjgMreeSNldB12C+Zeee" +
            "Ui9IVDBqfkcBOszPh0HvSMBGX3IkIkac56/UcTFbaa/GNWTwZtrVshjnypXm15Y1d62E" +
            "hg9G8wNHFMwhvzHqfGvLA++K7x/dCtB+iaPE7WNxom4RaE+UYJ8kDjbpjrbtiXPuqUN9+SbCwQK6IN86vAer";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void runOpMode()
    {
        driveBase = new DriveBase(hardwareMap);

        arm = hardwareMap.get(DcMotor.class, "arm");
        claw = hardwareMap.get(DcMotor.class, "claw");

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        sensors = new Sensors(hardwareMap);

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector())
        {
            initTfod();
        }
        else
        {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null)
        {
            tfod.activate();
        }

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        driveBase.drive(1, PI/4, 0);

        sleep(500);

        driveBase.drive(1, 0, 0);

        while(opModeIsActive() && sensors.getFrontDistance() > 1);

        hooks.extend();

        driveBase.drive(-1, 0, 0);

        sleep(5000);

        driveBase.drive(1, -PI/2, 0);

        while (opModeIsActive() && sensors.getBottomC() < 100);
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
