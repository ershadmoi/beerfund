package com.junaid.beerfund;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.JSONObject;
import com.junaid.beerfund.dynamo.BeerFundDeckDynamoDBReadWriter;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry.DynamoFieldNames;
import com.junaid.beerfund.dynamo.IDynamoDBReadWriter;
import com.junaid.beerfund.utils.DynamoDBHelper;
import com.junaid.beerfund.utils.JSONHelper;
import com.junaid.beerfund.utils.LambdaLoggerHelper;

/**
 * Represents a BeerFundReader lambda function for the Dynamo Table {@link BeerFundDeckEntry}
 * This reads the table using provided JSON input
 * Operation steps are as follows
 * 
 * 1. If no input is specified runs a scan operation on the entire table backing {@link BeerFundDeckDynamoDBReadWriter}
 * 2. If input is provided then read the table using the provided primaryKey + rangeKey (if present)
 * 3. Return the read input as a jsonList mapped to an entry beerFundEntries.
 * 
 * The list can either be of 
 * 		size 0
 * 		size 1 if valid primaryKey + rangeKey are provided
 *  	size n if no input is provided => A table scan is performed
 * 
 * @author Ershad
 */
public class BeerFundReader implements RequestHandler<String, String> {
	
	// lambda logger helper
	private LambdaLoggerHelper logger;
	
	// A custom client if required
	private AmazonDynamoDBClient client;
	
	// Setter for the above client
	public void setDynamoDBClient(final AmazonDynamoDBClient client) { this.client = client; }

    @Override
    public String handleRequest(String input, Context context) {
    	// Initialize logger for request context
    	LambdaLoggerHelper.initialize(context.getLogger());
    	logger = LambdaLoggerHelper.getLoggerInstance();
    	
    	// Log the input just for debugging purposes on cloudwatch logs
    	logger.log("Input: " + input);
    	
    	// Create a new jsonObject which will be used to generate the payload
    	JSONObject jsonObject = new JSONObject();
    	List<BeerFundDeckEntry> beerFundDeckEntries = null;
    	try {
    		// Fetch any primary/range keys in the input
    		Map<String, String> queryKeys = JSONHelper.getPrimaryAndHashKeys(input);
    		
    		// DynamoReadWriter reference instance
    		IDynamoDBReadWriter<BeerFundDeckEntry> dynamoDBReadWriter = 
    				client ==  null ? new BeerFundDeckDynamoDBReadWriter() :
    					new BeerFundDeckDynamoDBReadWriter(client);
    		
    		// If we have any primary/hash keys in the input
    		if(queryKeys != null) {
    			// Read the specified resource
    			BeerFundDeckEntry beerFundDeckEntry = dynamoDBReadWriter.readResource(
    					BeerFundDeckEntry.class, 
    					queryKeys.get(DynamoFieldNames.loginid), 
    					queryKeys.get(DynamoFieldNames.sub_team)
    			);
    			
    			// Build a list which will have only 1 entry read
    			beerFundDeckEntries = DynamoDBHelper.getAsList(beerFundDeckEntry);
    		} else { // Otherwise scan the whole table
    			beerFundDeckEntries = dynamoDBReadWriter.readTable();
    		}
    		
    		// If the list is not empty add an entry into the JSON object
    		if(beerFundDeckEntries != null) {
    			jsonObject.put("beerFundEntriesList", beerFundDeckEntries);
    		}
    	} catch (Exception e) {
        	logger.log("Caught exception when trying to read from DynamoDB Table" 
        				+ e.getMessage());
        	return ErrorCodes.READ_ERROR;
    	}
    	
    	// Log the response payload for debugging purposes on cloudwatch logs
    	logger.log("Response payload: " + jsonObject.toString());
    	
    	// Return the json payload
    	return jsonObject.toString();
    }
}
