package com.capone;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.capone.lob.model.ExecutionResult;
import com.capone.lob.model.OrderCategoryEnum;
import com.capone.lob.model.OrderTypeEnum;
import com.capone.lob.model.TradeOrder;
import com.jayway.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LimitOrderBookRestTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${local.server.port}") // 6
	int port;

	String baseUrl;

	@Before
	public void setUp() {
		RestAssured.port = port;
		baseUrl = "http://localhost:" + port + "/capone/api/orders";
	}

	@Test
	public void createOrder() {
		MultiValueMap map = createOrderMap();
		TradeOrder result = restTemplate.postForObject(baseUrl, map, TradeOrder.class);
		assertEquals(result.getId().longValue(), 1L);

	}

	@Test
	public void getOrder() {

		ResponseEntity<String> response = this.restTemplate.getForEntity(baseUrl + "/{id}", String.class, "1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);

	}

	@Test
	public void getInvalidOrder() {

		ResponseEntity<String> response = this.restTemplate.getForEntity(baseUrl + "/{id}", String.class, "100");
		System.out.println(response.getBody());

		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);

	}

	MultiValueMap createOrderMap() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("orderCategory", "LIMIT");
		map.add("orderType", "BID");
		map.add("quantity", "100");
		map.add("price", "20");
		map.add("symbol", "AAPL");
		return map;

	}

}
