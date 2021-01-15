package CsvToJson.parameterizedJson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class RequestGenerator {
	
	public static ArrayList<String> getRequests(String jsonPath,String csvPath) throws JsonProcessingException, IOException{
		List<Map<String,String>> csvDataMap = read(new File(csvPath));
		
		ArrayList<String> jsonStrings = new ArrayList<String>();
		
		csvDataMap.forEach(map->{
			try{
			String eachJsonString = updateJsonNodes(map,jsonPath);
			jsonStrings.add(eachJsonString);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		});
		return jsonStrings;
	}

private static String updateJsonNodes(Map<String, String> eachCsvEntry, String jsonPath) throws JsonParseException, JsonMappingException, IOException {
	ObjectMapper mapper = new ObjectMapper();
	
	Map<String, Object> map = mapper.readValue(new File(jsonPath), new TypeReference<Map<String,Object>>(){});
	
	Map<String,Object> updateMap = mapIterator(eachCsvEntry,map);
	
	String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(updateMap);
	
	return jsonResult;
}



@SuppressWarnings("unchecked")
private static Map<String, Object> mapIterator(Map<String, String> eachCsvEntry, Map<String, Object> map) {
	
	map.entrySet().forEach(entry->{
		
		if(entry.getValue() instanceof List){
				List<Map<String,Object>> listOfElements = (List<Map<String,Object>>) entry.getValue();
				
				listOfElements.forEach(m->{
					mapIterator(eachCsvEntry,m);
				});
		}
		else if(entry.getValue() instanceof Map){
			mapIterator(eachCsvEntry,(Map<String,Object>) entry.getValue());
		}
		else{
			eachCsvEntry.keySet().forEach(colName->{
				if(entry.getValue()!=null && entry.getValue().equals(colName)){
					map.put(entry.getKey(), eachCsvEntry.get(colName));
				}
			});
			
		}
		
	});
	
	return map;
}



private static List<Map<String,String>> read(File file) throws JsonProcessingException, IOException {
	List<Map<String,String>> response = new LinkedList<Map<String,String>>();
	CsvMapper mapper = new CsvMapper();
	CsvSchema schema =  CsvSchema.emptySchema().withHeader();
	MappingIterator<Map<String,String>> iterator = mapper.reader(Map.class).with(schema).readValues(file);
	while(iterator.hasNext()){
		response.add(iterator.next());
	}
	return response;
}


}
