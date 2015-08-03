package com.junaid.beerfund.utils;

import java.util.StringTokenizer;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.junaid.beerfund.dynamo.BeerFundDeckDynamoDBReadWriter;
import com.junaid.beerfund.dynamo.BeerFundDeckEntry;
import com.junaid.beerfund.dynamo.IDynamoDBReadWriter;

/**
 * A simple java function that reuses the lambda code 
 * to initialize a beer_fund table for a team
 * @author Ershad
 */
public class InitializeTableForTeam {
	
	// Static variables
	private static String helpMessage;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("HELP\n----\n\n");
		sb.append("Run using : java InitializeTableForTeam <csv of user ids> <sub_team_name>\n\n");
		sb.append("NOTE : sub_team_name can be your own team name. this is just to allow if tomorrow there is a future sub team.\n\n");
		helpMessage = sb.toString();
	}
	
	// dynamoDBReadWriter for BeerFund
	private static IDynamoDBReadWriter<BeerFundDeckEntry> dynamoDBReadWriter = 
			new BeerFundDeckDynamoDBReadWriter(new AmazonDynamoDBClient(new ProfileCredentialsProvider()));
		
	public static void main(String args[]) {
		if(args.length >= 2)  {
			StringTokenizer st = new StringTokenizer(args[0], ",");
			while(st.hasMoreTokens()) {
				initializeRowEntry(st.nextToken(), args[1]);
			}
		} else {
			System.err.println("Cannot initialize table for team not enough args.");
			printHelp();
		}
	}
	
	/**
	 * Helper method which prints help
	 */
	public static void printHelp() {
		System.out.println(helpMessage);
	}
	
	/**
	 * Initializes a row entry even if it exists in the BeerFund table with defaults.
	 * Look at {@link BeerFundDeckEntry} to get an idea of the DynamoTable this is using
	 * @param userId			The loginid - primary key in the table
	 * @param sub_team_name		The sub_team_name - range key in the table
	 */
	public static void initializeRowEntry(final String userId, final String sub_team_name) {
		try {
			BeerFundDeckEntry beerFundDeckEntry = new BeerFundDeckEntry();
			JSONHelper.updateBeerFundDeckEntry(beerFundDeckEntry,
	    			"{\"loginid\":\"" + userId + "\",\"sub_team\":\"" + sub_team_name + "\",\"attended_meetings\":\"0\"}");
			dynamoDBReadWriter.writeResource(beerFundDeckEntry);
		} catch (Exception e) {
			System.err.println("Error : " + e.getMessage());
			e.printStackTrace();
		}
		
	}
}
