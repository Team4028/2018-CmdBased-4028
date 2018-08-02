package org.usfirst.frc.team4028.robot.sensors;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.wpilibj.CameraServer;

public class SwitchableCameraServer
{
	// =================================================================================================================
	// Define Enums for the Camera
	public enum CAMERA_CHOICE {
		DRIVER,
		CARRIAGE
	}
	
	// Note: Sonix (bottom)
	private static final String USB1_NAME = "driver camera";
	private static final String USB1_DEVICE_PATH =  "/dev/v4l/by-path/platform-ci_hdrc.0-usb-0:1.1:1.0-video-index0";
	
	// Note: LifeCAM (top)
	private static final String USB2_NAME = "carriage camera";	
	private static final String USB2_DEVICE_PATH = "/dev/v4l/by-path/platform-ci_hdrc.0-usb-0:1.2:1.0-video-index0";
	
    private static final int CAMERA_TCP_PORT = 1180;
      
    private UsbCamera _camera1;
	private UsbCamera _camera2;
	private int _currentCameraId;
	
	private MjpegServer _rawVideoServer;
		
    // ==========================
    // Singleton Pattern
    // ==========================
    private static SwitchableCameraServer _instance = new SwitchableCameraServer();

	public static SwitchableCameraServer getInstance() 
	{
		return _instance;
	}

	// private constructor
	private SwitchableCameraServer() 
	{
		int width = 320; // 160; // 320; //640;
		int height = 240; //90; //180; //480;
		int frames_per_sec = 15; //10; //20; //15;
		
		_rawVideoServer = new MjpegServer("raw_video_server", CAMERA_TCP_PORT);    
		
		// =======================
		//  drivers camera
		// ======================= 
		if (Files.exists(Paths.get(USB1_DEVICE_PATH), LinkOption.NOFOLLOW_LINKS)) 
		{
			System.out.println ("...camera1 exists");
			_camera1 = new UsbCamera(USB1_NAME, USB1_DEVICE_PATH);
			_camera1.setVideoMode(VideoMode.PixelFormat.kMJPEG, width, height, frames_per_sec);
			_camera1.setExposureManual(5);
			_camera1.setWhiteBalanceManual(50);
		}
		
		// =======================
		//  carriage camera
		// ======================= 
		if (Files.exists(Paths.get(USB2_DEVICE_PATH), LinkOption.NOFOLLOW_LINKS)) 
		{
			System.out.println ("...camera2 exists");
			_camera2 = new UsbCamera(USB2_NAME, USB2_DEVICE_PATH);
			_camera2.setVideoMode(VideoMode.PixelFormat.kMJPEG, width, height, frames_per_sec);
			_camera2.setExposureManual(60);
			_camera2.setWhiteBalanceManual(50);
		}
		
		if(_camera1 != null)
		{
			_currentCameraId = 1;
			_rawVideoServer.setSource(_camera1);
		}
		else if(_camera2 != null)
		{
			_currentCameraId = 2;
			_rawVideoServer.setSource(_camera2);
		}
	}
	
	public void toggle()
	{
		if (_currentCameraId == 1)
		{
			setCamera(CAMERA_CHOICE.CARRIAGE);
		}	
		else if (_currentCameraId == 2)
		{
			setCamera(CAMERA_CHOICE.CARRIAGE);
		}
	}
	
	public void setCamera(CAMERA_CHOICE cameraChoice)
	{
		switch(cameraChoice)
		{
			case DRIVER:
				if (_currentCameraId == 1 && _camera2 != null)
				{
					_rawVideoServer.setSource(_camera2);
					_currentCameraId = 2;
					System.out.println ("current camera ==> " + _camera2.getName());
				}	
				break;
				
			case CARRIAGE:
				if (_currentCameraId == 2  && _camera1 != null)
				{
					_rawVideoServer.setSource(_camera1);
					_currentCameraId = 1;		
					System.out.println ("current camera ==> " + _camera1.getName());
				}
				break;
		}
	}
}
