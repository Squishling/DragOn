package co.uk.squishling.dragon.objects;

public class Gear {
	private float acceleration;
	private float topSpeed;
	private float gearRatio;
	
	public Gear(float acceleration, float topSpeed, float gearRatio) {
		this.acceleration = acceleration;
		this.topSpeed = topSpeed;
		this.gearRatio = gearRatio;
	}
	
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	
	public void setGearRatio(float gearRatio) {
		this.gearRatio = gearRatio;
	}
	
	public void setTopSpeed(float topSpeed) {
		this.topSpeed = topSpeed;
	}
	
	public float getAcceleration() {
		return acceleration;
	}
	
	public float getTopSpeed() {
		return topSpeed;
	}
	
	public float getGearRatio() {
		return gearRatio;
	}
}
