import java.util.Scanner;
import org.jibble.pircbot.*;
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


	public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		if (message.contains("weather") || message.contains("temperature"))
		{
			getWeather();
		}

		if (message.contains("Hello"))
		{
			sendMessage(channel, "Hey " + sender + "! ");
		}
	}

	//if the user types "weather", get weather for location
	public void getWeather()
	{
		Scanner input = new Scanner(System.in);

		String cityName = "";
		String zipCode = "";
		double temp;

		System.out.print("Enter the city/zipcode");
		String cityZip = input.nextLine();

		String apiKey = /*API Key*/;

		String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + cityZip + "US&APPID" + apiKey;
		getRequest(urlString);


	}

	public void getRequest(String urlString)
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
				System.out.println(content);
				in.close();

				System.out.println(content.toString());
				//const json = 'h';
				parseJson(content);

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

	}

	public void parseJson(StringBuffer content)
	{
		String jsonS = "";
		Gson gson = new Gson();
		//JsonElement jelement = new JsonParser().parse(content);
		JsonObject jsonObject = gson.fromJson(jsonS, JsonObject.class);
		int id = jsonObject.get("temp").getAsInt();

		System.out.println("main " + jsonObject.get("main").getAsInt());

	}
}