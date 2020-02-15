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

@TeleOp(name="Tele Encode Test", group="Linear Opmode")
//@Disabled
public class EncoderTeleTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private Servo foundGrabber = null;
    private Servo capstoneArm = null;
    private Servo stoneGrabber = null;
    private boolean controller2Toggle = false;
    private String hasControl = "";

    // declare motor speed variables
    double RF; double LF; double RR; double LR;
    // declare joystick position variables
    double X1; double Y1; double X2; double Y2;
    // operational constants
    double joyScale = 0.5;
    double motorMax = 0.6; // Limit motor power to this value for Andymark RUN_USING_ENCODER mode

    private int minEncoder = 0;
    private int maxEncoder = 360;
    private int moveEncoder = 0;

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
        foundGrabber = hardwareMap.get(Servo.class, "found_servo");
        capstoneArm = hardwareMap.get(Servo.class, "capStone_servo");
        stoneGrabber = hardwareMap.get(Servo.class, "stone_servo");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        ElapsedTime sinceEncoderChange = new ElapsedTime();
        sinceEncoderChange.reset();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry

            // Reset speed variables
            LF = 0; RF = 0; LR = 0; RR = 0;

            // Get joystick values
            // Is toggleable between drivers
            if(!controller2Toggle) {
                Y1 = -gamepad1.right_stick_y * joyScale; // invert so up is positive
                X1 = gamepad1.right_stick_x * joyScale;
                Y2 = -gamepad1.left_stick_y * joyScale; // Y2 is not used at present
                X2 = gamepad1.left_stick_x * joyScale;
            }
            else {
                Y1 = -gamepad2.right_stick_y * joyScale; // invert so up is positive
                X1 = gamepad2.right_stick_x * joyScale;
                Y2 = -gamepad2.left_stick_y * joyScale; // Y2 is not used at present
                X2 = gamepad2.left_stick_x * joyScale;
            }

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
            //leftFront.setPower(LF);
            rightFront.setPower(RF);
            leftBack.setPower(LR);
            rightBack.setPower(RR);

            leftFront.setTargetPosition(moveEncoder);

            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if(leftFront.getCurrentPosition()>moveEncoder) {
                leftFront.setPower(-0.5);
            }
            else {
                leftFront.setPower(0.5);
            }

            //Lowers and raises the foundation grabber
            if(gamepad1.a||gamepad2.a) {
                foundGrabber.setPosition(0.7);
            }
            else if(gamepad1.b||gamepad2.b) {
                foundGrabber.setPosition(-0.7);
            }

            //Flips out and flips back the arm holding the capstone
            if(gamepad1.x||gamepad2.x) {
                capstoneArm.setPosition(1);
            }
            else if(gamepad1.y||gamepad2.y) {
                capstoneArm.setPosition(-0.4);
            }

            if(gamepad1.dpad_down||gamepad2.dpad_down) {
                stoneGrabber.setPosition(0);
            }
            else if(gamepad1.dpad_up||gamepad2.dpad_up){
                stoneGrabber.setPosition(1);
            }

            if (gamepad1.dpad_left || gamepad2.dpad_left) {
                if (moveEncoder >= minEncoder + 60) {
                    moveEncoder -= 60;
                    wait(0.2);
                }
                else {
                    moveEncoder = minEncoder;
                    wait(0.2);
                }
            } else if (gamepad1.dpad_right || gamepad2.dpad_right) {
                if (moveEncoder <= maxEncoder - 60) {
                    moveEncoder += 60;
                    wait(0.2);
                }
                else {
                    moveEncoder = maxEncoder;
                    wait(0.2);
                }
            }

            //Holding the right trigger slows down the robot to 50% speed for added precision
            if (!controller2Toggle) {
                if (gamepad1.right_trigger >= 0.5) {
                    joyScale = 0.25;
                } else {
                    joyScale = 0.5;
                }
            }
            else {
                if (gamepad2.right_trigger >= 0.5) {
                    joyScale = 0.25;
                } else {
                    joyScale = 0.5;
                }
            }

            /*If either controller presses both bumpers the drive controls swap controller
            this will be our fail safe in case the primary driver's controller stops working
             */
            if((gamepad1.right_bumper&&gamepad1.left_bumper)||(gamepad2.right_bumper&&gamepad2.left_bumper)) {
                if (!controller2Toggle) {
                    controller2Toggle = true;
                }
                else {
                    controller2Toggle = false;
                }
            }

            if(controller2Toggle) {
                hasControl = "Secondary";
            }
            else {
                hasControl = "Primary";
            }


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Power Scaling: ", "(%.1f)", joyScale);
            telemetry.addData("Driver Controlling: ", hasControl);
            telemetry.addData("Go to encoder value ", moveEncoder);
            telemetry.addData("I'm at encoder value ", leftFront.getCurrentPosition());
            //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
