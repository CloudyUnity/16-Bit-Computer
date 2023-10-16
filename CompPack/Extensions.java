package CompPack;

public class Extensions {

	public static double clamp(double x, double min, double max) {
		return Math.max(min, Math.min(max, x));
	}
	
	public static float lerp(float a, float b, float f) 
	{
	    return (a * (1 - f)) + (b * f);
	}
}
