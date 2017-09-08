/*
    A benchmark for feature selection when feature models evolve.
    Copyright (C) 2017  Takfarinas Saber, David Brevet, Goetz Botterweck and Anthony Ventresque

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.File;
import java.util.ArrayList;

/* Argument 0 : percentage of features that will be added/removed ( i.e FM=5000 features, 10% modification : nb removed + nb add = 500 )
 * Argument 1 : percentage of constraints that will be added/removed ( i.e FM=100000 constraints, 10% modification : nb removed + nb add = 10000 )
 * Argument 2 : number of new feature models you want to create
 * Important : argument 2 does not take into account modified constraints, just added and removed !
 * Important : original FM, mandatory features, dead features, augment and results should be in directory "../Feature_Model_Generator/ressources/original"
 * Important : name of the FM is in FM_Name var.
 * */

public class Main {

	public static void main(String[] args) {
		
		String FM_Name = "/freebsd-icse11.dimacs";
		String new_percentage_folder = "/" + args[0] + "." + args[1];
		new File(new File("").getAbsolutePath() + "/ressources" + new_percentage_folder).mkdir();
		
		
		//Number of new FM we want to generate
		for(int i = 0; i < Integer.parseInt(args[2]); i++){
		
			long starting_time = System.currentTimeMillis();
	
			if (Integer.parseInt(args[0]) > 100 || Integer.parseInt(args[1]) > 100) {
				System.out.println("Enter a value between 0 and 100 in arguments 1 and 2");
			} else {
				
				System.out.println("*************************** Starting generation number " + (i+1) +" ***************************\n");
				String path = new File("").getAbsolutePath() + "/ressources/";
				GenerateReport reports = new GenerateReport(path + new_percentage_folder + "/", i+1);
				
				//Reading original feature model data and store informations into datastructures
				System.out.print("	Read data ... ");
				ReadData rd = new ReadData(path + "original"+FM_Name, reports);	
				ArrayList<Feature> features = rd.getFeatures();
				int[][] constraints = rd.getConstraints();
				int[] mandatory = rd.getMandatory();
				int[] dead = rd.getDead();
				System.out.println("done");
				
				//Generate the modifications
				System.out.print("	Generate modifications ... ");
				new File(path + new_percentage_folder + "/modifications" + (i+1)).mkdir();
				new GenerateModifications(path + new_percentage_folder + "/modifications" + (i+1) +FM_Name, features, mandatory, constraints,
						Integer.parseInt(args[0]), Integer.parseInt(args[1]), reports);
				System.out.println("done");
				
				//Generate the augment
				System.out.print("	Generate augment ... ");
				new GenerateAugment(path + "original" + FM_Name + ".augment",
						path + new_percentage_folder + "/modifications" + (i+1) + FM_Name + ".next", reports);
				System.out.println("done");
				
				//Copie mandatory and dead file
				System.out.print("	Generate mandatory and dead ... ");
				new GenerateMandatoryDead(path + "original" + FM_Name,
						path + new_percentage_folder + "/modifications" + (i+1) + FM_Name + ".next", reports);
				System.out.println("done");
				
				//Generate transformations for each new FM : SATIBEA will be able to read this transformations
				System.out.print("	Generate transform ... ");
				new File(path + new_percentage_folder + "/transform" + (i+1)).mkdir();
				new GenerateTransform(path + new_percentage_folder + "/modifications" + (i+1) + FM_Name + ".next", 
						path + new_percentage_folder + "/transform" + (i+1) +"/", path + "original" + FM_Name, reports);
				System.out.println("done\n");
				
				reports.addModification("\nComplete creation running time in " + (System.currentTimeMillis() - starting_time) + " ms");
				reports.writeReports();
			}
		}
	}

}
