package dungnm243.SmartServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SmartServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartServerApplication.class, args);
    }

    @Bean
    public WebClient client() {
        return WebClient.builder()
                .baseUrl("http://127.0.0.1:5000")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
                                                            