package com.aos.tray.agent;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class LedControl {
	private GpioController gpio;
	private GpioPinDigitalOutput firstPin;
	private GpioPinDigitalOutput secondPin;
	
	public LedControl() {
		this.gpio = GpioFactory.getInstance();
		this.firstPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18);
		this.secondPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24);
	}
	
	public void ledOn() {
		firstPin.high();
		secondPin.high();
		
		gpio.unprovisionPin(firstPin);
		gpio.unprovisionPin(secondPin);
	}
	
	public void ledOff() {
		firstPin.low();
		secondPin.low();
		
		gpio.unprovisionPin(firstPin);
		gpio.unprovisionPin(secondPin);
	}
}
