package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {


    HardwareMap hardwareMap;
    //MecanumDriveTrain driveTrain = new MecanumDriveTrain();
    Lift lift = new Lift();
    LinearClaw linear_C = new LinearClaw();
    //ClawLeft left_claw = new ClawLeft();

    //DriveTrain2 dT = new DriveTrain2();




    public void init(HardwareMap ahwMap) {
        hardwareMap = ahwMap;
        //driveTrain.init(hardwareMap);
        lift.init(hardwareMap);
        linear_C.init(hardwareMap);
        //dT.init(hardwareMap);

        //left_claw.init(hardwareMap);





    }

}
