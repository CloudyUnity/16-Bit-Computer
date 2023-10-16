package CompPack;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class NodeDragger extends Shape{

	public NodeVisible conNode;
	
	public NodeDragger(NodeVisible input) {
		super(input.position, input.scale, SceneBuilder.INVISIBLE, Color.cyan);
		this.conNode = input;
		
		draggable = true;
		interactible = true;
		visible = false;
	}
	
	@Override 
	protected void onMousePressed(MouseEvent e) {
				
		if (conNode.draggable && e.getClickCount() == 2) {
			ProgramLogicGateBuilder.mouse.setSelected(conNode);
		}
	}
	
	@Override
	protected void onMouse3Pressed() {
		
		conNode.onMouse3Pressed();
	}
	
	@Override
	protected void onMouseRelease(double dur) {
		
		if (dur < 200) {
			conNode.onMouseRelease(dur);
			position = conNode.position;
			return;
		}			
		
		NodeVisible node = ProgramLogicGateBuilder.node.FindClosestNodeTo(position, conNode);
		
		position = conNode.position;
		if (node == null) {
			for (Node o : conNode.outputs)
				((NodeVisible)o).inputNode = null;
			conNode.outputs.clear();			
			return;
		}
		
		if (node.state.length != conNode.state.length)
			return;
		
		if (node.inputNode != null) {
			node.inputNode.outputs.remove(node);
		}
		conNode.outputs.add(node);
		node.inputNode = conNode;
		
	}
}
