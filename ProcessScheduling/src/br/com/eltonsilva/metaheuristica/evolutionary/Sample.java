package br.com.eltonsilva.metaheuristica.evolutionary;

import java.util.Iterator;
import java.util.List;

public class Sample {
	private int id;
	private List<Job> listJobs;
	private float fitness=0;
	private float consumption=0;
	
	//Calcula o consumo energetico total da amostra
	protected void calcConsumption() {
		float consum=0;
		for(Iterator<Job> iterator = listJobs.iterator(); iterator.hasNext();) {
			Job job= (Job) iterator.next();
			consum+= job.getProcessor().getPowerPerSecond()*job.getProcessingTime()*job.getProcessor().getProcessingSpeed();
		}
		this.consumption = consum;
	}
	
	//calcula o fitness da amostra. O fitness será o maior tempo de execução associado a um dos processadores
	protected void calcFitness() {
		float fitness=0;
		float processor[]= new float[4];
		
			for(int index=0; index < processor.length; index++) {
				fitness=0;
				for(Iterator<Job> jobIterator = listJobs.iterator();jobIterator.hasNext();) {
					Job job = (Job)jobIterator.next();
					if(job.getProcessor().getProcessorNumber()-1 == index) {
						fitness+= job.getProcessingTime() * job.getProcessor().getProcessingSpeed();
					}
				}
				processor[index]=fitness;
			}
			
			fitness = processor[0];
			for(int index=1; index < processor.length; index++) {
				if(fitness < processor[index])
						fitness=processor[index];
			}
			
		this.fitness=fitness;
	}
		
	public List<Job> getListJobs() {
		return listJobs;
	}
	public void setListJobs(List<Job> listJobs) {
		this.listJobs = listJobs;
	}
	public float getFitness() {
		return fitness;
	}
	public void setFitness(float fitness) {
		this.fitness = fitness;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getConsumption() {
		return consumption;
	}

	public void setConsumption(float consumption) {
		this.consumption = consumption;
	}

	
}
