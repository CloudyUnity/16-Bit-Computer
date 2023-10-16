package CompPack;

import java.awt.Color;

public class ColorManager {

	public static final String WHITE = "F6ECF0";
	public static final String BLACK = "353B3C";
	
	public static final String LIGHT_BLUE = "A7C5EB";
	public static final String DARK_BLUE = "949CDF";
	public static final String DARKER_BLUE = "424874";
	public static final String SELECTED_BLUE = "4d5487";
	
	public static final String RED = "CD5D7D";
	public static final String ORANGE = "B66D0D";
	public static final String GREEN = "949D6A";
	public static final String PURPLE = "540D6E";
	
	public static Color parseColor(String hex) {
		
		int red = Integer.valueOf(hex.substring(0, 2), 16);
	    int green = Integer.valueOf(hex.substring(2, 4), 16);
	    int blue = Integer.valueOf(hex.substring(4, 6), 16);
	    return new Color(red, green, blue);
	}
}
