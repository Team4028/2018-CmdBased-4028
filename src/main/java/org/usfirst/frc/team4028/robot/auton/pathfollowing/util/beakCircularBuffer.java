package org.usfirst.frc.team4028.robot.auton.pathfollowing.util;

import edu.wpi.first.wpilibj.CircularBuffer;

public class beakCircularBuffer extends CircularBuffer {

	int _size = 0;

	public beakCircularBuffer(int size) {
		super(size);
		_size = size;
	}

	public double[] toArray() {
		double[] arr = new double[_size];

		for (int i = 0; i < arr.length; i++) {
			arr[i] = get(i);
		}

		return arr;
	}

}