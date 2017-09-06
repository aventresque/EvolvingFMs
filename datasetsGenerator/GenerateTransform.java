import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class GenerateTransform {
	
	public GenerateTransform(String path_to_next, String path_to_transform, String path_to_original, GenerateReport reports){
		
        FileWriter fw;
        File f;
		
		HashMap <Integer, Integer> comp = new HashMap<Integer, Integer>();
		ArrayList<String> new_CNF_file = new ArrayList<String>();
		ArrayList<String> new_mandatory_file = new ArrayList<String>();
		ArrayList<String> new_dead_file = new ArrayList<String>();
		ArrayList<String> new_augment_file = new ArrayList<String>();
		ArrayList<String> new_result_file = new ArrayList<String>();
		
		BufferedReader br = null;
		Pattern p = Pattern.compile(" ");
		String currentLine = "";
		
		/*Reading*/
		
		try{
			reports.addTransform("Starting transformation ...\n\n");
			long time1 = System.currentTimeMillis();
			
			reports.addTransform("    Transform dimacs ... ");
			br = new BufferedReader(new FileReader(path_to_next));
			int i = 0;
			while((currentLine = br.readLine()) != null){			// Transform FM CNF
				if(currentLine.charAt(0) == 'c'){					// Reading features
					i++;
					String[] elements = p.split(currentLine);
					comp.put(Integer.parseInt(elements[1]), i);
					new_CNF_file.add("c " + i + " " + elements[2]);
				} else if(currentLine.charAt(0) == 'p'){			// Reading "p cnf #feature #clauses"
					new_CNF_file.add(currentLine);
				} else {											// Reading constraints
					String[] elements = p.split(currentLine);
					String temp = "";
					for(int j=0; j<elements.length-1; j++){
						temp += elements[j].charAt(0) == '-' ? "-" + comp.get(Integer.parseInt(elements[j].substring(1)))
								+ " " : comp.get(Integer.parseInt(elements[j])) + " ";
					}
					new_CNF_file.add(temp + "0");
				}
			}
			br.close();
			
			reports.addTransform("done in " + (System.currentTimeMillis()-time1) + " ms\n");
			long time2 = System.currentTimeMillis();
			reports.addTransform("    Transform mandatory ... ");
			
			br = new BufferedReader(new FileReader(path_to_next + ".mandatory"));	// Transform mandatory
			while((currentLine = br.readLine()) != null){
				new_mandatory_file.add(""+comp.get(Integer.parseInt(currentLine)));
			}
			br.close();
			
			reports.addTransform("done in " + (System.currentTimeMillis()-time2) + " ms\n");
			time2 = System.currentTimeMillis();
			reports.addTransform("    Transform dead ... ");
			
			br = new BufferedReader(new FileReader(path_to_next + ".dead"));		// Transform dead
			while((currentLine = br.readLine()) != null){
				if(comp.containsKey(Integer.parseInt(currentLine))){
					new_dead_file.add(""+comp.get(Integer.parseInt(currentLine)));
				}
			}
			br.close();
			
			reports.addTransform("done in " + (System.currentTimeMillis()-time2) + " ms\n");
			time2 = System.currentTimeMillis();
			reports.addTransform("    Transform augment ... ");
			
			br = new BufferedReader(new FileReader(path_to_next + ".augment"));		// Transform augment
			new_augment_file.add("#FEATURE_INDEX COST USED_BEFORE DEFECTS");
			while((currentLine = br.readLine()) != null){
				if(Character.isDigit(currentLine.charAt(0))){
					String[] elements = p.split(currentLine);
					new_augment_file.add(comp.get(Integer.parseInt(elements[0])) + " " + elements[1] + " " + elements[2] + " " + elements[3]);	
				}
			}
			br.close();
			
			reports.addTransform("done in " + (System.currentTimeMillis()-time2) + " ms\n");
			time2 = System.currentTimeMillis();
			reports.addTransform("    Transform result ... ");
			
			br = new BufferedReader(new FileReader(path_to_original + ".result"));	// Transform result
			while(!(currentLine = br.readLine()).equals("*****")){
				ArrayList<Boolean> result = new ArrayList<Boolean>();
				for(int j=0; j<i; j++){
					result.add(false);
				}
				for(int j=0; j<currentLine.length(); j++){
					if(comp.containsKey(j+1)){
						result.set(comp.get(j+1)-1, currentLine.charAt(j) == '1' ? true : false);
					}
				}
				String converted = "";
				for(Boolean b : result){
					converted += b == true ? "1" : "0";
				}
				new_result_file.add(converted);
			}
			br.close();
			
			reports.addTransform("done in " + (System.currentTimeMillis()-time2) + " ms\n\n");
			
			/*Writting*/
			
			f = new File(path_to_transform + "/ecos-icse11.dimacs");				//Writting FM CNF
			fw = new FileWriter(f);
            for (String s : new_CNF_file) {
                fw.write(s+"\n");
            }
            fw.close();
            
            f = new File(path_to_transform + "/ecos-icse11.dimacs.mandatory");		//Writting mandatory
			fw = new FileWriter(f);
            for (String s : new_mandatory_file) {
                fw.write(s+"\n");
            }
            fw.close();
            
            f = new File(path_to_transform + "/ecos-icse11.dimacs.dead");			//Writting dead
			fw = new FileWriter(f);
            for (String s : new_dead_file) {
                fw.write(s+"\n");
            }
            fw.close();
            
            f = new File(path_to_transform + "/ecos-icse11.dimacs.augment");		//Writting augment
			fw = new FileWriter(f);
            for (String s : new_augment_file) {
                fw.write(s+"\n");
            }
            fw.close();
            
            f = new File(path_to_transform + "/ecos-icse11.dimacs.richseed");		//Writting seed
			fw = new FileWriter(f);
            for (String s : new_result_file) {
                fw.write(s+"\n");
            }
            fw.close();
            
            reports.addTransform("Complete transformation running time in " + (System.currentTimeMillis()-time1) + " ms");

			
		} catch (Exception e) {
			System.out.println("Error : cannot write transform files");
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				System.out.println("Error : cannot close transform files");
				ex.printStackTrace();
			}
		}
	}

}
