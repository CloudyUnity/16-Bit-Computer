package CompPack;

public class Extensions {

	public static double Clamp(double x, double min, double max) {
		return Math.max(min, Math.min(max, x));
	}
}
