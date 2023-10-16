package CompPack;

import java.util.ArrayList;
import java.util.List;

public class NodeVisible extends Node {

	public NodeVisible inputNode;

	NodeDragger drag;
	public Boolean inputDisabled = false;
	public Boolean deactivated = false;
	public Boolean isInput = false;
	public Boolean isOutput = false;
	public Boolean hideWires = false;

	public static final Vector2 BASE_SCALE = new Vector2(15, 15);

	public NodeVisible(Vector2 pos) {
		super(pos, BASE_SCALE, SceneBuilder.NODE, ColorManager.BLACK);

		setStateSize(1);

		drag = new NodeDragger(this);

		isCircle = true;

		ProgramLogicGateBuilder.node.addNode(this);
	}

	public NodeVisible(Vector2 pos, int busSize) {
		super(pos, BASE_SCALE, SceneBuilder.NODE, ColorManager.BLACK);

		setStateSize(busSize);

		drag = new NodeDragger(this);

		isCircle = true;

		ProgramLogicGateBuilder.node.addNode(this);
	}

	@Override
	public void update() {

		if (drag != null && ProgramLogicGateBuilder.mouse.selected != drag) {
			drag.position = new Vector2(worldPosition());
		}

		Boolean noInput = inputNode == null || inputNode.deactivated;
		if (noInput && !isInput && !inputDisabled) {
			for (int i = 0; i < state.length; i++)
				state[i] = false;
		}
		
		checkOutputs();

		if (state.length == 1 && state[0])
			setColor(ColorManager.RED);
		else
			setColor(ColorManager.BLACK);
	}
	
	@Override
	public void checkOutputs() {
		
		for (int i = outputs.size() - 1; i >= 0; i--) {						

			Node output = outputs.get(i);
			
			if (output instanceof NodeVisible) {
				NodeVisible outVis = (NodeVisible)output;
				
				Boolean isNull = outVis == null;
				Boolean deactive = outVis.deactivated;
				Boolean unEqualLengths = outVis.state.length != state.length;

				if (isNull || deactive || unEqualLengths) {
					outputs.remove(i);
					continue;
				}
			}						
			
			Boolean equal = true;
			for (int j = 0; j < output.state.length; j++) {
				if (output.state[j] != state[j]) {
					equal = false;
					break;
				}
			}
			if (equal)
				continue;						
			
			output.changeState(state);
		}
	}

	public void setStateSize(int size) {
		
		outputs.clear();
		if (inputNode != null) {
			inputNode.outputs.remove(this);
			inputNode = null;
		}
		state = new Boolean[size];
		for (int i = 0; i < size; i++) {
			state[i] = false;
		}
	}

	public List<Vector2> endWirePoint() {

		List<Vector2> results = new ArrayList<Vector2>();

		if (drag == null)
			return results;

		if (ProgramLogicGateBuilder.mouse.selected == drag) {
			results.add(drag.center());
		}

		if (outputs.size() != 0) {

			for (Node output : outputs) {
				results.add(new Vector2(output.worldCenter()));
			}
		}

		return results;
	}

	@Override
	protected void onMouseRelease(double dur) {

		if (state.length == 1) {
			state[0] = !state[0];
			return;
		}

		for (int i = 0; i < state.length; i++) {
			state[i] = Math.random() < 0.5 ? true : false;
		}
	}

	@Override
	protected void onMouse3Pressed() {

		if (isInput || isOutput) {
			try {
				int size = Integer.parseInt(InputField.loadBlockName.getText());
				setStateSize(size);
			} catch (Exception e) {
				hideWires = !hideWires;
			}
			return;
		}
		hideWires = !hideWires;
	}
}