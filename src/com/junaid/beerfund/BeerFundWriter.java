package com.junaid.beerfund;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.JSONObject;
import com.junaid.beerfund.dynamo.BeerFundDeckDynamoDBReadWriter;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry;
import com.junaid.beerfund.dynamo.IDynamoDBReadWriter;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry.DynamoFieldNames;
import com.junaid.beerfund.utils.DynamoDBHelper;
import com.junaid.beerfund.utils.JSONHelper;
import com.junaid.beerfund.utils.LambdaLoggerHelper;

/**
 * Represents a BeerFundWriter lambda function for the Dynamo Table {@link BeerFundDeckEntry}
 * This updates the table using provided JSON input
 * Operation steps are as follows
 * 
 * 1. Read the table using the provided primaryKey + rangeKey
 * 2. Update the {@link BeerFundDeckEntry} instance if found with values from the input JSON
 * 3. Save this back into the DynamoDBTable backing {@link BeerFundDeckEntry}
 * 
 * @author Ershad
 */
public class BeerFundWriter implements RequestHandler<String, String> {

	private LambdaLoggerHelper logger;
	private AmazonDynamoDBClient client;
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
    			
    			JSONHelper.updateBeerFundDeckEntry(beerFundDeckEntry, input);
    			
    			// Write it
    			dynamoDBReadWriter.writeResource(beerFundDeckEntry);
    			
    			// Build a list which will have only 1 entry updated
    			beerFundDeckEntries = DynamoDBHelper.getAsList(beerFundDeckEntry);
    		}
    		
    		// If the list is not empty add an entry into the JSON object
    		if(beerFundDeckEntries != null) {
    			jsonObject.put("beerFundEntriesList", beerFundDeckEntries);
    		}
    	} catch (Exception e) {
        	logger.log("Caught exception when trying to update DynamoDB Table" 
        				+ e.getMessage());
        	return ErrorCodes.WRITE_ERROR;
    	}
    	
    	// Log the response payload for debugging purposes on cloudwatch logs
    	logger.log("Response payload: " + jsonObject.toString());
    	
    	// Return the json payload
    	return jsonObject.toString();
    }

}
