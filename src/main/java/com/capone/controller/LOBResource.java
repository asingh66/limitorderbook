package com.capone.controller;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.capone.lob.model.ExecutionResult;
import com.capone.lob.model.TradeOrder;
import com.capone.lob.service.InvalidOrderException;
import com.capone.lob.service.LimitOrderNotFoundException;
import com.capone.lob.service.LimitOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@Api(value = "Limit Order Book", description = "Endpoint for managing Limit Order Book")
@RestController
@RequestMapping("/api")
public class LOBResource {

	@Autowired
	LimitOrderService orderService;

	@ApiOperation(value="Api to place a new Order",notes="Use to place new orders")
	@RequestMapping(value = "/orders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TradeOrder> createOrder(@ApiParam(value="Parameters to create order") TradeOrder o) throws InvalidOrderException {
		
		//validate and create order, if validation fails, send BAD_REQUEST code
		orderService.createOrder(o);
		String url = "/api/orders/" + o.getId();
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path(url).build().toUri();
		headers.setLocation(location);		
		ResponseEntity <TradeOrder> response = new ResponseEntity<>(o, headers,HttpStatus.CREATED);
		return response;
	}
	
	@ApiOperation(value="Api to cancel an order",notes="Use to cancel an existing order")
	@RequestMapping(value = "/orders/{id}/cancel", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> cancelOrder( @ApiParam(value="Id to existing order to cancel") @PathVariable Long id) throws InvalidOrderException, LimitOrderNotFoundException {
		TradeOrder o = orderService.findOrder(id);
		if (o != null) {
			orderService.cancelOrder(o);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} else {
			//send a 404 error to client
			throw new LimitOrderNotFoundException("Order id :" + id + " not found in the system");
		}
	}
	
	@ApiOperation(value="Api to get all open orders",notes="Use to get all NEW and PARTIAL_MATCH orders")	
	@RequestMapping(value = "/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<TradeOrder>> getOrders() throws InvalidOrderException, LimitOrderNotFoundException {
		Iterable <TradeOrder> orders = orderService.findOrders();
		return new ResponseEntity<Iterable<TradeOrder>>(orderService.findOrders(),HttpStatus.OK);
		
	}
	
	@ApiOperation(value="Api to get an existing order",notes="Use to get an existing order")
	@RequestMapping(value = "/orders/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TradeOrder getOrder( @ApiParam (value="Id of existing order") @PathVariable Long id) throws InvalidOrderException, LimitOrderNotFoundException {
		TradeOrder o = orderService.findOrder(id);
		if (o == null) {
			//send a 404 error to client
			throw new LimitOrderNotFoundException("Order id :" + id + " not found in the system");
		} else {
			return o;		
		}
	}
	
	@ApiOperation(value="Api to execute/match all existing order",notes="Use to execute all orders in the system")
	@RequestMapping(value = "/orders/execute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ExecutionResult> executeOrders()  {
		return orderService.executeOrders();
	}
	
	@ApiResponses( value = {@ApiResponse(code=200, message="Orders executed")})
	@ApiOperation(value="Api to execute/match all existing order of a Symbol",notes="Use to execute all orders of a symbol in the system")
	@RequestMapping(value = "/orders/execute/{symbol}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ExecutionResult executeOrder(@ApiParam (value="Stock Symbol") @PathVariable String symbol)  {
		return orderService.executeOrders(symbol);
	}
	
	

}
