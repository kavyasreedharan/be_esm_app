package com.management.employee.esmapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.management.employee.esmapp.controller.EmployeeDataController;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeDataControllerTest {
	
	Logger logger = LoggerFactory.getLogger(EmployeeDataController.class);

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Test
	public void userDataUploadSuccessTest() {
       try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data1.csv")){
    	   MockMultipartFile file = new MockMultipartFile("file", uploadStream);
   		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), file.getContentType(), file.getBytes());
   		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
   		
   		mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isOk());
       } catch(Exception e) {
    	   logger.error(e.getMessage());
       }
	}
	
	@Test
	public void newAndExistingDataTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data2.csv")){
	    	   MockMultipartFile file = new MockMultipartFile("file", uploadStream);
	   		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), file.getContentType(), file.getBytes());
	   		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   		
	   		mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isOk());
	       } catch(Exception e) {
	    	   logger.error(e.getMessage());
	       }
	}
	
	@Test
	public void emptyDataTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data3.csv")){
	    	   MockMultipartFile file = new MockMultipartFile("file", uploadStream);
	   		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), file.getContentType(), file.getBytes());
	   		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   		
	   		mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isNotAcceptable());
	       } catch(Exception e) {
	    	   logger.error(e.getMessage());
	       }
	}
	
	@Test
	public void incorrectFileTypeTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data4.txt")){
	    	   MockMultipartFile file = new MockMultipartFile("file", uploadStream);
	   		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), file.getContentType(), file.getBytes());
	   		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   		
	   		mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isUnsupportedMediaType());
	       } catch(Exception e) {
	    	   logger.error(e.getMessage());
	       }
	}
	
	@Test
	public void partialRowDataTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data5.csv")){
	    	   MockMultipartFile file = new MockMultipartFile("file", uploadStream);
	   		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), file.getContentType(), file.getBytes());
	   		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   		
	   		mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isNotAcceptable());
	       } catch(Exception e) {
	    	   logger.error(e.getMessage());
	       }
	}
	
	@Test
	public void tooManyDataInRowTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data6.csv")){
	    	   MockMultipartFile file = new MockMultipartFile("file", uploadStream);
	   		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), file.getContentType(), file.getBytes());
	   		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   		
	   		mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isNotAcceptable());
	       } catch(Exception e) {
	    	   logger.error(e.getMessage());
	       }
	}
	
	@Test
	public void negativeSalaryTest() throws Exception {
		try( InputStream uploadStream = EmployeeDataControllerTest.class.getClassLoader().getResourceAsStream("sample-data7.csv")){
	    	   MockMultipartFile file = new MockMultipartFile("file", uploadStream);
	   		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), file.getContentType(), file.getBytes());
	   		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   		
	   		mockMvc.perform(multipart("/users/upload").file(multipartFile)).andExpect(status().isNotAcceptable());
	       } catch(Exception e) {
	    	   logger.error(e.getMessage());
	       }
	}


}
