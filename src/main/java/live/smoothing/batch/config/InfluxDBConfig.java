package live.smoothing.batch.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import live.smoothing.batch.prop.AggregationInfluxDBProperties;
import live.smoothing.batch.prop.RawInfluxDBProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InfluxDBConfig {

    private final AggregationInfluxDBProperties aggregationInfluxDBProperties;
    private final RawInfluxDBProperties rawInfluxDBProperties;

    /**
     *
     * @return 집계를 담당하는 InfluxDBClient를 반환
     */
    @Bean
    public InfluxDBClient aggregationInfluxClient() {
        return InfluxDBClientFactory
                .create(
                        aggregationInfluxDBProperties.getUrl(),
                        aggregationInfluxDBProperties.getToken().toCharArray(),
                        aggregationInfluxDBProperties.getOrg()
                );
    }

    /**
     *
     * @return Raw 데이터를 담당하는 InfluxDBClient를 반환
     */
    @Bean
    public InfluxDBClient rawInfluxClient() {
        return InfluxDBClientFactory
                .create(
                        rawInfluxDBProperties.getUrl(),
                        rawInfluxDBProperties.getToken().toCharArray(),
                        rawInfluxDBProperties.getOrg()
                );
    }
}