import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import br.com.eltonsilva.metaheuristica.evolutionary.Evolutionary;
import br.com.eltonsilva.metaheuristica.evolutionary.JsonIO;

public class Test {

	public static void main(String[] args) throws IOException {
		Evolutionary evolutionary = new Evolutionary();
		File path = new File("");
		JsonIO jsonJobs =  new JsonIO(path.getAbsolutePath()+"/jobs.json");
		JsonIO jsonProcessor =  new JsonIO(path.getAbsolutePath()+"/processor.json");
		JsonIO jsonResults =  new JsonIO(path.getAbsolutePath()+"/results.json");
		evolutionary.readProcessor(jsonProcessor);
		evolutionary.readJob(jsonJobs);
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Informe o valor limite para consumo energetico");
		evolutionary.setMaxConsumption(scan.nextInt());
		/*System.out.println("Informe o valor limite para tempo de processamento");
		evolutionary.setMaxTime(scan.nextInt());*/
		System.out.println("Informe o numero de iteracoes limite");
		evolutionary.setNumberOfInterations(scan.nextInt());
		scan.close();
		evolutionary.setMutationRate(5);
		evolutionary.setSizeOfSample(10);
		evolutionary.loop();
		
		jsonResults.objectToJson(evolutionary.getBestSampleGlobal());
		
		//Gambiarra para limpar tela
		for(int line=0; line < 10 ;line++) {
			System.out.println("");
		}
		
		evolutionary.printTable();
	}
}
