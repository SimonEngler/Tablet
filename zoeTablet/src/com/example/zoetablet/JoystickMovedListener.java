package com.example.zoetablet;


public interface JoystickMovedListener {
	public void OnMoved(int pan, int tilt);
	public void OnReleased();
	public void OnReturnedToCenter();
}
