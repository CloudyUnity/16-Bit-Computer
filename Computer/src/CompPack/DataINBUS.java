package CompPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataINBUS implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public List<Integer> inputs = new ArrayList<Integer>();
	public int output;
}
