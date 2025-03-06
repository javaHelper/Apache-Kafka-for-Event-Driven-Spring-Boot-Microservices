package com.appsdeveloperblog.ws.emailnotification.handler;

import com.appsdeveloperblog.ws.emailnotification.error.NotRetryableException;
import com.appsdeveloperblog.ws.emailnotification.error.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics="products-created-events-topic")
public class ProductCreatedEventHandler {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restTemplate;

	public ProductCreatedEventHandler(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}

	@KafkaHandler
	public void handle(ProductCreatedEvent productCreatedEvent) {
		LOGGER.info("Received a new event: {}", productCreatedEvent);

		String requestUrl = "http://localhost:8082/response/200";

		try {
			ResponseEntity<String> responseEntity =
					restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

			if(responseEntity.getStatusCode().value() == HttpStatus.OK.value()){
				LOGGER.info("Received Response from remote service :{}", responseEntity.getBody());
			}
		}catch (ResourceAccessException ex){
			LOGGER.error(ex.getMessage());
			throw new RetryableException(ex);
		} catch (HttpServerErrorException ex) {
			LOGGER.error(ex.getMessage());
			throw new NotRetryableException(ex);
		}catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new NotRetryableException(ex);
		}
	}
}