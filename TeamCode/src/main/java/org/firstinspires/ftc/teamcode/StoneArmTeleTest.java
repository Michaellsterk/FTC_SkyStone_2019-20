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

@TeleOp(name="Tele StoneArm Test", group="Linear Opmode")
//@Disabled
public class StoneArmTeleTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    /*
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private Servo foundGrabber = null;
    private Servo capstoneArm = null;
    private Servo stoneGrabber = null;
    private boolean controller2Toggle = false;
    private String hasControl = "";

    private Servo stoneArmH = null;
     */
    private DcMotor stoneArmV = null;
    /*
    private DcMotor stoneValve = null;

    private Servo stoneValveRelease = null;

    private CRServo capRelease = null;
    private CRServo stoneRotate = null;

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

    double stoneArmHPower = 0;
     */
    double stoneArmVPower = 0;
    /*
    double stoneValvePower = 1;
    double capReleasePower = 0;
    double stoneRotatePower = 0;
    */
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
        /*
        leftFront  = hardwareMap.get(DcMotor.class, "fl_motor");
        leftBack  = hardwareMap.get(DcMotor.class, "bl_motor");
        rightFront = hardwareMap.get(DcMotor.class, "fr_motor");
        rightBack = hardwareMap.get(DcMotor.class, "br_motor");
        foundGrabber = hardwareMap.get(Servo.class, "found_servo");
        capstoneArm = hardwareMap.get(Servo.class, "capStone_servo");
        stoneGrabber = hardwareMap.get(Servo.class, "stone_servo");

        stoneArmH = hardwareMap.get(Servo.class, "stoneH_CRservo");

         */
        stoneArmV = hardwareMap.get(DcMotor.class, "stoneV_motor");

        stoneArmV.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        /*
        stoneValve = hardwareMap.get(DcMotor.class, "stoneValve_motor");
        stoneValveRelease = hardwareMap.get(Servo.class, "stoneValve_servo");

        capRelease = hardwareMap.get(CRServo.class, "capRelease_CRservo");
        stoneRotate = hardwareMap.get(CRServo.class, "stoneRotate_CRservo");

         */

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            /*
            if(gamepad1.a) {
                stoneArmH.setPosition(stoneArmHPower*-1);
            }
            else if (gamepad1.b) {
                stoneArmH.setPosition(stoneArmHPower);
            }
            else {
                stoneArmH.setPosition(0);
            }

             */

            if(gamepad1.x) {
                stoneArmV.setPower(stoneArmVPower*-1);
            }
            else if (gamepad1.y) {
                stoneArmV.setPower(stoneArmVPower);
            }
            else {
                stoneArmV.setPower(0);
            }

            if(gamepad1.dpad_down) {
                stoneArmVPower-=0.05;
                wait(0.2);
            }
            else if (gamepad1.dpad_up) {
                stoneArmVPower+=0.05;
                wait(0.2);
            }
            /*

            if(gamepad1.dpad_left) {
                stoneArmHPower-=0.05;
                wait(0.2);
            }
            else if (gamepad1.dpad_right) {
                stoneArmHPower+=0.05;
                wait(0.2);
            }

            if(gamepad2.dpad_down) {
                stoneRotatePower-=0.05;
                wait(0.2);
            }
            else if (gamepad2.dpad_up) {
                stoneRotatePower+=0.05;
                wait(0.2);
            }

             */

            if(stoneArmVPower<0) {
                stoneArmVPower=0;
            }
            else if (stoneArmVPower>1) {
                stoneArmVPower=1;
            }
            /*

            if(stoneArmHPower<0) {
                stoneArmHPower=0;
            }
            else if (stoneArmHPower>1) {
                stoneArmHPower=1;
            }

            if(stoneValvePower<0) {
                stoneValvePower=0;
            }
            else if (stoneValvePower>1) {
                stoneValvePower=1;
            }

            if(gamepad1.left_trigger>0.6) {
                stoneValve.setPower(stoneValvePower);
            }
            else {
                stoneValve.setPower(0);
            }

             */

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            telemetry.addData("Vertical Power:", stoneArmVPower);
            telemetry.addData("Vertical Encoder:", stoneArmV.getCurrentPosition());
            /*
            telemetry.addData("Horizontal Power:", stoneArmHPower);
            telemetry.addData("Valve Power", stoneValvePower);

            telemetry.addData("Horizontal Position:", stoneArmH.getPosition());
            telemetry.addData("Valve Power", stoneValvePower);
            
             */
            //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
