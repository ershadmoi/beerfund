package com.junaid.beerfund.utils;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry.DynamoFieldNames;

/**
 * A Helper class of static functions to deal with JSON operations
 * @author Ershad
 */
public class JSONHelper {
	
	/**
	 * Gets the primary and range keys from the input if a valid JSON string is provided
	 * @param input 	A valid {@link JSONObject} string
	 * @return			A {@link Map} containing the primary and secondary keys for {@link BeerFundDeckEntry}
	 */
	public static Map<String, String> getPrimaryAndHashKeys(final String input) {
    	Map<String, String> primaryAndHashKeys = null;
    	if(input != null && input != "") {
    		try {
    			primaryAndHashKeys = new HashMap<String, String>(2);
    			JSONObject jsonObject = new JSONObject(input);
    			
    			if(jsonObject.has(DynamoFieldNames.loginid)) {
    				primaryAndHashKeys.put(DynamoFieldNames.loginid, 
    						jsonObject.getString(DynamoFieldNames.loginid));
    			}
    			
    			if(jsonObject.has(DynamoFieldNames.sub_team)) {
    				primaryAndHashKeys.put(DynamoFieldNames.sub_team, 
    						jsonObject.getString(DynamoFieldNames.sub_team));
    			}
    		} catch (JSONException e) {
    			LambdaLoggerHelper.getLoggerInstance()
    				.log("Error parsing input to generate JSON with error msg : " 
    						+ e.getMessage());
    		}
    	}
		
    	return primaryAndHashKeys;
    }
	
	/**
	 * Updates a {@link BeerFundDeckEntry} instance with existent JSON keys from the input string.
	 * The {@link DynamoFieldNames} is used to infer the JSON keys for lookup
	 * @param beerFundDeckEntry		The instance to be updated values from JSON input with
	 * @param input					A json string
	 * @return						True provided the update is successful. False otherwise.
	 */
	public static boolean updateBeerFundDeckEntry(final BeerFundDeckEntry beerFundDeckEntry, final String input) {
		if(input != null && input != "") {
    		try {
    			JSONObject jsonObject = new JSONObject(input);
				if(jsonObject.has(DynamoFieldNames.loginid)) {
					beerFundDeckEntry.setLoginId(jsonObject.getString(DynamoFieldNames.loginid));
				}
				
				if(jsonObject.has(DynamoFieldNames.sub_team)) {
					beerFundDeckEntry.setSubTeamName(jsonObject.getString(DynamoFieldNames.sub_team));
				}
				
				if(jsonObject.has(DynamoFieldNames.attended_meetings)) {
					beerFundDeckEntry.setNumberOfMeetingsAttended(
							Long.valueOf(jsonObject.getString(DynamoFieldNames.attended_meetings)));
				}
				
				if(jsonObject.has(DynamoFieldNames.missed_meetings)) {
					beerFundDeckEntry.setNumberOfMeetingsMissed(
							Long.valueOf(jsonObject.getString(DynamoFieldNames.missed_meetings)));
				}
				
				if(jsonObject.has(DynamoFieldNames.total_meetings)) {
					beerFundDeckEntry.setTotalNumberOfMeetings(
							Long.valueOf(jsonObject.getString(DynamoFieldNames.total_meetings)));
				}
				
				if(jsonObject.has(DynamoFieldNames.last_deposit_amount)) {
					beerFundDeckEntry.setLastDepositAmount(
							Float.valueOf(jsonObject.getString(DynamoFieldNames.last_deposit_amount)));
				}
				
				if(jsonObject.has(DynamoFieldNames.remaining_balance)) {
					beerFundDeckEntry.setRemainingBalanceAmount(
							Float.valueOf(jsonObject.getString(DynamoFieldNames.remaining_balance)));
				}
				
				if(jsonObject.has(DynamoFieldNames.star_rating)) {
					beerFundDeckEntry.setStarRating(
							Float.valueOf(jsonObject.getString(DynamoFieldNames.star_rating)));
				}
				
				return true;
    		} catch (JSONException e) {
    			LambdaLoggerHelper.getLoggerInstance()
				.log("Error parsing input to generate JSON with error msg : " 
						+ e.getMessage());
    		} catch (Exception e) {
    			LambdaLoggerHelper.getLoggerInstance()
				.log("Error updating BeerFundDeckEntry from JSON input with message : " 
						+ e.getMessage());
    		}
		}
		
		return false;
	}
}
