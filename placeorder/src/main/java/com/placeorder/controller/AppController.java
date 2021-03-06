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
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	JSONParser orderParser;
	
	@Autowired
	ObjectMapper orderMapper;
	
	@RequestMapping(value = "/orders", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public List<OrderDetails> getOrderDetails(HttpServletRequest request) {
		List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();

		String jsonFilePath = request.getSession().getServletContext().getRealPath("/config/orderdetails.json");
		try {
			Object existingOrder = orderParser.parse(new FileReader(jsonFilePath));

			JSONArray orders = (JSONArray) existingOrder;
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

	@RequestMapping(value = "/order/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public JSONObject getOrderDetailsById(HttpServletRequest request, @PathVariable int id) {
		List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
		JSONObject order = new JSONObject();

		String jsonFilePath = request.getSession().getServletContext().getRealPath("/config/orderdetails.json");
		try {
			Object existingOrder = orderParser.parse(new FileReader(jsonFilePath));

			JSONArray orders = (JSONArray) existingOrder;
			orderDetails.addAll(orders);

			Iterator<JSONObject> orderItr = orders.iterator();
			while (orderItr.hasNext()) {
				JSONObject tempOrder = new JSONObject();
				tempOrder = (JSONObject) orderItr.next();
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

	@RequestMapping(value = "/freshorder", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
	@ResponseBody
	public void updateOrderDetails(@RequestBody String json, HttpServletRequest request) {

		String jsonFilePath = request.getSession().getServletContext().getRealPath("/config/orderdetails.json");
		File file = new File(jsonFilePath);

		try {
			Object existingOrder = orderParser.parse(new FileReader(jsonFilePath));
			JSONArray orders = (JSONArray) existingOrder;
			JSONArray inputOrder = (JSONArray) orderParser.parse(json);

			OrderDetails orderDetails = orderMapper.readValue(inputOrder.get(0).toString(), OrderDetails.class);
			if (orders.toJSONString().indexOf(String.valueOf(orderDetails.getOrderId())) == -1)
				orders.add(orderDetails);
			orderMapper.writeValue(new FileWriter(file.getAbsoluteFile(), false), orders);

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

	@RequestMapping(value = "/deleteorder/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	@ResponseBody
	public List<OrderDetails> deleteOrderDetailsById(HttpServletRequest request, @PathVariable int id) {
		List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
				
		String jsonFilePath = request.getSession().getServletContext().getRealPath("/config/orderdetails.json");
		
		File file = new File(jsonFilePath);
		
		try {
			Object existingOrder = orderParser.parse(new FileReader(jsonFilePath));

			JSONArray orders = (JSONArray) existingOrder;
			orderDetails.addAll(orders);
			
			Iterator<JSONObject> orderItr = orders.iterator();
			while (orderItr.hasNext()) {
				JSONObject tempOrder = new JSONObject();
				tempOrder = (JSONObject) orderItr.next();
				if (tempOrder.toString().contains(String.valueOf(id)))
					orderDetails.remove(tempOrder);
			}
			
			orderMapper.writeValue(new FileWriter(file.getAbsoluteFile(), false), orderDetails);
			

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
	
	@RequestMapping(value = "/updateorder/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	public List<OrderDetails> updateOrderDetailsById(HttpServletRequest request, @PathVariable int id, @RequestBody String json) {
		
		List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
		OrderDetails orderDet=null;
		String jsonFilePath = request.getSession().getServletContext().getRealPath("/config/orderdetails.json");
				
		File file = new File(jsonFilePath);
		
		try {
			JSONArray inputOrder = (JSONArray) orderParser.parse(json);
			Object existingOrder = orderParser.parse(new FileReader(jsonFilePath));

			JSONArray orders = (JSONArray) existingOrder;
			orderDetails.addAll(orders);
			
			Iterator<JSONObject> orderItr = orders.iterator();
			while (orderItr.hasNext()) {
				JSONObject tempOrder = new JSONObject();
				tempOrder = (JSONObject) orderItr.next();
				if (tempOrder.toString().contains(String.valueOf(id))&&inputOrder.get(0).toString().contains(String.valueOf(id))){
					 orderDet = orderMapper.readValue(inputOrder.get(0).toString(), OrderDetails.class);
					 orderDetails.remove(tempOrder);
					 orderDetails.add(orderDet);
				}
					
			}
			
			orderMapper.writeValue(new FileWriter(file.getAbsoluteFile(), false), orderDetails);
			

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
}