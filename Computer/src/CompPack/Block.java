package CompPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String name;
	public int nodes;
	
	public List<Integer> inputs = new ArrayList<Integer>();
	public List<Integer> outputs = new ArrayList<Integer>();
	public List<Vector2> connections = new ArrayList<Vector2>();
	
	public List<AND> andList = new ArrayList<AND>();
	public List<NOT> notList = new ArrayList<NOT>();
}