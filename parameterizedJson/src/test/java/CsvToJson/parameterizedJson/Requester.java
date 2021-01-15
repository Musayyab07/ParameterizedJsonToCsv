package CsvToJson.parameterizedJson;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Requester {

	public static void main(String[] args) throws JsonProcessingException, IOException {
		
		String jsonPath = "src/test/resources/ApiRequest.json";
		
		String csvPath = "src/test/resources/ReadData.csv";
		
		ArrayList<String> requestList = RequestGenerator.getRequests(jsonPath,csvPath);
		
		for(String eachRequest:requestList){
			System.out.println(eachRequest);
		}
		

	}

}
