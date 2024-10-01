package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {


    HardwareMap hardwareMap;
    //MecanumDriveTrain driveTrain = new MecanumDriveTrain();
    LinearVerticalLeft linear_L = new LinearVerticalLeft();
    LinearVerticalRight linear_R = new LinearVerticalRight();
    LinearClaw linear_C = new LinearClaw();
    //ClawLeft left_claw = new ClawLeft();

    DriveTrain2 driveTrain = new DriveTrain2();




    public void init(HardwareMap ahwMap) {
        hardwareMap = ahwMap;
        driveTrain.init(hardwareMap);
        linear_L.init(hardwareMap);
        linear_R.init(hardwareMap);
        linear_C.init(hardwareMap);

        //left_claw.init(hardwareMap);





    }

}
