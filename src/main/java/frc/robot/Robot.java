package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DriveCommand;
import frc.robot.controllers.Controller;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.utils.I2CCOM;


import frc.robot.utils.Config;
import edu.wpi.first.wpilibj.CameraServer;


public class Robot extends TimedRobot {
  public static DriveTrainSubsystem m_drivetrainsubsystem = new DriveTrainSubsystem();
  public static OI m_oi;

  I2CCOM arduinoI2C;


  Command driveCommand = new DriveCommand();
  Command m_autonomousCommand;

  SendableChooser<Command> m_chooser = new SendableChooser<>();

  @Override
  public void robotInit() {
    m_oi = new OI();
    m_chooser.setDefaultOption("Default Auto", new DriveCommand());
    SmartDashboard.putData("Auto mode", m_chooser);
    arduinoI2C = new I2CCOM(1);

    
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }

  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
    
  }

  @Override
  public void teleopInit() {
    CameraServer.getInstance().startAutomaticCapture();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    Controller controller = (Controller) Config.get("controls.main");
    boolean xmassonoff = (controller.getButton(8));
    boolean xmassmode = (controller.getButton(7));

    Scheduler.getInstance().run();
    driveCommand.start();

    if (xmassonoff){
      arduinoI2C.sendData(1, 1);
    } if (xmassmode){
      arduinoI2C.sendData(1, 0);
    }
    }

    
  

  @Override
  public void testPeriodic() {
  }
}
