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

public class GenerateReport {

	private ArrayList<String> generate_modification_report;
	private ArrayList<String> generate_transform_report;
	private String path;
	private int generation;

	public GenerateReport(String path, int i) {
		this.path = path;
		generation = i;
		generate_modification_report = new ArrayList<String>();
		generate_transform_report = new ArrayList<String>();

	}

	public void addModification(String s) {
		generate_modification_report.add(s);
	}

	public void addTransform(String s) {
		generate_transform_report.add(s);
	}

	public void writeReports() {
		try {
			// Writting modofication report
			FileWriter fw = new FileWriter(new File(path + "modifications" + generation +"/modification.report"));
			for (String s : generate_modification_report) {
				fw.write(s);
			}
			fw.close();

			// Writting transform report
			fw = new FileWriter(new File(path + "transform" + generation +"/transform.report"));
			for (String s : generate_transform_report) {
				fw.write(s);
			}
			fw.close();

		} catch (Exception e) {
			System.out.println("Error : cannot generate reports");
			e.printStackTrace();
		}
	}
}
