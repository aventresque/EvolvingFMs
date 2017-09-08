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
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import org.sat4j.core.VecInt;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

/* Class where the next feature model is created
 * */

public class GenerateModifications {

	private ArrayList<Feature> new_features;
	private ArrayList<Integer> added_features;
	private int[] mandatory;
	private int[][] new_constraints;
	private Random rand;
	private int nb_old_constraints;
	private String path;
	private GenerateReport reports;

	public GenerateModifications(String path, ArrayList<Feature> features, int[] mandatory, int[][] constraints,
			int feature_percentage, int constraints_percentage, GenerateReport reports) {
		this.reports = reports;
		nb_old_constraints = constraints.length;
		rand = new Random();
		this.mandatory = mandatory;
		new_features = features;
		new_constraints = constraints;
		added_features = new ArrayList<Integer>();
		this.path = path;
		reports.addModification("\nStarting FM generation ...\n");
		long time2 = System.currentTimeMillis();
		this.generateFeatures(feature_percentage);
		this.generateConstraints(constraints_percentage);
		reports.addModification("\n    New FM has : " + new_features.size() + " features\n");
		reports.addModification("    New FM has : " + new_constraints.length + " constraints\n");
		this.writeResult();
		reports.addModification("\nFM generation done in " + (System.currentTimeMillis() - time2) + " ms\n");
		reports.addModification("\n*************************************************************************************\n");
	}

	private void generateFeatures(int percentage) {
		int nb_of_modifications = new_features.size() * percentage / 100;
		int nb_removed = (15 + rand.nextInt(16)) * nb_of_modifications / 100;
		int nb_add = nb_of_modifications - nb_removed;

		reports.addModification("\n    " + nb_of_modifications + " features to modify (add + remove)\n");
		reports.addModification("    Removing " + nb_removed + " features ... ");
		long time1 = System.currentTimeMillis();

		this.remove_features(nb_removed);

		reports.addModification("Done in " + (System.currentTimeMillis() - time1) + " ms\n");
		reports.addModification("    Adding " + nb_add + " features ... ");
		long time2 = System.currentTimeMillis();

		this.add_features(nb_add);

		reports.addModification("Done in " + (System.currentTimeMillis() - time2) + " ms\n");
	}

	private void remove_features(int nb) {
		int removed;
		int[][] temp_constraints = new_constraints.clone();
		int already_removed = 0;
		while (already_removed < nb) {
			removed = rand.nextInt(new_features.size());
			while (mandatory_contains(new_features.get(removed).getId())) {
				removed = rand.nextInt(new_features.size());
			}
			int[][] result = constraints_with_removed(temp_constraints, new_features.get(removed).getId());
			if (feasability_test(result)) {
				already_removed++;
				new_features.remove(removed);
				temp_constraints = result.clone();
			}
		}
		new_constraints = temp_constraints;
	}

	private boolean mandatory_contains(int feature) {
		for (int i = 0; i < mandatory.length; i++) {
			if (mandatory[i] == feature) {
				return true;
			}
		}
		return false;
	}

	private int[][] constraints_with_removed(int[][] temp_constraints, int removed) {
		ArrayList<ArrayList<Integer>> table = new ArrayList<ArrayList<Integer>>(temp_constraints.length);
		for (int i = 0; i < temp_constraints.length; i++) {
			ArrayList<Integer> t = new ArrayList<Integer>(temp_constraints[i].length);
			for (int j = 0; j < temp_constraints[i].length; j++) {
				t.add(temp_constraints[i][j]);
			}
			while (t.contains(removed) || t.contains(-removed)) {
				t.remove((Integer) removed);
				t.remove((Integer) (-removed));
			}
			if (t.size() > 1) {
				table.add(t);
			}
		}
		int[][] result = new int[table.size()][];
		for (int i = 0; i < table.size(); i++) {
			result[i] = new int[table.get(i).size()];
			for (int j = 0; j < table.get(i).size(); j++) {
				result[i][j] = table.get(i).get(j);
			}
		}
		return result;
	}

	private void add_features(int nb) {
		int nextFeature = new_features.get(new_features.size() - 1).getId() + 1;
		for (int i = 0; i < nb; i++) {
			added_features.add(nextFeature + i);
			new_features.add(new Feature(nextFeature + i, "feature_" + (nextFeature + i)));
		}
	}

	private void generateConstraints(int percentage) {
		int nb_of_modifications = new_constraints.length * percentage / 100;
		int nb_removed = nb_old_constraints - new_constraints.length;
		int nb_add = nb_of_modifications - nb_removed;

		reports.addModification("\n    " + nb_removed + " constraints removed\n");
		reports.addModification("    " + nb_add + " constraints to add\n");
		reports.addModification("    Adding " + nb_add + " constraints ... ");
		long time1 = System.currentTimeMillis();

		add_constraints(nb_add);

		reports.addModification("Done in " + (System.currentTimeMillis() - time1) + " ms\n");
	}

	private void add_constraints(int nb) {
		TreeMap<Integer, Double> wheel = new TreeMap<Integer, Double>();
		for (int[] i : new_constraints) {
			if (wheel.containsKey(i.length)) {
				wheel.put(i.length, wheel.get(i.length) + 1.0);
			} else {
				wheel.put(i.length, 1.0);
			}
		}
		double temp = 0;
		for (Integer key : wheel.keySet()) {
			wheel.put(key, wheel.get(key) * 100 / new_constraints.length + temp);
			temp = wheel.get(key);
		}
		
		final int MAXVAR = new_features.size();
		final int NBCLAUSES = new_constraints.length;
		ISolver solver = SolverFactory.newDefault();
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);
		try {
			for (int i = 0; i < NBCLAUSES; i++) {
				int[] constraint = new int[new_constraints[i].length];
				for (int j = 0; j < constraint.length; j++) {
					constraint[j] = new_constraints[i][j];
				}
				solver.addClause(new VecInt(constraint));
			}
		} catch (ContradictionException e) {
			e.printStackTrace();
		}

		int added_success = 0;
		int added_constraint_size;
		int number_new_features;

		while (added_success < nb) {
			added_constraint_size = choose_size(wheel);
			number_new_features = choose_number_NF(added_constraint_size);
			int[] test_constraint = new int[added_constraint_size];
			for (int i = 0; i < number_new_features; i++) {
				test_constraint[i] = added_features.get(rand.nextInt(added_features.size()))
						* (rand.nextBoolean() ? 1 : -1);
			}
			for (int i = number_new_features; i < added_constraint_size; i++) {
				test_constraint[i] = new_features.get(rand.nextInt(new_features.size() - added_features.size())).getId()
						* (rand.nextBoolean() ? 1 : -1);
			}
			try {
				IConstr ic = solver.addClause(new VecInt(test_constraint));
				IProblem problem = solver;
				if (problem.isSatisfiable(new VecInt(mandatory))) {
					added_success++;
					int[][] temp_new_constraints = new int[new_constraints.length + 1][];
					for (int i = 0; i < new_constraints.length; i++) {
						temp_new_constraints[i] = new int[new_constraints[i].length];
						for (int j = 0; j < new_constraints[i].length; j++) {
							temp_new_constraints[i][j] = new_constraints[i][j];
						}
					}
					temp_new_constraints[temp_new_constraints.length - 1] = test_constraint;
					new_constraints = temp_new_constraints;
				} else {
					solver.removeConstr(ic);
				}
			} catch (ContradictionException e) {
				//e.printStackTrace();
			} catch (TimeoutException e) {
				//e.printStackTrace();
			}
		}
	}

	private int choose_size(TreeMap<Integer, Double> wheel) {
		double choice = rand.nextDouble() * 100;
		for (Integer key : wheel.keySet()) {
			if (choice < wheel.get(key)) {
				return key;
			}
		}
		return 0;
	}

	private int choose_number_NF(int size) {
		double choice = rand.nextDouble() * 100;
		if (choice < 5.0) {
			return 0;
		} else if (choice < 50.0) {
			return 1;
		} else if (choice < 60.0) {
			return (int) (size * 0.35);
		} else if (choice < 70.0) {
			return (int) (size * 0.45);
		} else if (choice < 80.0) {
			return (int) (size * 0.55);
		} else if (choice < 90.0) {
			return (int) (size * 0.65);
		} else if (choice < 95.0) {
			return (int) (size * 0.75);
		} else {
			return size;
		}
	}

	private boolean feasability_test(int[][] checking_constraints) {

		final int MAXVAR = new_features.size();
		final int NBCLAUSES = checking_constraints.length;
		ISolver solver = SolverFactory.newDefault();
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);

		try {
			for (int i = 0; i < NBCLAUSES; i++) {
				int[] temp = new int[checking_constraints[i].length];
				for (int j = 0; j < temp.length; j++) {
					temp[j] = checking_constraints[i][j];
				}
				solver.addClause(new VecInt(temp));
			}
			IProblem problem = solver;
			if (problem.isSatisfiable(new VecInt(mandatory))) {
				return true;
			} else {
				return false;
			}
		} catch (ContradictionException e) {
			return false;
		} catch (TimeoutException e) {
			return false;
		}
	}

	private void writeResult() {
		try {
			FileWriter fw = new FileWriter(new File(path + ".next"));
			for (Feature f : new_features) {
				fw.write(f.toString());
			}
			fw.write("p cnf " + new_features.size() + " " + new_constraints.length + "\n");
			for (int[] constraint : new_constraints) {
				for (int i : constraint) {
					fw.write(i + " ");
				}
				fw.write(0 + "\n");
			}
			fw.close();
		} catch (Exception e) {
			System.out.println("Error : cannot generate modification file");
			e.printStackTrace();
		}
	}
}
