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


    public Servo claw_yaw;
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


    public int claw_yaw_pressed = 0;


    private static final double SERVO_INCREMENT = 0.05;
    private static final double MIN_SERVO_POSITION = 0.0;
    private static final double MAX_SERVO_POSITION = 1.0;


    private boolean rightBumperPressedLast = false;
    private boolean leftBumperPressedLast = false;






    public double new_pos;


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
        claw_yaw = hardwareMap.get(Servo.class, "claw_yaw");






        claw_rot.setPosition(1);
        left_servo.setPosition(0.065);
        specimen_grabber.setPosition(0.4);
        claw_yaw.setPosition(0.2);








    }


    //Code to run REPEATEDLY after the driver hits INIT
    @Override
    public void init_loop() {




        //telemetry.addData("linear_claw", robot.linear_C.linear_claw.getCurrentPosition());
        telemetry.addData("left_servo_position", left_servo.getPosition());


        telemetry.addData("left_front", left_f.getCurrentPosition());
        telemetry.addData("right_front", right_f.getCurrentPosition());
        telemetry.addData("left_back", left_b.getCurrentPosition());
        telemetry.addData("right_back", right_b.getCurrentPosition());






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
        //----------------------------------------------------------------------------------------------------------------------------------------
        robot.lift.doTelemetry(telemetry);
        telemetry.addData("Input val", -gamepad2.left_stick_y * 0.50 * robot.lift.MAX_VEL);


        robot.linear_C.linear_claw_Telemetry(telemetry);


        telemetry.addData("left_slide_input", -gamepad2.left_stick_y * 0.50 * robot.lift.MAX_VEL);


        telemetry.update();


        double input_vel = gamepad2.right_stick_y * 0.75 * robot.linear_C.MAX_VEL;




//        double left_slide_vel = -joystickY * 0.50 * robot.lift.MAX_VEL;
//        double right_slide_vel = -joystickY * 0.50 * robot.lift.MAX_VEL;




        if (input_vel < 0 && robot.linear_C.linear_claw.getCurrentPosition() >= robot.linear_C.OUTER_BOUND
                || input_vel > 0 && robot.linear_C.linear_claw.getCurrentPosition() <= robot.linear_C.INNER_BOUND) {
            robot.linear_C.linear_claw.setVelocity(input_vel);
        } else {
            robot.linear_C.linear_claw.setVelocity(0);
        }




        double joystickY = gamepad2.left_stick_y;
        if (Math.abs(joystickY) < 0.05) { // Dead zone to ignore small inputs
            joystickY = 0;
            robot.lift.left_slide.setVelocity(0);
            robot.lift.right_slide.setVelocity(0);
        } else {
            double left_slide_vel = -joystickY * 1 * robot.lift.MAX_VEL;
            double right_slide_vel = -joystickY * 1 * robot.lift.MAX_VEL;


            // Set velocities only within bounds
            if ((left_slide_vel > 0 && robot.lift.left_slide.getCurrentPosition() <= robot.lift.MAX_BOUNDS) ||
                    (left_slide_vel < 0 && robot.lift.left_slide.getCurrentPosition() >= robot.lift.STARTING_BOUNDS)) {
                robot.lift.left_slide.setVelocity(left_slide_vel);
            } else {
                robot.lift.left_slide.setVelocity(0); // Stop if out of bounds
            }


            if ((right_slide_vel > 0 && robot.lift.right_slide.getCurrentPosition() <= robot.lift.MAX_BOUNDS) ||
                    (right_slide_vel < 0 && robot.lift.right_slide.getCurrentPosition() >= robot.lift.STARTING_BOUNDS)) {
                robot.lift.right_slide.setVelocity(right_slide_vel);
            } else {
                robot.lift.right_slide.setVelocity(0); // Stop if out of bounds
            }
        }




//        robot.lift.left_slide.setPower(gamepad2.left_stick_y);
//        robot.lift.right_slide.setPower(gamepad2.left_stick_y);




//       if (gamepad2.left_bumper){
//           left_servo.setPosition(0.8);
//       }


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
                    specimen_grabber.setPosition(servoToggled ? 0.93 : 0.5);  // Adjust position CHECK BOUNDS
                    taskStep++;  // Move to step 3
                    break;


                case 3:
                    
                    taskStep++;  // Move to step 4
                    break;


                case 4:
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
        if (gamepad2.dpad_up && !lbPressedLast) {
            specimen_grabber.setPosition(0.5);
            left_servo.setPosition(0.015);
            //claw_rot.setPosition(0.05);


            taskInProgress = false;  // Allow task to restart
            taskStep = 0;  // Reset task step
        }
        lbPressedLast = gamepad2.dpad_up;


        if (gamepad2.dpad_up && !lbPressedLast && !rightBumperTaskInProgress) {
            // Start the task sequence only if it isn't already running
            rightBumperTaskInProgress = true;
            rightBumperTaskStep = 1;
            rightBumperTimer.reset();
        }


// Update button state
        aPressedLast = gamepad2.a;




        if (gamepad2.x && !xPressedLast) {
            spin = !spin;
            intake_servo.setPower(spin ? -1 : 0);
        }
        xPressedLast = gamepad2.x;


        if (gamepad2.y && yPressedLast) {
            isSpecimen_grab_toggle = !isSpecimen_grab_toggle;
            specimen_grabber.setPosition(isSpecimen_grab_toggle ? 0.60 : 0.90);
        }
        yPressedLast = gamepad2.y;




        if (gamepad2.b && !bPressedLast) {
            lift_servo_toggled = !lift_servo_toggled;
            claw_rot.setPosition(lift_servo_toggled ? 0.3 : 1);
            claw_yaw.setPosition(0.2);
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


        lbPressedLast = gamepad2.dpad_up;




        if (gamepad2.right_bumper) {
            claw_yaw_pressed += 1;
            new_pos = 0.02 * claw_yaw_pressed;
            claw_yaw.setPosition(0.2 + new_pos);
        }


        if (gamepad2.left_bumper) {
            claw_yaw_pressed -= 1;
            new_pos = 0.02 * claw_yaw_pressed;
            claw_yaw.setPosition(0.2 + new_pos);
        }


        if (gamepad2.dpad_down) {
            int currentPosition = robot.linear_C.linear_claw.getCurrentPosition();
            int newPosition = currentPosition - 600; // Move down by 600 ticks


            // Ensure we don't exceed bounds
            if (newPosition >= robot.linear_C.OUTER_BOUND) {
                robot.linear_C.linear_claw.setTargetPosition(newPosition);
                robot.linear_C.linear_claw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.linear_C.linear_claw.setVelocity(1400);
            } else {
                // Optionally stop the motor if it's out of bounds
                robot.linear_C.linear_claw.setVelocity(0);
            }
        }


//        //Method to move motor to designated position
//        public void encoder ( int turnage, double power){
//            robot.linear_C.linear_claw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            target = ticks / turnage;
//            robot.linear_C.linear_claw.setTargetPosition((int) target);
//            robot.linear_C.linear_claw.setPower(power);
//            robot.linear_C.linear_claw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//
//        }
    }
}



