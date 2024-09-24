package ex5.lab1.ies.deti.ua;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

import java.util.Random;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ex5_api.lab1.ies.deti.ua.CityForecast;
import ex5_api.lab1.ies.deti.ua.ForecastService;

public class Main {

    Toolkit toolkit;
    Timer timer;
    private static final Logger logger = LogManager.getLogger(Main.class);
    private ForecastService forecastService;

    public Main() {
        toolkit = Toolkit.getDefaultToolkit();
        timer = new Timer();
        forecastService = new ForecastService();
        timer.scheduleAtFixedRate(new RemindTask(), 0, 20 * 1000); // 20s interval
    }

    class RemindTask extends TimerTask {
        @Override
        public void run() {
            long time = System.currentTimeMillis();
            if (time - scheduledExecutionTime() > 5) {
                return;
            }

            int city_id = CityIdSelector.getRandomCityId();
            
            try {
                // Delegate the request to Project 2 (ForecastService)
                List<CityForecast> forecastData = forecastService.getForecastForCity(city_id);
                
                // Log the forecast data
                logger.info("Weather forecast for city ID: {}", city_id);
                for (CityForecast dayForecast : forecastData) {
                    logger.info("Date: {}", dayForecast.getForecastDate());
                    logger.info("Max Temperature: {}°C", dayForecast.getTMax());
                    logger.info("Min Temperature: {}°C", dayForecast.getTMin());
                    logger.info("Weather Type: {}", dayForecast.getPredWindDir());
                    logger.info("Wind Speed: {} km/h", dayForecast.getClassWindSpeed());
                    logger.info("-------------------------------------------");
                }
                logger.info("===========================================");
            } catch (Exception ex) {
                logger.error("Error retrieving forecast: {}", ex.getMessage());
            }
        }
    }

    public static void main(String args[]) {
        new Main();
    }
}


class CityIdSelector {

    // Array contendo os IDs das cidades
    private static final int[] cityIds = {
        1010500, // Aveiro
        1020500, // Beja
        1030300, // Braga
        1040200, // Bragança
        1050200, // Castelo Branco
        1060300, // Coimbra
        1070500, // Évora
        1080500, // Faro
        1090700, // Guarda
        1100900, // Leiria
        1110600, // Lisboa
        1121400, // Portalegre
        1131200, // Porto
        1141600, // Santarém
        1151200, // Setúbal
        1160900, // Viana do Castelo
        1171400, // Vila Real
        1182300, // Viseu
        2310300, // Funchal (Madeira)
        3410100  // Ponta Delgada (Açores)
    };

    
    public static int getRandomCityId() {
        Random random = new Random();
        int randomIndex = random.nextInt(cityIds.length); 
        return cityIds[randomIndex]; 
    }

    public static void main(String[] args) {
        int randomCityId = getRandomCityId();
        System.out.println("ID de cidade aleatório selecionado: " + randomCityId);
    }
}