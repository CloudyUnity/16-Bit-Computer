package CompPack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveManager {

	public static final String FILE_PATH = "data2.json";	
	
	public static void initialise() {
		
		Path path = Paths.get(FILE_PATH);
		if (!Files.exists(path)) {
			try {
				Files.createFile(path);
				save(new SaveData());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			SaveData data = load();
			System.out.println("Saved blocks:");
			for (Block block : data.blocks) {
				System.out.println("Name: " + block.name + " - Nodes: " + block.nodes);
			}
		}
	}
	
	public static void saveBlock(Block block) {
		block.name = InputField.saveBlockName.getText();
		SaveData data = load();
		data.blocks.add(block);
		save(data);
	}
	
	public static void save(Serializable data) {
		try {
			
			Path path = Paths.get(FILE_PATH);
			ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path));
			oos.writeObject(data);
			System.out.println("Saved Data - " + path.toAbsolutePath());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static SaveData load() {
		try {
			
			ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(FILE_PATH)));
			return (SaveData)(ois.readObject());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Block loadBlock(String name) {
		
		SaveData data = load();
		if (data == null) {
			System.out.println("ERROR - DATA NOT FOUND");
			return null;
		}
		
		for (Block block : data.blocks) {
			if (block.name.equals(name)) {
				showBlock(block);
				return block;
			}				
		}
		
		System.out.println("ERROR - BLOCK NAME NOT FOUND");
		return null;
	}
	
	public static void removeBlock(String name) {
		
		SaveData data = load();
		if (data == null) {
			System.out.println("ERROR - DATA NOT FOUND");
			return;
		}
		
		for (Block block : data.blocks) {
			if (block.name.equals(name)) {
				data.blocks.remove(block);
				save(data);
				return;
			}				
		}
	}
	
	public static void showBlock(Block block) {
		System.out.println("Name - " + block.name);
		System.out.println("Nodes - " + block.nodes);
		
		for (Integer i : block.inputs) {
			System.out.println("Input - " + i);
		}
		for (Integer i : block.outputs) {
			System.out.println("Output - " + i);
		}
		for (AND i : block.andList) {
			System.out.println("AND - " + i.input1 + ", " + i.input2 + ", " + i.output);
		}
		for (NOT i : block.notList) {
			System.out.println("NOT - " + i.input + ", " + i.output);
		}
		for (Vector2 i : block.connections) {
			System.out.println("Connection - " + i);
		}
	}
}
