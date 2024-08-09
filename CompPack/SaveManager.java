package CompPack;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {

	public static final String FILE_PATH = "Data/Logic Builder/";

	public static final Boolean USING_MEM_MAP = true;
	public static final Boolean SAVE_AS_BLOCKS = true;

	public static List<Block> cache = new ArrayList<Block>();

	public static void initialise() {

		cache.clear();
	}

	public static void saveAsBlock(Block b) {

		b.name = InputField.saveBlockName.getText();
		Path path = Paths.get(FILE_PATH + "Blocks\\" + b.name);

		try {

			FileOutputStream fos = new FileOutputStream(path.toFile());
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(b);

			oos.close();
			fos.close();

			System.out.println("Saved Data - " + path.toAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Block loadAsBlock(String name) {

		Path path = Paths.get(FILE_PATH + "Blocks\\" + name);

		if (!Files.exists(path)) {
			System.out.println("DATA NOT FOUND");
			return null;
		}

		for (Block b : cache) {
			if (b.name.equals(name))
				return b;
		}

		try {

			if (!USING_MEM_MAP) {

				FileInputStream fis = new FileInputStream(path.toFile());
				ObjectInputStream ois = new ObjectInputStream(fis);
				Block b = (Block) ois.readObject();
				ois.close();

				System.out.println("Loaded data - " + path.toAbsolutePath());

				cache.add(b);

				return b;
			}

			FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			ByteBufferInputStream bbis = new ByteBufferInputStream(buffer);
			ObjectInputStream ois = new ObjectInputStream(bbis);
			Block b = (Block) ois.readObject();

			ois.close(); // Close the stream after reading
			channel.close(); // Close the file channel
			buffer.clear();
			bbis.close();

			System.out.println("Loaded data - " + path.toAbsolutePath());

			cache.add(b);

			return b;

		} catch (EOFException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void removeBlock(String name) {

		Path path = Paths.get(FILE_PATH + "Blocks\\" + name);

		if (!Files.exists(path)) {
			System.out.println("DATA NOT FOUND");
			return;
		}

		try {
			Files.delete(path);
			System.out.println("File deleted successfully: " + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> loadProgram(String name) {

		Path path = Paths.get(FILE_PATH + name + ".txt");

		try {
			return Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<List<String>> loadVM() {

		File folder = new File(FILE_PATH + "VM\\");
		File[] files = folder.listFiles();
		
		List<List<String>> allFiles = new ArrayList<List<String>>();
		
		for (File file : files) {
			
			Path path = Paths.get(FILE_PATH + "VM\\" + file.getName());
			try {
				List<String> data = Files.readAllLines(path);
				allFiles.add(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return allFiles;
	}
	
	public static List<String> loadJack() {

		File folder = new File(FILE_PATH + "Jack\\");
		File[] files = folder.listFiles();
		
		List<String> allFiles = new ArrayList<String>();
		
		for (File file : files) {
			
			Path path = Paths.get(FILE_PATH + "Jack\\" + file.getName());
			try {
				String data = Files.readString(path);
				allFiles.add(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return allFiles;
	}
	
	public static void clearVM() {
		
		File folder = new File(FILE_PATH + "VM\\");
		File[] files = folder.listFiles();
		for (File f : files) {
			f.delete();
		}
	}
	
	public static void saveProgram(String name, String content) {

		Path path = Paths.get(FILE_PATH + name + ".txt");

		try {
			Files.write(path, content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void showBlock(Block block) {
		System.out.println("Name - " + block.name);
		System.out.println("Nodes - " + block.nodes);

		for (int i = 0; i < block.states.size(); i++) {
			System.out.println("Node - " + i + " , States: " + block.states.get(i));
		}
		for (Integer i : block.inputs) {
			System.out.println("Input - " + i);
		}
		for (Integer i : block.outputs) {
			System.out.println("Output - " + i);
		}
		for (DataAND i : block.andList) {
			System.out.println("AND - " + i.input1 + ", " + i.input2 + ", " + i.output);
		}
		for (DataNOT i : block.notList) {
			System.out.println("NOT - " + i.input + ", " + i.output);
		}
		for (Vector2 i : block.connections) {
			System.out.println("Connection - " + i);
		}
		for (DataINBUS i : block.inList) {
			for (int j : i.inputs)
				System.out.println("INB - " + i + " , " + j + " , " + i.output);
		}
	}
}
