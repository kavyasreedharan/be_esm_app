package com.management.employee.esmapp.model;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * 
 * @author Kavya Sreedharan
 * This is the model class for sending API response
 *
 */

@Data
public class Response {

	private HttpStatus responseCode;
	
	private String message;
	
	public Response() {
	}
	
	public Response(HttpStatus responseStatusCode, String message) {
		this.responseCode = responseStatusCode;
		this.message = message;
	}
}
