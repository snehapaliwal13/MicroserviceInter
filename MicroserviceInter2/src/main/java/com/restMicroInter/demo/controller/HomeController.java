package com.restMicroInter.demo.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.restMicroInter.demo.model.User;
import com.restMicroInter.demo.service.UserService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

/**
 * @author sneha.paliwal
 *
 */
@RestController
public class HomeController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired 
	private EurekaClient eurekaClient; 

	@Autowired
	UserService userService;

	/*
	 * @Value("${service.MicroserviceCrud2.serviceId}") private String serviceId;
	 */

	@GetMapping(path = "/inter", produces = "application/json", consumes = "application/json")
	public HashMap<Integer,Integer> getTotalBalance() {
		
		InstanceInfo instanceInfo=eurekaClient.getNextServerFromEureka("MICROSERVICECRUD2",false);
		String baseUrl=instanceInfo.getHomePageUrl();
		System.out.println("BaseURL" + baseUrl);
		String url=baseUrl+"/users";
		System.out.println(url);
		ParameterizedTypeReference<List<User>> typeRef=new
				ParameterizedTypeReference<List<User>>() { }; ResponseEntity<List<User>>
				response=restTemplate.exchange(url, HttpMethod.GET, getHeaders(),typeRef);
				List<User> userBody=(List<User>) response.getBody();
				HashMap<Integer,Integer> resultMap=userService.getTotalBalanceForUser(userBody);
				return resultMap;
	}

	private HttpEntity<Object> getHeaders() {
		HttpHeaders headers=new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(headers);
	}

}
