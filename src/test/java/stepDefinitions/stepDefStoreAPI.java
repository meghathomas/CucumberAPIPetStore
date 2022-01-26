package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import resources.Utils;

public class stepDefStoreAPI {
	public static Map<String,Object> payload;
    ObjectMapper objectMapper;
    public static long id;
    RequestSpecification res;
    Response response;
	
	@Given("Generate Payload for {string} POST Request")
	public void generate_payload_for_post_request(String fileName) throws JsonParseException, JsonMappingException, IOException {
		objectMapper = new ObjectMapper();
		payload = objectMapper.readValue(new File("src\\test\\java\\resources\\jsons\\"+fileName+".json"), new TypeReference<Map<String,Object>>(){});
        id=Utils.generateRandomNumber();
        payload.put("id", id);
	    
	}

	@When("Generate request for API")
	public void generate_request_for_api() throws IOException {
		res=given().spec(Utils.requestSpecification());
	}

	@When("Call the {string} API with resource {string}")
	public void call_the_api_with_resource(String methodType, String resource) throws IOException {
		if(methodType.equalsIgnoreCase("POST")) {
			res=res.body(payload);
            response = res.when().post(Utils.getDataFromPropertiesFile(resource));
		}
	    
	}

	@Then("Validate response JSON schema using {string}")
	public void validate_response_json_schema_using(String schemafile) {
    	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemafile));
	}
	
	@Then("Validate Status code is {int}")
	public void validate_status_code_is(Integer statusCode) {
		assertEquals(response.statusCode(),statusCode);
	}
	
	@When("Call the {string} API with resource {string} by {string}")
	public void call_the_api_with_resource_by(String methodtype, String resource, String value) throws IOException {
		if(methodtype.equalsIgnoreCase("GET")) {
			if(value.equalsIgnoreCase("id")){
				response=res.when().get(Utils.getDataFromPropertiesFile(resource)+"/"+id);
	        }else {
	        	response=res.when().get(Utils.getDataFromPropertiesFile(resource));
	        }
		}else if(methodtype.equalsIgnoreCase("DELETE")) {
			response=res.when().delete(Utils.getDataFromPropertiesFile(resource)+"/"+id);
		}
		
	}

	@Then("Validate {string} in response")
	public void validate_in_response(String value) {
		JsonPath js = new JsonPath(response.asString());
		if(value.equals("message")) {
			assertTrue("Order not found".equalsIgnoreCase(js.get(value).toString()));
		}else {
			assertEquals(payload.get(value).toString(),js.get(value).toString());
		}    
	}
}
