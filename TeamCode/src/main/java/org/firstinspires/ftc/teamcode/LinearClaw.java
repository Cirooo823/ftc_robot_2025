package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LinearClaw {

    public DcMotorEx linear_claw;
    HardwareMap hardwareMap;


    public final double TICKS_PER_REV = 537.7;
    public final double MAX_VEL = 312/60 * TICKS_PER_REV;

    public final int OUTER_BOUND = -2100;
    public final int INNER_BOUND = 0;

    public void init(HardwareMap ahwMap) {
        hardwareMap = ahwMap;
        linear_claw = hardwareMap.get(DcMotorEx.class, "linear_motion_claw");
        linear_claw.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        linear_claw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linear_claw.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linear_claw.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    public void loop(){

    }

    public void linear_claw_Telemetry(Telemetry telemetry){
        telemetry.addData("Linear Claw Vel", linear_claw.getVelocity());
        telemetry.addData("Linear Claw Pos", linear_claw.getCurrentPosition());
    }

}
