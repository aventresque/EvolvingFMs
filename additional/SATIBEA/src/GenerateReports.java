

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class GenerateReports {
	private ArrayList<String> hypervolume_report;
	private ArrayList<String> time_report;
	private ArrayList<String> non_violated_report;
	private ArrayList<String> objectiv_report;
	private ArrayList<String> big_hypervolume_report;
	private String path;
	private String i;

	public GenerateReports(String path) {
		this.path = path;
		hypervolume_report = new ArrayList<String>();
		time_report = new ArrayList<String>();
		non_violated_report = new ArrayList<String>();
		objectiv_report = new ArrayList<String>();
		big_hypervolume_report = new ArrayList<String>();
	}

	public void addHypervolume(String s) {
		hypervolume_report.add(s);
	}

	public void addTime(String s) {
		time_report.add(s);
	}

	public void addNonViolated(String s) {
		non_violated_report.add(s);
	}

	public void addObjectiv(String s) {
		objectiv_report.add(s);
	}
	
	public void addBigHypervolume(String s){
		big_hypervolume_report.add(s);
	}

	public void writeReports() {
		try {
			// Writing hypervolume report
			FileWriter fw = new FileWriter(new File(path + "_HV"));
			for (String s : hypervolume_report) {
				fw.write(s);
			}
			fw.close();

			// Writing time report
			fw = new FileWriter(new File(path + "_time"));
			for (String s : time_report) {
				fw.write(s);
			}
			fw.close();

			// Writing non violated report
			fw = new FileWriter(new File(path + "_non_violated"));
			for (String s : non_violated_report) {
				fw.write(s);
			}
			fw.close();

			// Writting objectiv report
			fw = new FileWriter(new File(path + "_obj"));
			for (String s : objectiv_report) {
				fw.write(s);
			}
			fw.close();

			// Writting big hypervolume report
			fw = new FileWriter(new File(path + "_bigHV"));
			for (String s : big_hypervolume_report) {
				fw.write(s);
			}
			fw.close();

		} catch (Exception e) {
			System.out.println("Error : cannot generate reports");
			e.printStackTrace();
		}
	}
}
