package edu.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
public class ElasticConfig {

    @Value("${elasticsearch.host:localhost}")
    public String host;
    @Value("${elasticsearch.port:9200}")
    public int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Bean
    public RestHighLevelClient client() {
        RestHighLevelClient client = null;
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, 9200, "http")));
    }
}
