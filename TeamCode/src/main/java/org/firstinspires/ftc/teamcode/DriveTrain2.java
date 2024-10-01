package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveTrain2 {

    HardwareMap hardwareMap;

    public DcMotorEx right_b;
    public DcMotorEx left_f;
    public DcMotorEx right_f;
    public DcMotorEx left_b;


    public void init(HardwareMap hwMap) {
        hardwareMap = hwMap;
        left_f = hardwareMap.get(DcMotorEx.class, "left_front");
        right_f = hardwareMap.get(DcMotorEx.class, "right_front");
        left_b = hardwareMap.get(DcMotorEx.class, "left_back");
        right_b = hardwareMap.get(DcMotorEx.class, "right_back");

        right_f.setDirection(DcMotorSimple.Direction.REVERSE);
        right_b.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(Gamepad gamePad){

        double x = -gamePad.right_stick_x;
        double y = -gamePad.left_stick_y;
        double rx = -gamePad.left_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        right_f.setPower( (y+ x + rx) / denominator);
        left_b.setPower( (y -x + rx) / denominator);
        left_f.setPower( (y - x - rx) / denominator);
        right_b.setPower( (y + x - rx) / denominator);
    }


}




