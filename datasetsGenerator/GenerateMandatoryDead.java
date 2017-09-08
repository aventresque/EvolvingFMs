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
