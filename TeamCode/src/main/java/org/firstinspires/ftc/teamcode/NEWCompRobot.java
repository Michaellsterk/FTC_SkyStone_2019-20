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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="NEW Comp Robot", group="Linear Opmode")
//@Disabled
public class NEWCompRobot extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotor pumpMotor = null;
    private DcMotor stoneArmV = null;
    private CRServo stoneArmH = null;

    // declare motor speed variables
    double RF; double LF; double RR; double LR;
    // declare joystick position variables
    double X1; double Y1; double X2; double Y2;
    // operational constants
    double joyScale = 0.5;
    double motorMax = 0.6; // Limit motor power to this value for Andymark RUN_USING_ENCODER mode

    int targetEncoderArmV = 0;
    int nextEncoderArmV = 0;
    int minEncoderArmV = 0;
    int maxEncoderArmV = 1200;
    int encoderChangeArmV = 100;

    int minEncoderArmH = -300;
    int maxEncoderArmH = 300;
    double targetEncoderArmH = 0;

    public void wait(double secs) {
        ElapsedTime t = new ElapsedTime();
        t.reset();
        while (t.seconds() < secs && opModeIsActive()){}
    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFront  = hardwareMap.get(DcMotor.class, "fl_motor");
        leftBack  = hardwareMap.get(DcMotor.class, "bl_motor");
        rightFront = hardwareMap.get(DcMotor.class, "fr_motor");
        rightBack = hardwareMap.get(DcMotor.class, "br_motor");
        pumpMotor = hardwareMap.get(DcMotor.class, "pump_motor");
        stoneArmV = hardwareMap.get(DcMotor.class, "stoneV_motor");
        stoneArmH = hardwareMap.get(CRServo.class, "stoneH_servo");
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        pumpMotor.setDirection(DcMotor.Direction.REVERSE);
        stoneArmV.setDirection(DcMotor.Direction.FORWARD);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        stoneArmV.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if(gamepad1.right_trigger>0.3) {
                joyScale = 0.1;
            }
            else {
                joyScale = 0.5;
            }
            // Setup a variable for each drive wheel to save power level for telemetry

            // Reset speed variables
            LF = 0; RF = 0; LR = 0; RR = 0;

            // Get joystick values
                Y1 = -gamepad1.right_stick_y * joyScale; // invert so up is positive
                X1 = gamepad1.right_stick_x * joyScale;
                Y2 = -gamepad1.left_stick_y * joyScale; // Y2 is not used at present
                X2 = gamepad1.left_stick_x * joyScale;


            // Forward/back movement
            LF += Y1; RF += Y1; LR += Y1; RR += Y1;

            // Side to side movement
            LF += X1; RF -= X1; LR -= X1; RR += X1;

            // Rotation movement
            LF += X2; RF -= X2; LR += X2; RR -= X2;

            // Clip motor power values to +-motorMax
            LF = Math.max(-motorMax, Math.min(LF, motorMax));
            RF = Math.max(-motorMax, Math.min(RF, motorMax));
            LR = Math.max(-motorMax, Math.min(LR, motorMax));
            RR = Math.max(-motorMax, Math.min(RR, motorMax));

            // Send values to the motors
            leftFront.setPower(LF);
            rightFront.setPower(RF);
            leftBack.setPower(LR);
            rightBack.setPower(RR);

            if(gamepad1.left_trigger>0.3) {
                pumpMotor.setPower(1);
            }
            else {
                pumpMotor.setPower(0);
            }

            if(gamepad1.a) {
                stoneArmH.setPower(-180);
            }
            else if(gamepad1.b) {
                stoneArmH.setPower(180);
            }

            if (gamepad1.x) {
                if(stoneArmV.getCurrentPosition()>targetEncoderArmV-10&&stoneArmV.getCurrentPosition()<targetEncoderArmV+10) {
                    if (targetEncoderArmV >= minEncoderArmV + encoderChangeArmV) {
                        targetEncoderArmV -= encoderChangeArmV;
                        wait(0.2);
                    } else {
                        targetEncoderArmV = minEncoderArmV;
                        wait(0.2);
                    }
                }
            } else if (gamepad1.y) {
                if (targetEncoderArmV <= maxEncoderArmV - encoderChangeArmV) {
                    targetEncoderArmV += encoderChangeArmV;
                    wait(0.2);
                }
                else {
                    targetEncoderArmV = maxEncoderArmV;
                    wait(0.2);
                }
            }

            //Drop System

            if(stoneArmV.getCurrentPosition()<targetEncoderArmV+30) {
                stoneArmV.setPower(0.3);
            }
            else if (stoneArmV.getCurrentPosition()>targetEncoderArmV-30){
                stoneArmV.setPower(0);
            }
            else {
                stoneArmV.setPower(0.01);
            }

            //Ratchet System

            /*
            if(stoneArmV.getCurrentPosition()<targetEncoderArmV) {
                stoneArmV.setPower(0.3);
            }
            else if (stoneArmV.getCurrentPosition()>targetEncoderArmV+20){
                for(int i = 0; i < ((Math.abs(stoneArmV.getCurrentPosition())-targetEncoderArmV)/5); i++) {
                    nextEncoderArmV=stoneArmV.getCurrentPosition()-5;
                    while(stoneArmV.getCurrentPosition()>nextEncoderArmV) {
                        stoneArmV.setPower(0);
                    }
                    stoneArmV.setPower(0.05);
                    wait(0.005);
                }
                while(stoneArmV.getCurrentPosition()>targetEncoderArmV) {
                    stoneArmV.setPower(0);
                }
                stoneArmV.setPower(0.05);
                wait(0.005);
            }
            else {
                stoneArmV.setPower(0);
            }
             */


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Power Scaling: ", "(%.1f)", joyScale);
            telemetry.addData("Target Position", targetEncoderArmV);
            telemetry.addData("Current Position", stoneArmV.getCurrentPosition());
            telemetry.addData("Next Position", nextEncoderArmV);
            //telemetry.addData("Horizontal Position", stoneArmH.getPosition());
            telemetry.addData("Horizontal Target", targetEncoderArmH);
            //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
