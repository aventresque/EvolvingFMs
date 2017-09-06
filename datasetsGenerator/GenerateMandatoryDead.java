import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GenerateMandatoryDead {

	public GenerateMandatoryDead(String source, String destination, GenerateReport reports){
		
		try {
			reports.addModification("\nStarting mandatory and dead copy ...\n");
			long time1 = System.currentTimeMillis();
			Files.copy(new File(source + ".mandatory").toPath(), new File(destination + ".mandatory").toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(new File(source + ".dead").toPath(), new File(destination + ".dead").toPath(), StandardCopyOption.REPLACE_EXISTING);
			reports.addModification("\nMandatory and dead copy done in " + (System.currentTimeMillis()-time1) + " ms\n");
			reports.addModification("\n*************************************************************************************\n");

		} catch (Exception e) {
			System.out.println("Error : cannot copy mandatory and dead files");
			e.printStackTrace();		
		}
	}
}
