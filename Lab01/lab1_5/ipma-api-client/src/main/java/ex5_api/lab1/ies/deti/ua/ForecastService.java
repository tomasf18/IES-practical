package ex5_api.lab1.ies.deti.ua;

import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastService {

    private IpmaService service;

    public ForecastService() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.ipma.pt/open-data/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        
        service = retrofit.create(IpmaService.class);
    }

    public List<CityForecast> getForecastForCity(int cityId) throws Exception {
        Call<IpmaCityForecast> callSync = service.getForecastForACity(cityId);
        Response<IpmaCityForecast> apiResponse = callSync.execute();
        IpmaCityForecast forecast = apiResponse.body();

        if (forecast != null && forecast.getData() != null && !forecast.getData().isEmpty()) {
            return forecast.getData();
        } else {
            throw new Exception("No forecast data available for this city.");
        }
    }
}
