package ex2.lab1.ies.deti.ua;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {

    private static final Logger logger = LogManager.getLogger(WeatherStarter.class);

    public static void  main(String[] args ) {

        int city_id = Integer.parseInt(args[0]); // 1010500 for Aveiro

        // get a retrofit instance, loaded with the GSon lib to convert JSON into objects
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create a typed interface to use the remote API (a client)
        IpmaService service = retrofit.create(IpmaService.class);
        // prepare the call to remote endpoint
        Call<IpmaCityForecast> callSync = service.getForecastForACity(city_id);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                List<CityForecast> forecastData = forecast.getData();
                if (!forecastData.isEmpty()) {
                    logger.info("Weather forecast for {}, {}", forecast.getGlobalIdLocal(), forecast.getCountry());
                    System.out.printf("Weather forecast for %s, %s%n", forecast.getGlobalIdLocal(), forecast.getCountry());
                    for (CityForecast dayForecast : forecastData) {
                        logger.info("Date: {}", dayForecast.getForecastDate());
                        logger.info("Max Temperature: {}°C", dayForecast.getTMax());
                        logger.info("Min Temperature: {}°C", dayForecast.getTMin());
                        logger.info("Weather Type: {}", dayForecast.getPredWindDir());
                        logger.info("Wind Direction: {}", dayForecast.getPredWindDir());
                        logger.info("Wind Speed: {} km/h", dayForecast.getClassWindSpeed());
                        System.out.printf("%nDate: %s%n", dayForecast.getForecastDate());
                        System.out.printf("Max Temperature: %.1f°C%n", Double.parseDouble(dayForecast.getTMax()));
                        System.out.printf("Min Temperature: %.1f°C%n", Double.parseDouble(dayForecast.getTMin()));
                        System.out.printf("Precipitation Probability: %s%%%n", dayForecast.getPrecipitaProb());
                        System.out.printf("Wind Direction: %s%n", dayForecast.getPredWindDir());
                        System.out.printf("Wind Speed Class: %d%n", dayForecast.getClassWindSpeed());
                    }
                } else {
                    logger.error("No forecast data available for this city.");
                    System.out.println("No forecast data available for this city.");
                }
            } else {
                logger.error("No results for this request!");
                System.out.println( "No results for this request!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
