package br.com.eltonsilva.metaheuristica.evolutionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author eltonsilva
 *
 */
public class Evolutionary {
	private List<Job> listJobs;  //Lista de todos os jobs que serão processados
	private List<Processor> listProcessor; //Lista de todos os processadores
	private List<Sample> samples;//Lista com todas as amostras geradas
	private Sample bestSample; //Amostra com melhor fitness da lista de amostradas geradas
	private float maxConsumption; //Consumo maximo permitido
	private float maxTime; //Tempo de processamento maximo permitido
	private float mutationRate; //Taxa de mutação, quanto menor mais dificil de ocorrer mutação, valor entre 0 e 100
	private double percentageConsumptionToStop;
	private double percentageTimeToStop;
	private int sizeOfSample; //Tamnho total da amostra, indica o tamanho da lista de Sample
	private int numberOfInterations;
	private Sample bestSampleGlobal; //Amostra com melhor fitness da lista de amostradas geradas
	
	//Imprime a distribuição atual de jobs em cada processador
	public void printTable() {
		int count=1;
		
		System.out.println("Processor |  Job");	
		for(Iterator<Job> iterator = bestSampleGlobal.getListJobs().iterator(); iterator.hasNext();count++) {
			Job job = (Job)iterator.next();
			System.out.println(job.getProcessor().getProcessorNumber()+ "         |   "+count);	
		}
		System.out.println("\n"+"Fitness:" + bestSampleGlobal.getFitness() + " Consumption:" + bestSampleGlobal.getConsumption() + "\n");
	}
	
	
	//A entrada dos processadores será feita de maneira estatica aqui, futuramente essa entrada será feita pelo usuário
	public void readProcessor(JsonIO json){
		listProcessor= new ArrayList<Processor>();
		
		listProcessor = json.jsonToListProcessor(listProcessor);
	
	}
	//A entrada dos jobs será feita de maneira estatica aqui, futuramente essa entrada será feita pelo usuário
	public void readJob(JsonIO json){
		listJobs = new ArrayList<Job>();
		listJobs = json.jsonToListJob(listJobs);
		
	}
	
	//gera uma amostra com listas de jobs
	private void factoryRandomSample(int sizeOfSample) {
		Random generator = new Random();
		samples= new ArrayList<Sample>();
		Sample sample;
		
		while(sizeOfSample > 0) {  //Criação de um Sample para decremento de sizeOfSample
			for(int count=0;count < listJobs.size();count++) {
				listJobs.get(count).setProcessor(listProcessor.get(generator.nextInt(listProcessor.size())));
				
			}
			//Montagem o do objeto Sample, execução necessária a cada mudança,
			sample = new Sample();
			sample.setListJobs(listJobs);
			sample.setId(sizeOfSample);
			sample.calcFitness();
			sample.calcConsumption();
			samples.add(sample);
			sizeOfSample--;
			
		}
		bestSample=samples.get(generator.nextInt(samples.size()));
		calcBestFitness();
			
	}
	
	
	//Cria roleta para sorteio
	private float[] createRoulette() {
		float roulette[] = new float[samples.size()]; 
		int count = 0;
		float total=0;
		
		//Calcula o somatoria de todos os fitness de samples, usado para calcular o percentual de cada indice da roleta
		for(Iterator<Sample> sampleIterator = samples.iterator(); sampleIterator.hasNext();count++) {
			Sample element= (Sample)sampleIterator.next();
			total += 1/element.getFitness();
		}
		
		//Associa a cada indice da roleta um intervalo de valores, quanto maior o intervalo maior a chance de ser sorteado
		for(Iterator<Sample> sampleIterator = samples.iterator(); count < samples.size()-1;count++) {
			Sample intermSample= (Sample)sampleIterator.next();
			
			//O criterio de definição dos intervalos foi feito considerando o inverso do fitness, quanto maior o fitness, menor o intervalo
			if(count > 0)
				roulette[count] = (float) (roulette[count-1]+(((1/intermSample.getFitness())/total)*100));
			else
				roulette[count] = (float) (((1/intermSample.getFitness())/total)*100);
			
			
		}
		return roulette;
	}
	
	//Realiza o sorteio da roleta
	private int sortRoulette(float vector[],int size) {
		Random rand = new Random();
		float valueRoulette;
		int index;
		valueRoulette = rand.nextInt(100);
		for(index=0; index< size-1; index++) {
			if(valueRoulette >= vector[index]  && valueRoulette <= vector[index+1])
				break;
		}
		return index;
		
	}
	
	//Retorna duas amostras que sofrerão crossover
	//A Sample é sorteando aleatoriamente respeitando os intervalos da roleta
	private int[] calcParentsSample() {
		int index[]= new int[2];
		float roulette[] = createRoulette();
		int indexFirst = sortRoulette(roulette,samples.size());
		int indexSecond = sortRoulette(roulette,samples.size());
		
		//Para impedir que um Sample seja sorteado duas vezes
		while(indexSecond == indexFirst) {
			indexSecond=sortRoulette(roulette,samples.size());
			
		}
		
		index[0]=indexFirst;
		index[1]=indexSecond;
		
		return index;
	}
	
	
	//Alterar a amostra com melhor fitness
	private void calcBestFitness() {
		float consumption;
		for(Iterator<Sample> sampleIterator = samples.iterator(); sampleIterator.hasNext();) {
			Sample intermSample= (Sample)sampleIterator.next();
			consumption = intermSample.getConsumption();
			//Uso de 3 criterios para calcular o melhor fitness, menor valor fitness, menor consumo e ambos
			if(bestSample.getFitness() >= intermSample.getFitness() && consumption <= maxConsumption) {
				bestSample=intermSample;
			}
			else if(consumption <= maxConsumption) {
				bestSample=intermSample;
			}
			else if(bestSample.getFitness() >= intermSample.getFitness()) {
				bestSample=intermSample;
			}
			
		}
		
		
	}
	
	

	//	Retorna 2 filhos com os cruzamentos feitos
	private void crossover(int father, int mother) {
		
		Random generator= new Random();
		int count =generator.nextInt(11);
		Processor temp;

		//Faz o intercambio de processor a partir do ponto de quebra sorteado
		while(count < samples.get(father).getListJobs().size()) {
			//Fazendo trocas de posições
			temp = samples.get(mother).getListJobs().get(count).getProcessor();
			samples.get(mother).getListJobs().get(count).setProcessor(samples.get(father).getListJobs().get(count).getProcessor());
			samples.get(father).getListJobs().get(count).setProcessor(temp);
			count++;
		}
		//Atualizações dos valors de fitness e consumo de todas as amostras (samples)
		samples.get(father).calcFitness();
		samples.get(father).calcConsumption();
		samples.get(mother).calcFitness();
		samples.get(mother).calcConsumption();
		
	
	}
	
	//Troca uma processor associado
	private void mutation() {
		Random rand = new Random();
		int position = rand.nextInt(samples.size());
		int job = rand.nextInt(listJobs.size());
		int processor = rand.nextInt(listProcessor.size());
		int confirmation = rand.nextInt(100);
		//Verifica se a mutação deve ser aplicada ou não
		if(confirmation < mutationRate) {
			samples.get(position).getListJobs().get(job).setProcessor(listProcessor.get(processor));
			samples.get(position).calcFitness();
			samples.get(position).calcConsumption();
		}
		
	}
	//Execução do loop evolucionário
	public void loop() {
		int amountGeneration=0;
		int vectorParents[];
		
		factoryRandomSample(sizeOfSample);
		bestSampleGlobal = new Sample();
		bestSampleGlobal.setFitness(1000);
		bestSampleGlobal.setConsumption(1000);
		while(!(/*bestSample.getFitness() <= maxTime*/numberOfInterations < amountGeneration && bestSampleGlobal.getConsumption() <= maxConsumption)) {
			if(bestSampleGlobal.getFitness() >= bestSample.getFitness() && bestSample.getConsumption() <= maxConsumption ) {
				bestSampleGlobal.setFitness(bestSample.getFitness());
				bestSampleGlobal.setConsumption(bestSample.getConsumption());
				bestSampleGlobal.setListJobs(bestSample.getListJobs());
			}
			createRoulette();
			vectorParents = calcParentsSample();
			crossover(vectorParents[0],vectorParents[1]);
			mutation();
			calcBestFitness();
			//Impressão apenas para ver o andamento do algoritmo, pode ser retirada sem prejuizo
			//System.out.println("Current generation:"+ amountGeneration +"  Fitness:" + bestSample.getFitness() + " Consumption:" + bestSample.getConsumption());
			System.out.println("Current generation:"+ amountGeneration +" FitnessGlobal:"+bestSampleGlobal.getFitness()+"  Fitness:" + bestSample.getFitness() + " ConsumptionGlobal:" + bestSampleGlobal.getConsumption());
			amountGeneration++; 
		}
	}
	
	
	/////////////////////////////Getters and Setters////////////////////////////////
	
	public double getMaxConsumption() {
		return maxConsumption;
	}
	
	public float getMaxTime() {
		return maxTime;
	}
	
	public void setMaxTime(float maxTime) {
		this.maxTime = maxTime;
	}
	public void setMaxConsumption(float maxConsumption) {
		this.maxConsumption = maxConsumption;
	}
	
	public List<Job> getListJobs() {
		return listJobs;
	}

	public void setListJobs(List<Job> listJobs) {
		this.listJobs = listJobs;
	}

	public List<Processor> getListProcessor() {
		return listProcessor;
	}

	public void setListProcessor(List<Processor> listProcessor) {
		this.listProcessor = listProcessor;
	}

	public List<Sample> getSamples() {
		return samples;
	}

	public void setSamples(List<Sample> samples) {
		this.samples = samples;
	}

	public Sample getBestSample() {
		return bestSample;
	}

	public void setBestSample(Sample bestSample) {
		this.bestSample = bestSample;
	}
	
	public double getMutationRate() {
		return mutationRate;
	}
	
	public void setMutationRate(float mutationRate) {
		this.mutationRate = mutationRate;
	}
	
	public int getSizeOfSample() {
		return sizeOfSample;
	}
	
	public void setSizeOfSample(int sizeOfSample) {
		this.sizeOfSample = sizeOfSample;
	}
	
	public double getPercentageConsumptionToStop() {
		return percentageConsumptionToStop;
	}
	
	public void setPercentageConsumptionToStop(double percentageConsumptionToStop) {
		this.percentageConsumptionToStop = percentageConsumptionToStop;
	}
	
	public double getPercentageTimeToStop() {
		return percentageTimeToStop;
	}
	
	public void setPercentageTimeToStop(double percentageTimeToStop) {
		this.percentageTimeToStop = percentageTimeToStop;
	}


	public int getNumberOfInterations() {
		return numberOfInterations;
	}


	public void setNumberOfInterations(int numberOfInterations) {
		this.numberOfInterations = numberOfInterations;
	}
	
	public Sample getBestSampleGlobal() {
		return bestSampleGlobal;
	}


	public void setBestSampleGlobal(Sample bestSampleGlobal) {
		this.bestSampleGlobal = bestSampleGlobal;
	}

}
