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
