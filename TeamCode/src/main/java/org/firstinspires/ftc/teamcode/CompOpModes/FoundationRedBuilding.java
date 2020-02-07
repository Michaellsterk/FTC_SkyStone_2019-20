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

package org.firstinspires.ftc.teamcode.CompOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

@Autonomous(name="Foundation Red Building", group="Linear Opmode")
//@Disabled
public class FoundationRedBuilding extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private Servo foundGrabber = null;

    public void wait(double secs) {
        ElapsedTime t = new ElapsedTime();
        t.reset();
        while (t.seconds() < secs && opModeIsActive()){}
    }

    public void straight(double power, double secs) {
        ElapsedTime t = new ElapsedTime();
        t.reset();
        while (t.seconds() < secs && opModeIsActive()){
            leftFront.setPower(power);
            leftBack.setPower(power);
            rightFront.setPower(power);
            rightBack.setPower(power);
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void turn(double powerLeft, double powerRight, double secs) {
        ElapsedTime t = new ElapsedTime();
        t.reset();
        while (t.seconds() < secs && opModeIsActive()){
            leftFront.setPower(powerLeft);
            leftBack.setPower(powerLeft);
            rightFront.setPower(powerRight);
            rightBack.setPower(powerRight);
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void strafe(double power, double secs) {
        ElapsedTime t = new ElapsedTime();
        t.reset();
        while(t.seconds() < secs && opModeIsActive()) {
            leftFront.setPower(power);
            leftBack.setPower(power*-1);
            rightFront.setPower(power*-1);
            rightBack.setPower(power);
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
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

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Boolean autoEnd = false;
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive() && !autoEnd) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftFPower = leftFront.getPower();
            double rightFPower = rightFront.getPower();
            double leftBPower = leftBack.getPower();
            double rightBPower = rightBack.getPower();

            strafe(-0.5,2.6);

            straight(0.5,0.5);

            straight(-0.2,0.125);

            foundGrabber.setPosition(0.7);

            wait(0.5);

            straight(-0.5,0.3);

            turn(0.5,-0.5,1.8);

            straight(0.5,1.5);

            foundGrabber.setPosition(-0.7);

            strafe(0.5,1);

            straight(0.5,0.4);

            strafe(-0.5, 1.8);

            //straight(-0.2, 0.7);

            strafe(0.5, 2.3);

            autoEnd = true;

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left Front (%.2f), left Back (%.2f), right Front (%.2f), right Back (%.2f)", leftFPower, leftBPower, rightFPower, rightBPower);
            telemetry.update();
        }
    }
}
//snack bot