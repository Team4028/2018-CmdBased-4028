package org.usfirst.frc.team4028.robot.sensors;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import org.usfirst.frc.team4028.robot.RobotMap;
import org.usfirst.frc.team4028.robot.util.NetworkUtilities;

import edu.wpi.first.wpilibj.DriverStation;

// This class encapsulates interactions with the Vision Server (onboard computer exposing tcp socket server)
public class VisionClient 
{
      // default read and write socket timeout
    private final static int DEFAULT_TIMEOUT = 50;
    private static final int POLLING_CYCLE_IN_MSEC = 50; // ie: 20x / sec

    // out main socket handle
    Socket _handle;
    boolean _isConnected = false;
    long _lastWarningWriteTimeMSec = 0;

    // socket based reader and writer objects
    BufferedInputStream  _bufferedReader;
    BufferedOutputStream _bufferedWriter;

    //=====================================================================================
	// Define Singleton Pattern
	//=====================================================================================
	private static VisionClient _instance = new VisionClient();
	
    public static VisionClient getInstance() 
    {
		return _instance;
	}
	
	// private constructor for singleton pattern
	private VisionClient() 
    {
        Thread visionThread = new Thread(RoboRealmUpdater);
        visionThread.start();
    }

    //=========================================================================
	//	Task Executed By Timer
    //=========================================================================	
    
    /* Initiates a socket connection to the vision socket server */
    public void connect(String hostname, int portNumber) 
    {
        _isConnected = false;

        try 
        {
            boolean isPingable = NetworkUtilities.RobustPortTest(RobotMap.VISION_SOCKET_SERVER_IPV4_ADDR, 
                                                                    RobotMap.VISION_SOCKET_SERVER_PORT);

            if(isPingable)
            {
                _handle = new Socket(hostname, portNumber);

                _handle.setSoTimeout(DEFAULT_TIMEOUT);

                _bufferedReader = new BufferedInputStream(_handle.getInputStream());
                _bufferedWriter = new BufferedOutputStream(_handle.getOutputStream());

                _isConnected = true;
            }
            else
            {
                if((_lastWarningWriteTimeMSec == 0)
                    || (new Date().getTime() - _lastWarningWriteTimeMSec) > 100) 
                {
                    DriverStation.reportError("Failed To Receive Game Data", false);

                    _lastWarningWriteTimeMSec = new Date().getTime();
                }
            }
        } 
        catch (IOException e2) 
        {
            // Unable to open connection to vision socket server
        }
    }

	// poll Vision Server to read current values
    public void updateVisionData() 
    { 
    }

    //=========================================================================
	//	Thread
	//=========================================================================	
    private Runnable RoboRealmUpdater = new Runnable() 
    {
		@Override
        public void run() 
        {    
            // =============================================================================
            // start looping
            // =============================================================================
            while(!Thread.interrupted()) 
            {       
                if (!_isConnected)
                {
                    connect(RobotMap.VISION_SOCKET_SERVER_IPV4_ADDR, RobotMap.VISION_SOCKET_SERVER_PORT);

                    //if()
                }
                else
                {	
            	    updateVisionData();
                }

                // sleep each cycle to avoid Robot Code not updating often enough issues
        		try {
					Thread.sleep(POLLING_CYCLE_IN_MSEC);
                } 
                catch (InterruptedException e) 
                {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
	            	
		}
	};
}