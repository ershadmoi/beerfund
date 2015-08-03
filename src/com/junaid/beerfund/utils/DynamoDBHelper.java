package com.junaid.beerfund.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry.DynamoFieldNames;

/**
 * Helper class for performing simple operations on {@link BeerFundDeckEntry} backing DynamoDB table
 * @author Ershad
 */
public class DynamoDBHelper {
	/**
	 * Reads an entire table using dynamo table scan operation
	 * @param client		The backing {@link AmazonDynamoDBClient} for this operation
	 * @param tableName		A {@link String} representing the table to be scanned
	 * @return				A {@link ScanResult} instance from performing a table scan
	 */
	public static ScanResult readTable(final AmazonDynamoDBClient client, final String tableName) {
		if(client == null) return null;
		
		if(tableName != null && tableName != "") {
			ScanRequest scanRequest = new ScanRequest().withTableName(tableName);
			ScanResult scanResult = client.scan(scanRequest);
			return scanResult;
		} else {
			return null;
		}
	}
	
	/**
	 * Creates a {@link BeerFundDeckEntry} instance using an AttributeMap
	 * @param attributeMap		The {@link Map} backing a {@link List} of {@link ScanResult}
	 * @return					A {@link BeerFundDeckEntry} instance
	 */
	public static BeerFundDeckEntry createBeerFundDeckEntryFromAttributeMap(
			Map<String, AttributeValue> attributeMap) {
		try {
			BeerFundDeckEntry beerFundDeckEntry = new BeerFundDeckEntry();
			beerFundDeckEntry.setLoginId(attributeMap.get(DynamoFieldNames.loginid).getS());
			beerFundDeckEntry.setSubTeamName(attributeMap.get(DynamoFieldNames.sub_team).getS());
			beerFundDeckEntry.setNumberOfMeetingsAttended(
					Long.valueOf(attributeMap.get(DynamoFieldNames.attended_meetings).getN()));
			beerFundDeckEntry.setNumberOfMeetingsMissed(
					Long.valueOf(attributeMap.get(DynamoFieldNames.missed_meetings).getN()));
			beerFundDeckEntry.setTotalNumberOfMeetings(
					Long.valueOf(attributeMap.get(DynamoFieldNames.total_meetings).getN()));
			beerFundDeckEntry.setLastDepositAmount(
					Float.valueOf(attributeMap.get(DynamoFieldNames.last_deposit_amount).getN()));
			beerFundDeckEntry.setRemainingBalanceAmount(
					Float.valueOf(attributeMap.get(DynamoFieldNames.remaining_balance).getN()));
			beerFundDeckEntry.setStarRating(
					Float.valueOf(attributeMap.get(DynamoFieldNames.star_rating).getN()));
			return beerFundDeckEntry;
		} catch (Exception e) {
			LambdaLoggerHelper.getLoggerInstance()
				.log("Error creatingBeerFundDeckEntry from AttributeMap with message : " 
						+ e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * A small helper utility to return a null-clean single item list from a {@link BeerFundDeckEntry} instance 
	 * @param beerFundDeckEntry		The instance to build a list from
	 * @return						A {@link List} of {@link BeerFundDeckEntry} containing the provided input provided its not null
	 */
	public static List<BeerFundDeckEntry> getAsList(final BeerFundDeckEntry beerFundDeckEntry) {
		if(beerFundDeckEntry == null) {
			return Arrays.asList();
		} else {
			return Arrays.asList(beerFundDeckEntry);	
		}
	}
}
