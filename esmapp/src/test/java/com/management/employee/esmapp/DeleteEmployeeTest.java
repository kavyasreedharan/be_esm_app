package com.management.employee.esmapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteEmployeeTest {

	Logger logger = LoggerFactory.getLogger(DeleteEmployeeTest.class);
	public static final String fileType = "text/csv";

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	public void insertDataToDb() {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data1.csv")){
			MockMultipartFile file = new MockMultipartFile("file", uploadStream);
			MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), fileType, file.getBytes());
			MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

			mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isOk());
		} catch(Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Test
	public void deleteEmployeeSuccessTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(delete("/users/{id}", "e0001"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.OK.value()))
		.andExpect(jsonPath("$.message").value("Employee data deleted successfully"));
	}

	@Test
	public void employeeIdNotExistTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(delete("/users/{id}", "e0011"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.responseCode").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.message").value("Employee data does not exists with this id."));
	}

	@Test
	public void employeeIdInvalidTest() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(delete("/users/"))
		.andExpect(status().isNotFound());
	}

	public static String convertToJson(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
