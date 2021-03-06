package com.Robostangs;

import edu.wpi.first.wpilibj.Timer;

/**
 * @author Thunderbird
 */
public class Autonomous {
    public static Autonomous instance = null;
    private static Timer timer;
    private static boolean hot = true, done = false;
    
    private Autonomous() {
        timer = new Timer();
    }
    
    public static Autonomous getInstance() {
	if (instance == null) {
	    instance = new Autonomous();
	}
	return instance;
    }
    
    public static void oneBallAutonomous() {
        if (!done) {
            timer.reset();
            timer.start();
            DriveTrain.restartEncoders();
	    
	    Shooter.loadCompleted = true;
	    while (timer.get() < 2.0 && !DriveTrain.driveDistance(Constants.AUTON_DRIVE_DISTANCE)) {
	        DriveTrain.drive(Constants.AUTON_DRIVE_POWER, Constants.AUTON_DRIVE_POWER);
	    }
	    while (timer.get() < 4.0) {
	        Arm.setPIDCustomShot(Constants.AUTON_ONE_BALL_ANGLE);            
                Ingestor.setSpeed(Constants.INGESTOR_CONSTANT_INGEST_SPEED);
	        DriveTrain.stop();
	    }
	    while (timer.get() < 9.0) {
		if (RobotMain.vision.getLeftStatus() && Shooter.loadCompleted) {
                    Shooter.shoot();
		} else if (timer.get() > 8.0) {
                    Shooter.shoot();
                }
            }
	    Arm.stop();
	    timer.stop();
	    done = true;
        }
        Shooter.autoLoad();
    }
    
    public static void twoBallAutonomous() {
        if (!done) {
	    timer.reset();
            timer.start();
	    
	    Shifting.lowGear();
            Ingestor.setSpeed(Constants.INGESTOR_CONSTANT_INGEST_SPEED);
     	    Shooter.autoLoad();
	    DriveTrain.resetBooleans();
	    
	    while (timer.get() < 1.0 && !DriveTrain.driveDistance(Constants.AUTON_2B_DRIVE_FIRST_FORWARD_DISTANCE)) {
                Ingestor.setSpeed(Constants.INGESTOR_CONSTANT_INGEST_SPEED);
                Arm.setPIDCustomShot(Constants.AUTON_2B_FIRST_SHOT_ANGLE);

                //DriveTrain.driveStraightEncoder(Constants.AUTON_2B_DRIVE_POWER);
		DriveTrain.drive(Constants.AUTON_2B_DRIVE_POWER, Constants.AUTON_2B_DRIVE_POWER);
     	    }
	    while (timer.get() < 2.0 /*&& !Arm.isInPosition(Constants.AUTON_2B_FIRST_SHOT_ANGLE)*/) {
		DriveTrain.stop();
		Arm.setPIDCustomShot(Constants.AUTON_2B_FIRST_SHOT_ANGLE);
	    }
	    while (timer.get() < 2.35) {
		if (Shooter.loadCompleted) {
		    Shooter.shooShoot();
		}
	    }
            DriveTrain.resetBooleans();
	    while (timer.get() < 4.0 && DriveTrain.isTurning(Constants.AUTON_2B_TURN_ANGLE)) {
		if (!Arm.isInPosition(Constants.ARM_INGEST_ANGLE)) {
		    Arm.setPIDIngest();
		}
                Shooter.loadAutonomous();
		DriveTrain.turn(Constants.AUTON_2B_DRIVE_POWER, Constants.AUTON_2B_TURN_ANGLE);
		Ingestor.stop();
	    }
            DriveTrain.resetBooleans();
	    while (timer.get() < 5.0 && !DriveTrain.driveDistance(Constants.AUTON_2B_DRIVE_SECOND_FORWARD_DISTANCE)) {
		if (!Arm.isInPosition(Constants.ARM_INGEST_ANGLE)) {
		    Arm.setPIDIngest();
		}
		Shooter.stop();
		Ingestor.ingest();
                
		//DriveTrain.driveStraightEncoder(Constants.AUTON_2B_DRIVE_POWER);
		DriveTrain.drive(Constants.AUTON_2B_DRIVE_POWER, Constants.AUTON_2B_DRIVE_POWER);
	    }
            double initial = timer.get();
            while (timer.get() - initial < 0.5) {
                DriveTrain.stop();
                Ingestor.ingest();
            }
            DriveTrain.resetBooleans();
	    while (timer.get() < 6.5 && !DriveTrain.driveDistance(Constants.AUTON_2B_DRIVE_BACK_DISTANCE)) {
                Ingestor.ingest();
                
		//DriveTrain.driveStraightEncoder(-Constants.AUTON_2B_DRIVE_POWER);
		DriveTrain.drive(-Constants.AUTON_2B_DRIVE_POWER, -Constants.AUTON_2B_DRIVE_POWER);
	    }
            DriveTrain.resetBooleans();
	    while (timer.get() < 7.75 && DriveTrain.isTurning(Constants.AUTON_2B_TURN_BACK_ANGLE)) {
		DriveTrain.turn(Constants.AUTON_2B_DRIVE_POWER, Constants.AUTON_2B_TURN_BACK_ANGLE);
	    }
	    while (timer.get() < 9.0 && !Arm.isInPosition(Constants.ARM_SHOOT_ANGLE)) {
		DriveTrain.stop();
		Arm.setPIDShot();
                Shooter.autoLoad();
	    }
	    while (timer.get() < 10.0 && Ingestor.hasBall()) {
		Shooter.shooShoot();
		Arm.stop();
	    }
	    Arm.stop();
            Ingestor.stop();
	    timer.stop();
	    done = true;
        }
	Shifting.highGear();
        //Shooter.autoLoad();        
    }
    
    //PROTOTYPE - DON'T USE THIS
    public static void threeBallAutonomous() {
        if (!done) {
	    timer.reset();
            timer.start();
	    
	    Shifting.lowGear();
            Ingestor.setSpeed(Constants.INGESTOR_CONSTANT_INGEST_SPEED);
     	    Shooter.autoLoad();
	    DriveTrain.resetBooleans();
	    
            while (timer.get() < 1.0 && !Arm.isInPosition(520)) {
                Arm.setPIDCustomShot(520);
            }
            while (timer.get() < 1.35) {
                if (Shooter.loadCompleted) {
                    Shooter.shooShoot();
                }
            }
            while (timer.get() < 2.25 && !Arm.isInPosition(Constants.ARM_INGEST_ANGLE)) {
                Arm.setPIDIngest();
                Shooter.autoLoad();
            }
	    while (timer.get() < 3.25 && !DriveTrain.driveDistance(4000)) {
                Ingestor.setSpeed(Constants.INGESTOR_INGEST_SPEED);
                Arm.setPIDIngest();
                //DriveTrain.driveStraightEncoder(Constants.AUTON_2B_DRIVE_POWER);
		DriveTrain.drive(1.0, 1.0);
     	    }
	    while (timer.get() < 4.5 /*&& !Arm.isInPosition(Constants.AUTON_2B_FIRST_SHOT_ANGLE)*/) {
		DriveTrain.stop();
		Arm.setPIDCustomShot(Constants.AUTON_2B_FIRST_SHOT_ANGLE);
	    }
	    while (timer.get() < 4.85) {
		if (Shooter.loadCompleted) {
		    Shooter.shooShoot();
		}
	    }
            DriveTrain.resetBooleans();
	    while (timer.get() < 6.0 && DriveTrain.isTurning(Constants.AUTON_2B_TURN_ANGLE)) {
		if (!Arm.isInPosition(Constants.ARM_INGEST_ANGLE)) {
		    Arm.setPIDIngest();
		}
                Shooter.autoLoad();
		DriveTrain.turn(Constants.AUTON_2B_DRIVE_POWER, Constants.AUTON_2B_TURN_ANGLE);
		Ingestor.stop();
	    }
            DriveTrain.resetBooleans();
	    while (timer.get() < 7.25 && !DriveTrain.driveDistance(Constants.AUTON_2B_DRIVE_SECOND_FORWARD_DISTANCE)) {
		if (!Arm.isInPosition(Constants.ARM_INGEST_ANGLE)) {
		    Arm.setPIDIngest();
		}
		Shooter.autoLoad();
		Ingestor.ingest();
                
		//DriveTrain.driveStraightEncoder(Constants.AUTON_2B_DRIVE_POWER);
		DriveTrain.drive(Constants.AUTON_2B_DRIVE_POWER, Constants.AUTON_2B_DRIVE_POWER);
	    }
	    DriveTrain.resetBooleans();
	    while (timer.get() < 8.5 && DriveTrain.isTurning(Constants.AUTON_2B_TURN_BACK_ANGLE)) {
                Ingestor.ingest();
		DriveTrain.turn(Constants.AUTON_2B_DRIVE_POWER, Constants.AUTON_2B_TURN_BACK_ANGLE);
                while (timer.get() > 7.75) {
                    Arm.setPIDCustomShot(510);
                }
	    }
            while (timer.get() < 9.0 /*&& !Arm.isInPosition(510)*/) {
                DriveTrain.stop();
                Arm.setPIDCustomShot(510);
	    }
	    while (timer.get() < 9.75 && Ingestor.hasBall()) {
		Shooter.shooShoot();
		Arm.stop();
	    }
	    Arm.stop();
            Ingestor.stop();
	    //Shooter.shooShoot();
	    timer.stop();
	    done = true;
        }
	Shifting.highGear();
        Shooter.autoLoad(); 
    }
    
    public static void reset() {
	DriveTrain.resetBooleans();
        done = false;
    }
    
    public static void setHot(boolean isHot) {
        hot = isHot;
    }
}