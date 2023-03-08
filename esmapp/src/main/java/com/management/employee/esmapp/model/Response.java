package com.management.employee.esmapp.model;

import lombok.Data;

/**
 * 
 * @author Kavya Sreedharan
 * This is the model class for sending API response
 *
 */

@Data
public class Response {

	private int responseCode;
	
	private String message;
	
	public Response() {
	}
	
	public Response(int responseStatusCode, String message) {
		this.responseCode = responseStatusCode;
		this.message = message;
	}
}
