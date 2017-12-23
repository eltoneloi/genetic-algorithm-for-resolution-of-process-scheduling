package br.com.eltonsilva.metaheuristica.evolutionary;

public class Processor {
	private int processorNumber; //identificador do processador
	private float powerPerSecond; //consumo energetico por segundo
	private float processingSpeed; //velocidade de processamento (pesos baseados em um processador generico)
	
	public Processor(int processorNumber, float powerPerSecond, float processingSpeed) {
		super();
		this.powerPerSecond = powerPerSecond;
		this.processorNumber = processorNumber;
		this.processingSpeed = processingSpeed;
	}
	
	public float getPowerPerSecond() {
		return powerPerSecond;
	}

	public void setPowerPerSecond(float powerPerSecond) {
		this.powerPerSecond = powerPerSecond;
	}

	public float getProcessingSpeed() {
		return processingSpeed;
	}

	public void setProcessingSpeed(float processingSpeed) {
		this.processingSpeed = processingSpeed;
	}
	
	
	
	public int getProcessorNumber() {
		return processorNumber;
	}
	
	public void setProcessorNumber(int processorNumber) {
		this.processorNumber = processorNumber;
	}
	
}
