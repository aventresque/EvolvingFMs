import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

public class GenerateAugment {
	
	HashMap<Integer, String> old_augments_id;
	ArrayList<String> to_print;
	
	public GenerateAugment(String old_augment_path, String new_fm_path, GenerateReport reports){
		
		reports.addModification("\nStarting augment generation ...\n");
		long time1 = System.currentTimeMillis();
		old_augments_id = new HashMap<Integer, String>();
		to_print = new ArrayList<String>();
		
		String currentLine = "";
		BufferedReader br = null;
		Pattern p = Pattern.compile(" ");
		Random rand = new Random();
		
		try{
			br = new BufferedReader(new FileReader(old_augment_path));
			while((currentLine = br.readLine()) != null){
				if(Character.isDigit(currentLine.charAt(0)) || currentLine.charAt(0) == '-'){
					String[] elements = p.split(currentLine);
					old_augments_id.put(Integer.parseInt(elements[0]), currentLine);
				}
			}
			br.close();
			
			br = new BufferedReader(new FileReader(new_fm_path));
			while((currentLine = br.readLine()).charAt(0) == 'c'){
				String[] elements = p.split(currentLine);
				if(old_augments_id.containsKey(Integer.parseInt(elements[1]))){
					to_print.add(old_augments_id.get(Integer.parseInt(elements[1])));
				}
				else{
					to_print.add(elements[1] + " " + (rand.nextFloat() * 10 + 5) + " " + (rand.nextBoolean() == true ? 1 : 0) + " "
							+ (rand.nextInt(10) + 1));
				}
			}
			
			File f = new File(new_fm_path + ".augment");
			FileWriter fw = new FileWriter(f);
			fw.write("#FEATURE_INDEX COST USED_BEFORE DEFECTS\n");

			for (String s : to_print) {
				fw.write(s);
				fw.write("\r\n");
			}

			fw.close();
			
			reports.addModification("\nAugment generation done in " + (System.currentTimeMillis()-time1) + " ms\n");
			reports.addModification("\n*************************************************************************************\n");

		} catch (Exception e) {
			System.out.println("Error cannot generate augment");
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				System.out.println("Error cannot close augment file");
				ex.printStackTrace();
			}
		}
	}

}
