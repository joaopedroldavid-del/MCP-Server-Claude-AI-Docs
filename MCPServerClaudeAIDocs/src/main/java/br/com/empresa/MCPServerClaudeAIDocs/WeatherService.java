package br.com.empresa.MCPServerClaudeAIDocs;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

    private final RestClient restClient;

    public WeatherService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.weather.gov")
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)")
                .build();
    }

    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(
            @ToolParam(description = "Latitude in decimal degrees") double latitude,
            @ToolParam(description = "Longitude in decimal degrees") double longitude
    ) {
        String pointsUrl = String.format("/points/%f,%f", latitude, longitude);
        var pointsResponse = restClient.get()
                .uri(pointsUrl)
                .retrieve()
                .body(String.class);

        return pointsResponse;
    }

    @Tool(description = "Get weather alerts for a US state")
    public String getAlerts(
            @ToolParam(description = "Two-letter US state code (e.g. CA, NY)") String state
    ) {
        String alertsUrl = String.format("/alerts/active?area=%s", state);
        return restClient.get()
                .uri(alertsUrl)
                .retrieve()
                .body(String.class);
    }
}
