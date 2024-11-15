package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {

    public DcMotorEx left_slide;
    public DcMotorEx right_slide;

    public final double TICKS_PER_REV = 537.7;
    public final double MAX_VEL = 312/60 * TICKS_PER_REV;

    public final int MAX_BOUNDS = 2200;
    public final int STARTING_BOUNDS = 0;

    HardwareMap hardwareMap;

    public void init(HardwareMap ahwMap) {
        hardwareMap = ahwMap;
        left_slide = hardwareMap.get(DcMotorEx.class, "linear_motion_left");
        right_slide = hardwareMap.get(DcMotorEx.class, "linear_motion_right");

        left_slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left_slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        left_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        right_slide.setDirection(DcMotorEx.Direction.REVERSE);
        //left_slide.setDirection(DcMotorEx.Direction.REVERSE);



    }


    public void doTelemetry(Telemetry telemetry){
        telemetry.addData("Right Slide Pos", right_slide.getCurrentPosition());
        telemetry.addData("Left Slide Pos", left_slide.getCurrentPosition());

        telemetry.addLine();

        telemetry.addData("Right Slide Vel", right_slide.getVelocity());
        telemetry.addData("Left Slide Vel", left_slide.getVelocity());
    }



    public void setVelocities(double velocity) {
        // Set initial velocities, and check bounds before setting velocity
        if (left_slide.getCurrentPosition() < MAX_BOUNDS) {
            left_slide.setVelocity(velocity);
        } else {
            left_slide.setVelocity(0); // Stop if it reaches max bounds
        }

        if (right_slide.getCurrentPosition() < MAX_BOUNDS) {
            right_slide.setVelocity(velocity);
        } else {
            right_slide.setVelocity(0); // Stop if it reaches max bounds
        }
    }

    // Method to move slides up with boundary checks
    public void moveUp(double velocity) {
        setVelocities(velocity);
    }

    // Method to move slides down
    public void moveDown(double velocity) {
        // Ensure slide doesn't go below starting bounds
        if (left_slide.getCurrentPosition() > STARTING_BOUNDS) {
            left_slide.setVelocity(-velocity);
        } else {
            left_slide.setVelocity(0);
        }

        if (right_slide.getCurrentPosition() > STARTING_BOUNDS) {
            right_slide.setVelocity(-velocity);
        } else {
            right_slide.setVelocity(0);
        }



    }
}
