package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class NodeDragger extends Shape{

	public Node conNode;
	
	public NodeDragger(Node input) {
		super(input.position, input.scale, Color.cyan);
		this.conNode = input;
		
		draggable = true;
		interactible = true;
		visible = false;
	}
	
	@Override 
	protected void onMousePressed(MouseEvent e) {
		if (conNode.draggable && e.getClickCount() == 2) {
			Main.mouse.setSelected(conNode);
		}
	}
	
	@Override
	protected void onMouseRelease(double dur) {
		
		if (dur < 200) {
			conNode.onClick();
			position = conNode.position;
			return;
		}			
		
		Node node = Main.node.FindClosestNodeTo(position, conNode);
		position = conNode.position;
		if (node == null) {
			for (Node o : conNode.outputs)
				o.inputNode = null;
			conNode.outputs.clear();			
			return;
		}
		
		if (node.inputNode != null) {
			node.inputNode.outputs.remove(node);
		}
		conNode.outputs.add(node);
		node.inputNode = conNode;
		
	}
}
