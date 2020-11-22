package myBot;

import org.jibble.pircbot.*;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;			
import com.google.gson.JsonObject;	


public class WeatherBot extends PircBot {

	//set name as WeatherBot
	public WeatherBot() {
		this.setName("WeatherBot"); }
	
	//set a boolean to check if reading in an input (initially set to false)
	boolean isReadingTemp = false;
	boolean isReadingA = false;
	
	public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		if(isReadingTemp)
		{
				int fahren = toFahren(getWeather(message));
				sendMessage(channel, "The weather in "  + message + " is " + fahren + " degrees Fahrenheit.");
				isReadingTemp = false;		//set back to false to be able to read in something else next if needed
		}
		
		if(isReadingA)
		{
			System.out.println("isReadingA");
			String info = getAPOD(message);	 
			sendMessage(channel, info);
			isReadingA = false;
		}
		
		if (message.contains("weather") || message.contains("temperature"))
		{
			sendMessage(channel, "Enter the city/zipcode");
			isReadingTemp = true;	
		}
		
		if(message.contains("quote"))
		{
			sendMessage(channel, "getting quote.. enter anything to continue");
			isReadingA = true;
		}
		if (message.contains("Hello"))
		{
			sendMessage(channel, "Hey " + sender + "! ");
		}
		
	}
	
	//if the user types "weather", get weather for location
	public double getWeather(String cityZip)
	{	
		String apiKey = /*API Key*/;		//given api key from openweatherchannel
		//replace q(city) with zip(zipcode) for zip if zip and city do not work in same string
		String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + cityZip + ",US&APPID=" + apiKey;
		
		return getRequestTemp(urlString);	//create url and get request to start parsing to Json
	}
	
	public double getRequestTemp(String urlString)
	{
		
		try
		{
			//create a url object with the urlString
			URL url = new URL(urlString);
			//Handling exceptions
			try
			{
				//get request with GET method - open connection
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");										//request setup
				
				//execute the request
				int status = con.getResponseCode();
				
				//read the response and place it in a content string
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				//Convert buffer reader to string and store it in a result variable (content)
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null)
				{
				    content.append(inputLine);
				}
				//return temperature 
				double temp = parseJsonTemp(content.toString());
				in.close();
				
				return temp;
				
			} 
			catch (IOException error)
			{
				System.out.println("Please try again.");
				error.printStackTrace();
			}
			
		}
		catch (MalformedURLException error)
		{
			System.out.println("Please try again.");
			error.printStackTrace() ;
		}
		 return 0;
	}
	
	//Parsing JSON means interpreting data with 
	public double parseJsonTemp(String content)
	{	
		//In order to get into temperature
		JsonObject object = new JsonParser().parse(content).getAsJsonObject();		//Store data as JsonObject
		JsonObject extract = object.getAsJsonObject("main");
		double temperature = extract.get("temp").getAsDouble();
		return temperature;
	}
	
	//converts kelvin data into fahrenhiet 
	public int toFahren(double kelvin)
	{
		int fahren = (int)(kelvin - 273.15) * 9/5 + 32;
		return fahren;
	}
	
	public String getAPOD(String dummy)
	{
		String urlString = "https://friends-quotes-api.herokuapp.com/quotes/random";
		return getRequestAPOD(urlString);	//create url and get request to start parsing to Json
	}
	
	public String getRequestAPOD(String urlString)
	{
		try
		{
			//create a url object with the urlString
			URL url = new URL(urlString);
			//Handling exceptions
			try
			{
				//get request with GET method - open connection
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");										//request setup
				
				//execute the request
				int status = con.getResponseCode();
				
				//read the response and place it in a content string
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				//Convert buffer reader to string and store it in a result variable (content)
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null)
				{
				    content.append(inputLine);
				}
				
				in.close();

				String info = parseJsonAPOD(content.toString());
				return info;
				
			} 
			catch (IOException error)
			{
				System.out.println("Please try again.");
				error.printStackTrace();
			}
			
		}
		catch (MalformedURLException error)
		{
			System.out.println("Please try again.");
			error.printStackTrace() ;
		}
		 return "";
	}
	
	String quote;
	public String parseJsonAPOD(String content)
	{
		//I need to make a JSON object from the content being passed to the method
		//then I need to access the "quote" object, so I used a get function
		System.out.println("here");
		JsonObject parse = new JsonParser().parse(content).getAsJsonObject();		//Store data as JsonObject
		//this.quote = parse.get("quote").getAsString();
		String quote = parse.get("quote").toString();	
		System.out.println("STRING: " + quote.toString());
		return quote;
	}
}