package com.junaid.beerfund.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

@DynamoDBTable(tableName="team_beer_fund")
public class BeerFundDeckEntry {
	public String loginId;
	public String subTeamName;
	public long numberOfMeetingsAttended;
	public long numberOfMeetingsMissed;
	public long totalNumberOfMeetings;
	public float lastDepositAmount;
	public float remainingBalanceAmount;
	public float starRating;
	
	@DynamoDBHashKey(attributeName="loginid")
	public String getLoginId() { return loginId; }
	public void setLoginId(final String loginId) { this.loginId = loginId; }
	
	@DynamoDBRangeKey(attributeName="sub_team")
	public String getSubTeamName() { return subTeamName; }
	public void setSubTeamName(final String subTeamName) { this.subTeamName = subTeamName; }
	
	@DynamoDBAttribute(attributeName="attended_meetings")
	public long getNumberOfMeetingsAttended() { return numberOfMeetingsAttended; }
	public void setNumberOfMeetingsAttended(final long numberOfMeetingsAttended) { this.numberOfMeetingsAttended = numberOfMeetingsAttended; }
	
	@DynamoDBAttribute(attributeName="missed_meetings")
	public long getNumberOfMeetingsMissed() { return numberOfMeetingsMissed; }
	public void setNumberOfMeetingsMissed(final long numberOfMeetingsMissed) { this.numberOfMeetingsMissed = numberOfMeetingsMissed; }
	
	@DynamoDBAttribute(attributeName="total_meetings")
	public long getTotalNumberOfMeetings() { return totalNumberOfMeetings; }
	public void setTotalNumberOfMeetings(final long totalNumberOfMeetings) { this.totalNumberOfMeetings = totalNumberOfMeetings; }
	
	@DynamoDBAttribute(attributeName="last_deposit_amount")
	public float getLastDepositAmount() { return lastDepositAmount; }
	public void setLastDepositAmount(final float lastDepositAmount) { this.lastDepositAmount = lastDepositAmount; }
	
	@DynamoDBAttribute(attributeName="remaining_balance_amount")
	public float getRemainingBalanceAmount() { return remainingBalanceAmount; }
	public void setRemainingBalanceAmount(final float remainingBalanceAmount) { this.remainingBalanceAmount = remainingBalanceAmount; }
	
	@DynamoDBAttribute(attributeName="star_rating")
	public float getStarRating() { return starRating; }
	public void setStarRating(final float starRating) { this.starRating = starRating; }
	
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(DynamoFieldNames.loginid, getLoginId());
			jsonObject.put(DynamoFieldNames.sub_team, getSubTeamName());
			jsonObject.put(DynamoFieldNames.attended_meetings, getNumberOfMeetingsAttended());
			jsonObject.put(DynamoFieldNames.missed_meetings, getNumberOfMeetingsMissed());
			jsonObject.put(DynamoFieldNames.total_meetings, getTotalNumberOfMeetings());
			jsonObject.put(DynamoFieldNames.last_deposit_amount, getLastDepositAmount());
			jsonObject.put(DynamoFieldNames.remaining_balance, getRemainingBalanceAmount());
			jsonObject.put(DynamoFieldNames.star_rating, getStarRating());
		} catch (JSONException e) {
			//TODO use LambdaLogger
		}
		
		return jsonObject.toString();
	}
	
	
	public static final String TABLE_NAME = "team_beer_fund";
	public static class DynamoFieldNames {
		public static String loginid = "loginid";
		public static String sub_team = "sub_team";
		public static String attended_meetings = "attended_meetings";
		public static String missed_meetings = "missed_meetings";
		public static String total_meetings = "total_meetings";
		public static String last_deposit_amount = "last_deposit_amount";
		public static String remaining_balance = "remaining_balance_amount";
		public static String star_rating = "star_rating";
	}
}
