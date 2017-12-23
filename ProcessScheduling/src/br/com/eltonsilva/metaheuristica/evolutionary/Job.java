package br.com.eltonsilva.metaheuristica.evolutionary;

public class Job {
	private String processName;
	private float processingTime; //Tempo de execução para um processador generico
	private boolean processed = false;  //Indentifica se o processo já foi executado
	private Processor processor; //Processador que irá processar o job
	
	
	public Job(String processName, int processingTime) {
		super();
		this.processName = processName;
		this.processingTime = processingTime;
	}
	
	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	public void setProcessingTime(float processingTime) {
		this.processingTime = processingTime;
	}

	
	public String getProcessName() {
		return processName;
	}
	
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	public double getProcessingTime() {
		return processingTime;
	}
	
	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}
	
	public boolean isProcessed() {
		return processed;
	}
	
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	
	
}
