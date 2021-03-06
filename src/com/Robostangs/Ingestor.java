package com.Robostangs;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * @author Laptop
 */
public class Ingestor {
    private static CANJaguar leftJag, rightJag;
    private static Ingestor instance = null;
    
    private Ingestor() {
        try {
            leftJag = new CANJaguar(Constants.INGESTOR_LEFT_JAG);
            rightJag = new CANJaguar(Constants.INGESTOR_RIGHT_JAG);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN ERROR AT INGESTOR");
        }
    }
    
    public static Ingestor getInstance() {
        if (instance == null) {
            instance = new Ingestor();
        }
        return instance;
    }
    
    public static void setSpeed(double speed) {
        try {
            rightJag.setX(-speed);
            leftJag.setX(speed);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN ERROR AT INGESTOR");
        }
    }

    public static void ingest() {
        setSpeed(Constants.INGESTOR_INGEST_SPEED);
    }
    
    public static void exgest() {
        setSpeed(Constants.INGESTOR_EXGEST_SPEED);
    }
    
    public static double getCurrent() {
	    try {
		    return (leftJag.getOutputCurrent() + rightJag.getOutputCurrent()) / 2;
	    } catch (CANTimeoutException ex) {
		    ex.printStackTrace();
	    }
	    return -1;
    }
    
    public static boolean hasBall() {
	    double averageCurrent = -1;
	    try {
                averageCurrent = (leftJag.getOutputCurrent() + rightJag.getOutputCurrent()) / 2;
	    } catch (CANTimeoutException ex) {
		System.out.println("JAG ERROR AT INGESTOR");
	    }
	    return averageCurrent > 6;
    }
    
    public static void stop() {
        setSpeed(0);
    }
}