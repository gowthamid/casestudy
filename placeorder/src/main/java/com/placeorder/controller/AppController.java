package com.placeorder.controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placeorder.model.OrderDetails;


@Controller
public class AppController {
	@RequestMapping(value = "/test", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public List<OrderDetails> getOrderDetails(HttpServletRequest request) {
		List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
		JSONParser parser = new JSONParser();

		String jsonFilePath = request.getSession().getServletContext().getRealPath("/config/orderdetails.json");
		try {
			Object object = parser.parse(new FileReader(jsonFilePath));

			JSONArray orders = (JSONArray) object;
			orderDetails.addAll(orders);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderDetails;
	}

	@RequestMapping(value = "/test/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public JSONObject getOrderDetailsById(HttpServletRequest request, @PathVariable int id) {
		List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
		JSONParser parser = new JSONParser();
		JSONObject order = new JSONObject();

		String jsonFilePath = request.getSession().getServletContext().getRealPath("/config/orderdetails.json");
		try {
			Object object = parser.parse(new FileReader(jsonFilePath));

			JSONArray orders = (JSONArray) object;
			orderDetails.addAll(orders);

			Iterator<JSONObject> itr = orders.iterator();
			while (itr.hasNext()) {
				JSONObject tempOrder = new JSONObject();
				tempOrder = (JSONObject) itr.next();
				if (tempOrder.toString().contains(String.valueOf(id)))
					order = tempOrder;
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return order;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
	@ResponseBody
	public void updateOrderDetails(@RequestBody String json, HttpServletRequest request) {

		String jsonFilePath = request.getSession().getServletContext().getRealPath("/config/orderdetails.json");
		File file = new File(jsonFilePath);
		JSONParser parser = new JSONParser();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Object object = parser.parse(new FileReader(jsonFilePath));
			JSONArray orders = (JSONArray) object;
			JSONArray jsonObject = (JSONArray) parser.parse(json);

			OrderDetails orderDetails = objectMapper.readValue(jsonObject.get(0).toString(), OrderDetails.class);
			if (orders.toJSONString().indexOf(String.valueOf(orderDetails.getOrderId())) == -1)
				orders.add(orderDetails);
			objectMapper.writeValue(new FileWriter(file.getAbsoluteFile(), false), orders);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}