package paths.center;

import org.usfirst.frc.team670.robot.Robot;
import org.usfirst.frc.team670.robot.commands.auto_specific.DropCube;
import org.usfirst.frc.team670.robot.commands.auto_specific.PickupCube;
import org.usfirst.frc.team670.robot.commands.auto_specific.ShootCube;
import org.usfirst.frc.team670.robot.commands.drive.Drive;
import org.usfirst.frc.team670.robot.commands.drive.Encoders_Drive;
import org.usfirst.frc.team670.robot.commands.drive.IR_Drive;
import org.usfirst.frc.team670.robot.commands.drive.NavX_Pivot;
import org.usfirst.frc.team670.robot.commands.drive.Pivot;
import org.usfirst.frc.team670.robot.commands.drive.Time_Drive;
import org.usfirst.frc.team670.robot.commands.elevator.Encoders_Elevator;
import org.usfirst.frc.team670.robot.commands.intake.Deploy;
import org.usfirst.frc.team670.robot.commands.intake.SpinIntake;
import org.usfirst.frc.team670.robot.constants.Field;
import org.usfirst.frc.team670.robot.constants.RoboConstants;
import org.usfirst.frc.team670.robot.constants.enums.ElevatorState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class center_left_switch_straight extends CommandGroup {

    public center_left_switch_straight() {
    	addSequential(new Drive(Robot.length+6));
    	addSequential(new Pivot(-90));
    	addSequential(new Drive(Field.SWITCH_LENGTH - 35.8));
    	addSequential(new Pivot(90));
    	addParallel(new Encoders_Elevator(ElevatorState.SWITCH));
    	addSequential(new Drive(Field.DS_TO_BASELINE - Robot.length - 6));
//    	addSequential(new Time_Drive(1.5, 0.45));
    	addSequential(new DropCube());
    	//Pick up cube
//    	addSequential(new Time_Drive(0.15, -0.3));
//		addParallel(new Encoders_Elevator(ElevatorState.EXCHANGE));
//		addSequential(new NavX_Pivot(90));
//		addParallel(new SpinIntake(-0.5, 7));
//		addSequential(new IR_Drive());
//		addSequential(new PickupCube());
		//Place Cube
//		addParallel(new Encoders_Elevator(ElevatorState.SWITCH));
//		addSequential(new Encoders_Drive(-Field.SWITCH_LENGTH/2));
//		addSequential(new NavX_Pivot(-90));
//		addSequential(new Time_Drive(0.15, 0.7));
//		addSequential(new ShootCube(0.5));
    }
}
