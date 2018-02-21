package org.usfirst.frc.team670.robot.commands.drive;

import java.util.HashMap;

import org.usfirst.frc.team670.robot.Robot;
import org.usfirst.frc.team670.robot.commands.LoggingCommand;
import org.usfirst.frc.team670.robot.constants.RoboConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * Uses a PID control loop plus the navX getDisplacement to move a given
 * distance in feet
 * 
 * @author vsharma8363AZ
 *
 */
public class Encoders_Drive extends LoggingCommand {

	private double ticksToTravel, minVelocity = 100, inches;
	private int numTimesMotorOutput;
	private boolean reachedMinSpeed, isWithinLimit;
	private double tolerance = 0.3;
	private double startYaw;
	private SensorCollection leftEncoder;
	private SensorCollection rightEncoder;
	private double currentYaw = 0;

	public Encoders_Drive(double inches) {

		this.inches = inches;
		this.ticksToTravel = ((inches) / (Math.PI * RoboConstants.DRIVEBASE_WHEEL_DIAMETER))
				* RoboConstants.DRIVEBASE_TICKS_PER_ROTATION;
		requires(Robot.driveBase);
		leftEncoder = Robot.driveBase.getLeft().getSensorCollection();
		rightEncoder = Robot.driveBase.getRight().getSensorCollection();
		reachedMinSpeed = false;

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.driveBase.initPID(Robot.driveBase.getLeft());
		Robot.driveBase.initPID(Robot.driveBase.getRight());
		if(inches < 36)
		{
			Robot.driveBase.getLeft().config_kF(RoboConstants.kPIDLoopIdx, RoboConstants.f, RoboConstants.kTimeoutMs);
			Robot.driveBase.getRight().config_kF(RoboConstants.kPIDLoopIdx, RoboConstants.f, RoboConstants.kTimeoutMs);
		}
		else
		{
			Robot.driveBase.getLeft().config_kF(RoboConstants.kPIDLoopIdx, 0, RoboConstants.kTimeoutMs);
			Robot.driveBase.getRight().config_kF(RoboConstants.kPIDLoopIdx, 0, RoboConstants.kTimeoutMs);
		}
		startYaw = Robot.sensors.getYaw();
		
		leftEncoder.setQuadraturePosition(0, 0);
		rightEncoder.setQuadraturePosition(0, 0);
		logInitialize(new HashMap<String, Object>() {{
		    put("TicksToTravel", ticksToTravel);
		    put("InchesToTravel", inches);
		    put("StartYaw", startYaw);
		    put("LeftEncoderTicks", leftEncoder.getQuadraturePosition());
		    put("RightEncoderTicks", rightEncoder.getQuadraturePosition());
		}});
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		System.out.println("Ticks to Travel: " + ticksToTravel + " | Left Ticks: " + Robot.driveBase.getLeft().getSensorCollection().getQuadraturePosition() + " | Right Ticks: " + Robot.driveBase.getRight().getSensorCollection().getQuadraturePosition());
		
		currentYaw = Robot.sensors.getYaw();
		double yawDiff = startYaw - currentYaw;
		System.out.println("yawDiff: " + yawDiff);

		if(Math.abs(yawDiff) > tolerance) {
			if(currentYaw > startYaw)
				Robot.driveBase.getRight().configClosedloopRamp(0.8, 0);
			else
				Robot.driveBase.getLeft().configClosedloopRamp(0.8, 0);
		}
		
		Robot.driveBase.getLeft().set(ControlMode.Position, -ticksToTravel);
		Robot.driveBase.getRight().set(ControlMode.Position, -ticksToTravel);
		if(!reachedMinSpeed)
			reachedMinSpeed = Math.abs(Robot.driveBase.getLeft().getSensorCollection().getQuadratureVelocity()) > minVelocity;
		isWithinLimit = Math.abs(leftEncoder.getQuadraturePosition()/ticksToTravel) >= 0.9;
		logExecute(new HashMap<String, Object>() {{
		    put("ReachedMinSpeed", reachedMinSpeed);
		    put("IsWithinLimit", isWithinLimit);
		    put("CurrentYaw", currentYaw);
		    put("LeftEncoderVelocity", leftEncoder.getQuadratureVelocity());
		    put("RightEncoderVelocity", leftEncoder.getQuadratureVelocity());
		    put("LeftEncoderTicks", leftEncoder.getQuadraturePosition());
		    put("RightEncoderTicks", rightEncoder.getQuadraturePosition());
		}});
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (Math.abs(Robot.driveBase.getRight().getSensorCollection().getQuadratureVelocity()) <= minVelocity
				&& Math.abs(
						Robot.driveBase.getRight().getSensorCollection().getQuadratureVelocity()) <= minVelocity
				&& reachedMinSpeed && isWithinLimit)
			numTimesMotorOutput++;
		
		SmartDashboard.putBoolean("IS Drive FINISHED:", (numTimesMotorOutput >= 2));
		SmartDashboard.putBoolean("IS WITHIN LIMIT", isWithinLimit);
		SmartDashboard.putNumber("LIMIT NUM:", Math.abs(leftEncoder.getQuadraturePosition()/ticksToTravel));

		return (numTimesMotorOutput >= 2);
	}
	

	// Called once after isFinished returns true
	protected void end() {
		System.out.println("PID Drive is finished");
		logFinished(new HashMap<String, Object>() {{
		    put("ReachedMinSpeed", reachedMinSpeed);
		    put("IsWithinLimit", isWithinLimit);
		    put("FinalYaw", Robot.sensors.getYaw());
		    put("LeftEncoderVelocity", leftEncoder.getQuadratureVelocity());
		    put("RightEncoderVelocity", leftEncoder.getQuadratureVelocity());
		    put("LeftEncoderTicks", leftEncoder.getQuadraturePosition());
		    put("RightEncoderTicks", rightEncoder.getQuadraturePosition());
		}});
		Robot.driveBase.getLeft().config_kF(RoboConstants.kPIDLoopIdx, 0, RoboConstants.kTimeoutMs);
		Robot.driveBase.getRight().config_kF(RoboConstants.kPIDLoopIdx, 0, RoboConstants.kTimeoutMs);
		Robot.driveBase.drive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}