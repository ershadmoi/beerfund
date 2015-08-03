package com.junaid.beerfund;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.lambda.runtime.Context;

/**
 * Tests for {@link BeerFundReader}
 * @author Ershad
 */
public class BeerFundReadHandlerTest {
    public Context ctx;
    public BeerFundReader handler;

    @Before
    public void createInput() throws IOException {
    	ctx = new TestContext();
    	handler = new BeerFundReader();
    	handler.setDynamoDBClient(new AmazonDynamoDBClient(new ProfileCredentialsProvider()));
    }

    @Test
    public void testBeerFundHandlerForEmptyInput() {
        final String output = handler.handleRequest("", ctx);
        if (output != null) {
            System.out.println(output.toString());
        }
    }
    
    @Test
    public void testBeerFundHandlerForValidJson() {
    	final String output = handler.handleRequest("{\"loginid\":\"junaid\",\"sub_team\":\"srx\"}", ctx);
    	if(output != null) {
    		System.out.println(output.toString());
    	}
    }
    
    @Test
    public void testBeerFundHandlerForEmptyValidJson() {
    	final String output = handler.handleRequest("{}", ctx);
    	if(output != null) {
    		System.out.println(output.toString());
    	}
    }
    
    @Test
    public void testBeerFundHandlerForInvalidJson() {
    	final String output = handler.handleRequest("{", ctx);
    	if(output != null) {
    		System.out.println(output.toString());
    	}
    }
}
