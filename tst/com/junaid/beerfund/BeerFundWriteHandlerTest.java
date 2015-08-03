package com.junaid.beerfund;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.lambda.runtime.Context;

/**
 * Tests for {@link BeerFundWriter}
 * @author Ershad
 */
public class BeerFundWriteHandlerTest {
    public Context ctx;
    public BeerFundWriter handler;

    @Before
    public void createInput() throws IOException {
    	ctx = new TestContext();
    	handler = new BeerFundWriter();
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
    	final String output = handler.handleRequest(
    			"{\"loginid\":\"junaid\",\"sub_team\":\"srx\",\"attended_meetings\":\"6\"}", ctx);
    	if(output != null) {
    		System.out.println(output.toString());
    	}
    }
    
    @Test
    public void testBeerFundHandlerForValidJsonInvalidUser() {
    	final String output = handler.handleRequest(
    			"{\"loginid\":\"foo\",\"sub_team\":\"baz\",\"attended_meetings\":\"6\"}", ctx);
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
