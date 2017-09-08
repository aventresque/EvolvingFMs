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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/* A class that is reading the dimacs files and store the informations in data structures :
 * feature , constraints, mandatory features, dead features.
 * */

public class ReadData {

	private int[][] constraints;
	private ArrayList<Feature> features;
	private int[] mandatory;
	private int[] dead;

	public ReadData(String path, GenerateReport reports) {

		features = new ArrayList<Feature>();
		BufferedReader bf = null;

		try {
			reports.addModification("Reading data ...\n");
			long time1 = System.currentTimeMillis();
			String currentLine;
			Pattern p = Pattern.compile(" ");
			bf = new BufferedReader(new FileReader(path));
			int line = 0;
			while ((currentLine = bf.readLine()) != null) {
				if (currentLine.charAt(0) == 'p') {
					String[] elements = p.split(currentLine);
					constraints = new int[Integer.parseInt(elements[3])][];
				}
			}
			bf.close();

			bf = new BufferedReader(new FileReader(path));
			while ((currentLine = bf.readLine()) != null) {
				line++;
				String[] elements = p.split(currentLine);
				if (currentLine.charAt(0) == 'c') {
					features.add(new Feature(line, elements[2]));
				} else if (Character.isDigit(currentLine.charAt(0)) || currentLine.charAt(0) == '-') {
					int[] constraint = new int[elements.length - 1];
					for (int i = 0; i < elements.length - 1; i++) {
						constraint[i] = Integer.parseInt(elements[i]);
					}
					constraints[line - features.size() - 2] = constraint;
				}
			}
			bf.close();

			bf = new BufferedReader(new FileReader(path + ".mandatory"));
			ArrayList<Integer> temp = new ArrayList<Integer>();
			while ((currentLine = bf.readLine()) != null) {
				String[] elements = p.split(currentLine);
				temp.add(Integer.parseInt(elements[0]));
			}
			mandatory = new int[temp.size()];
			for (int i = 0; i < temp.size(); i++) {
				mandatory[i] = temp.get(i);
			}
			bf.close();

			bf = new BufferedReader(new FileReader(path + ".dead"));
			temp = new ArrayList<Integer>();
			while ((currentLine = bf.readLine()) != null) {
				String[] elements = p.split(currentLine);
				temp.add(Integer.parseInt(elements[0]));
			}
			dead = new int[temp.size()];
			for (int i = 0; i < temp.size(); i++) {
				dead[i] = temp.get(i);
			}
			bf.close();
			
			reports.addModification("Reading done in " + (System.currentTimeMillis() - time1) + " ms\n");
			reports.addModification("\n" + features.size() + " features (" + mandatory.length + " mandatory)\n");
			reports.addModification(constraints.length + " constraints\n");
			reports.addModification("\n*************************************************************************************\n");

		} catch (Exception e) {
			System.out.println("Error in ReadData");
			e.printStackTrace();
		} finally {
			try {
				if (bf != null) {
					bf.close();
				}
			} catch (IOException ex) {
				System.out.println("Error : cannot close file in ReadData");
				ex.printStackTrace();
			}
		}
	}

	public int[] getDead() {
		return dead;
	}

	public int[][] getConstraints() {
		return constraints;
	}

	public ArrayList<Feature> getFeatures() {
		return features;
	}

	public int[] getMandatory() {
		return mandatory;
	}

}
