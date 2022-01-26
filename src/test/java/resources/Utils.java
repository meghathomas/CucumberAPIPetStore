package resources;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Random;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Utils {
	public static RequestSpecification req;
    public static String getDataFromPropertiesFile(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src\\test\\java\\resources\\Properties.properties");
        prop.load(fis);
        String value = prop.getProperty(key);
        return value;
    }
    public static RequestSpecification requestSpecification() throws IOException
    {
    	if(req==null) {
    		PrintStream log =new PrintStream(new FileOutputStream("logging.txt"));
            req=new RequestSpecBuilder().setBaseUri(getDataFromPropertiesFile("baseUrl"))
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON)
                    .build();
            return req;
    	}
    	return req;  
    }
    
    public static long generateRandomNumber() {
    	long r = new Random().nextLong(100);
		return r+1;
    }

}
