package br.com.eltonsilva.metaheuristica.evolutionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonIO {
	private String filePath;
	

	public JsonIO(String filePath) {
		super();
		this.filePath = filePath;
	}

	public boolean objectToJson(Object obj){
		Gson gson = new Gson();
	 
		// converte objetos Java para JSON e retorna JSON como String
		String json = gson.toJson(obj);
	 
		try {
			//Escreve Json convertido em arquivo chamado "file.json"
			FileWriter writer = new FileWriter(filePath);
			writer.write(json);
			writer.close();
			return true;
	 
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Job> jsonToListJob(List<Job> listJobs){
		BufferedReader read;
		try {
			read = new BufferedReader(new FileReader(filePath));
			Gson json = new Gson();
			Type collectionType = new TypeToken<List<Job>>() {}.getType();
			return json.fromJson(read, collectionType);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Processor> jsonToListProcessor(List<Processor> listProcessor) {
		BufferedReader read;
		try {
			read = new BufferedReader(new FileReader(filePath));
			Gson json = new Gson();
			Type collectionType = new TypeToken<List<Processor>>() {}.getType();
			return json.fromJson(read, collectionType);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
	
	

