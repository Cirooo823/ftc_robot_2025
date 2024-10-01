package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="TeleOp Program", group="TeleOp")
public class MecanumTeleOp extends OpMode {

    Robot robot = new Robot();
    double ticks = 1425;
    double LM_Ticks; //Find ticks of linear motion motor
    double target;






    //Code to run ONCE after the driver hits INIT
    @Override
    public void init() {

        robot.init(hardwareMap);
    }
    //Code to run REPEATEDLY after the driver hits INIT
    @Override
    public void init_loop() {

        telemetry.addData("Linear_motion_right", gamepad2.left_stick_x);
        telemetry.update();


    }

    //Code to run ONCE after the driver hits PLAY
    @Override
    public void start() {

    }

    //Code to run REPEATEDLY after the driver hits PLAY
    @Override
    public void loop() {

        robot.driveTrain.drive(gamepad1);


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

            if (gamepad1.dpad_right){
                encoder(20,1);
            } else if (gamepad1.dpad_left){
                encoder(-20, 1);

                //-------------------------------------------------

        } /*else if (gamepad1.a){
                robot.left_claw.servo_left.setPosition(1);
        } else if (gamepad1.b){
                robot.left_claw.servo_left.setPosition(-1);
        } */


        if (gamepad2.a){

        }

        else {
            //robot.linear_L.linear_motion_left.setPower(0.005);

            //robot.linear_R.linear_motion_right.setPower(0.005);
            robot.linear_C.linear_claw.setPower(0);


        }

    }

    //Method to move motor to designated position
    public void encoder(int turnage, double power){
        target = ticks/((double)turnage/100);
        robot.linear_C.linear_claw.setTargetPosition((int) target);
        robot.linear_C.linear_claw.setPower(power);
        robot.linear_C.linear_claw.setMode(DcMotor.RunMode.RUN_TO_POSITION);


    }




}



