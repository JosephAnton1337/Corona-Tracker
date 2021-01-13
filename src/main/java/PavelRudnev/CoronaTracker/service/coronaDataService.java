package PavelRudnev.CoronaTracker.service;

import PavelRudnev.CoronaTracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class coronaDataService {

  private static String Virus_Data_URl = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv" ;



    private List<LocationStats>allStats = new ArrayList<>();


    public List<LocationStats> getAllStats() {
        return allStats;

    }



@PostConstruct
@Scheduled(cron = "* * 1 * * *")

public  void fetchVirusData() throws IOException, InterruptedException {

     List<LocationStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();//создаем новый https слиент
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Virus_Data_URl))
                .build();//обрабатываем наши входные данные через https запрос
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());//ответ выведенный по нашему запросу
    StringReader csvBodyReader = new StringReader(httpResponse.body());
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);//взятый пример из документациии апаче для чтения  csv файлов
    for (CSVRecord record : records) {
        LocationStats locationstats = new LocationStats();//перменная для создания всех полей
        locationstats.setState (record.get("Province/State"));
        locationstats.setCountry(record.get("Country/Region"));
        locationstats.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
        newStats.add(locationstats);

    }
    this.allStats=newStats;
    }
}
