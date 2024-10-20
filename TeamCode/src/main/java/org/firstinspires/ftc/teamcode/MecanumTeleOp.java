package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="TeleOp Program", group="TeleOp")
public class MecanumTeleOp extends OpMode {


    Robot robot = new Robot();
    ElapsedTime timer = new ElapsedTime();
    private static final double ticks = 537.7;
    double LM_Ticks; //Find ticks of linear motion motor
    double target;
    public DcMotorEx right_b;
    public DcMotorEx left_f;
    public DcMotorEx right_f;
    public DcMotorEx left_b;


    public Servo left_servo;
    public CRServo intake_servo;
    public Servo claw_rot;

    public Servo specimen_grabber;


    private boolean servoToggled = false;
    private boolean lift_servo_toggled = false;
    private boolean isSpecimen_grab_toggle = false;
    private boolean spit_toggled = false;
    private boolean specimen_grab = false;
    private boolean spin = false;
    private boolean hawk_tuah = false;
    private boolean aPressedLast = false;
    private boolean bPressedLast = false;
    private boolean yPressedLast = false;
    private boolean xPressedLast = false;
    private boolean lbPressedLast = false;
    private boolean taskInProgress = false;

    private boolean isInOriginalPosition = true;

    private static final int SPECIMEN_DELAY_MS = 1000;
    private boolean rightBumperTaskInProgress = false;
    private int rightBumperTaskStep = 0; // Track the step in the bumper task

    private ElapsedTime rightBumperTimer = new ElapsedTime();

    private int taskStep = 0;  // Track which step of the task is executing
    private static final int TASK_DELAY_MS = 500;  // 200ms delay

    //Code to run ONCE after the driver hits INIT
    @Override
    public void init() {


        robot.init(hardwareMap);


        left_f = hardwareMap.get(DcMotorEx.class, "left_front");
        right_f = hardwareMap.get(DcMotorEx.class, "right_front");
        left_b = hardwareMap.get(DcMotorEx.class, "left_back");
        right_b = hardwareMap.get(DcMotorEx.class, "right_back");


        right_f.setDirection(DcMotorSimple.Direction.REVERSE);
        right_b.setDirection(DcMotorSimple.Direction.REVERSE);


        left_servo = hardwareMap.get(Servo.class, "left_servo");
        intake_servo = hardwareMap.get(CRServo.class, "intake_servo");
        claw_rot = hardwareMap.get(Servo.class, "intake_lift");
        specimen_grabber = hardwareMap.get(Servo.class, "specimen_grabber");



        claw_rot.setPosition(1);
        left_servo.setPosition(0.015);
        specimen_grabber.setPosition(0.5);


    }

    //Code to run REPEATEDLY after the driver hits INIT
    @Override
    public void init_loop() {


        telemetry.addData("linear_claw", robot.linear_C.linear_claw.getCurrentPosition());
        telemetry.addData("left_servo_position", left_servo.getPosition());
        telemetry.update();

    }


    //Code to run ONCE after the driver hits PLAY
    @Override
    public void start() {


    }


    //Code to run REPEATEDLY after the driver hits PLAY
    @Override
    public void loop() {


        double x = -gamepad1.right_stick_x;
        double y = -gamepad1.left_stick_y;
        double rx = -gamepad1.left_stick_x;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);


        //Hardware needs to change motors for increased speed.


        double drivePowerScale = gamepad1.right_bumper ? 0.5 : 1.0;

        // Apply scaled power to drivetrain motors
        right_f.setPower(((y + x + rx) / denominator) * drivePowerScale);
        left_b.setPower(((y - x + rx) / denominator) * drivePowerScale);
        left_f.setPower(((y - x - rx) / denominator) * drivePowerScale);
        right_b.setPower(((y + x - rx) / denominator) * drivePowerScale);



       /*
       double[] driveVelocities =
               robot.driveTrain.drive(
                       gamepad1.left_stick_x,
                       gamepad1.left_stick_y,
                       gamepad1.right_stick_x);


       robot.driveTrain.setDriveVelocities(driveVelocities);
       */

        //Mecanum Drive Train ---- Test this out

       /*
       if (gamepad1.dpad_up) {
           robot.linear_L.linear_motion_left.setPower(1);
           robot.linear_R.linear_motion_right.setPower(0.95);
       } else if (gamepad1.dpad_down) {
           robot.linear_L.linear_motion_left.setPower(-1);
           robot.linear_R.linear_motion_right.setPower(-0.95);

       }
       */


        robot.linear_L.linear_motion_left.setPower(gamepad2.left_stick_y);
        robot.linear_R.linear_motion_right.setPower(gamepad2.left_stick_y);


        robot.linear_C.linear_claw.setPower(gamepad2.right_stick_y);


       if (gamepad2.left_bumper){
           left_servo.setPosition(0.8);
       }


        if (gamepad2.a && !aPressedLast) {
            if (!taskInProgress) {
                taskInProgress = true;
                taskStep = 1;
                isInOriginalPosition = !isInOriginalPosition;  // Toggle the state
                servoToggled = false;  // Ensure toggle starts fresh
                timer.reset();  // Reset the timer
            }
        }

// Task sequence management with non-blocking delays
        if (taskInProgress && timer.milliseconds() > TASK_DELAY_MS) {
            switch (taskStep) {
                case 1:
                    servoToggled = !servoToggled;
                    intake_servo.setPower(servoToggled ? 0.3 : 0);  // Activate intake
                    taskStep++;  // Move to step 2
                    break;

                case 2:
                    specimen_grabber.setPosition(servoToggled ? 0.93 : 0.5);  // Adjust position
                    taskStep++;  // Move to step 3
                    break;

                case 3:
                    left_servo.setPosition(servoToggled ? 0.5 : 0.01);  // Adjust servo
                    taskInProgress = false;  // End task
                    break;

                default:
                    taskInProgress = false;  // Safety fallback
                    break;
            }
            timer.reset();  // Reset timer for the next step
        }

// Reset the task using the right bumper
        if (gamepad2.right_bumper && !lbPressedLast) {
            specimen_grabber.setPosition(0.5);
            left_servo.setPosition(0.015);
            //claw_rot.setPosition(0.05);

            taskInProgress = false;  // Allow task to restart
            taskStep = 0;  // Reset task step
        }
        lbPressedLast = gamepad2.right_bumper;

        if (gamepad2.right_bumper && !lbPressedLast && !rightBumperTaskInProgress) {
            // Start the task sequence only if it isn't already running
            rightBumperTaskInProgress = true;
            rightBumperTaskStep = 1;
            rightBumperTimer.reset();
        }

// Update button state
        aPressedLast = gamepad2.a;



        if (gamepad2.x && !xPressedLast){
            spin = !spin;
            intake_servo.setPower(spin ? -1 : 0);
        }
        xPressedLast = gamepad2.x;

        if (gamepad2.y && yPressedLast){
            isSpecimen_grab_toggle = !isSpecimen_grab_toggle;
            specimen_grabber.setPosition(isSpecimen_grab_toggle ? 0.50 : 0.90);
        }
        yPressedLast = gamepad2.y;


        if (gamepad2.b && !bPressedLast) {
            lift_servo_toggled = !lift_servo_toggled;
            claw_rot.setPosition(lift_servo_toggled ? 0.05: 1);
        }
        bPressedLast = gamepad2.b;




        if (rightBumperTaskInProgress && rightBumperTimer.milliseconds() > SPECIMEN_DELAY_MS) {
            switch (rightBumperTaskStep) {
                case 1:
                    specimen_grabber.setPosition(0.5); // Adjust specimen grabber first
                    rightBumperTaskStep++; // Move to step 2
                    break;

                case 2:
                    left_servo.setPosition(0.015); // Adjust left servo after delay
                    rightBumperTaskStep++; // Move to step 3
                    break;

                case 3:
                    claw_rot.setPosition(0.05); // Final adjustment
                    rightBumperTaskStep = 0; // Reset step counter
                    break;
                case 4:
                    intake_servo.setPower(1);
                    rightBumperTaskInProgress = false; // End task
                    taskInProgress = false; // Allow other tasks to start
                    break;

                default:
                    rightBumperTaskInProgress = false; // Safety fallback
                    break;
            }
            rightBumperTimer.reset(); // Reset timer for the next step
        }

        lbPressedLast = gamepad2.right_bumper;



    }



    //Method to move motor to designated position
    public void encoder(int turnage, double power){
        robot.linear_C.linear_claw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        target = ticks/turnage;
        robot.linear_C.linear_claw.setTargetPosition((int) target);
        robot.linear_C.linear_claw.setPower(-power);
        robot.linear_C.linear_claw.setMode(DcMotor.RunMode.RUN_TO_POSITION);


    }
}