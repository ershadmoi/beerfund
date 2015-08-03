package com.junaid.beerfund.dynamo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.junaid.beerfund.utils.DynamoDBHelper;

/**
 * A concrete implementation of DynamoDBReadWriter typed to BeerFundDeckEntry table
 * @author Ershad
 */
public class BeerFundDeckDynamoDBReadWriter implements IDynamoDBReadWriter<BeerFundDeckEntry> {
	// The dynamoDB client instance
	private AmazonDynamoDBClient client;
	
	// The dynamodbMapper
	private DynamoDBMapper mapper;
	
	// Default Constructor
	public BeerFundDeckDynamoDBReadWriter(){
		this(new AmazonDynamoDBClient(new EnvironmentVariableCredentialsProvider()));
		
	}
	
	// Alternate Constructor where local credential providers can be used
	public BeerFundDeckDynamoDBReadWriter(final AmazonDynamoDBClient client) {
		this.client = client;
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		mapper = new DynamoDBMapper(client);
	}
	
	public BeerFundDeckEntry readResource(final Class<BeerFundDeckEntry> beerFundDeckEntryClazz, 
			final String primaryKey, final String rangeKey) {
		BeerFundDeckEntry beerFundDeckEntry = null;
		if(primaryKey != null && primaryKey != "") {
			if(rangeKey != null && rangeKey != "") {
				beerFundDeckEntry = mapper.load(beerFundDeckEntryClazz, primaryKey, rangeKey);
			} else {
				beerFundDeckEntry = mapper.load(beerFundDeckEntryClazz, primaryKey);
			}
		}
		
		return beerFundDeckEntry;
	}
	
	public void writeResource(final BeerFundDeckEntry beerFundDeckEntry) {
		if(beerFundDeckEntry != null) {
			mapper.save(beerFundDeckEntry);
		}
	}
	
	public List<BeerFundDeckEntry> readTable() {
		ScanResult scanResult = DynamoDBHelper.readTable(client, getTableName());
		List<BeerFundDeckEntry> beerFundDeckEntries = new ArrayList<>();
		if(scanResult != null) {
			for(Map<String, AttributeValue> item : scanResult.getItems()) {
				BeerFundDeckEntry beerFundDeckEntry = DynamoDBHelper.createBeerFundDeckEntryFromAttributeMap(item);
				if(beerFundDeckEntry != null) { 
					beerFundDeckEntries.add(beerFundDeckEntry); 
				}
			}
		}
		
		return beerFundDeckEntries;
	}
	
	public String getTableName() {
		return BeerFundDeckEntry.TABLE_NAME;
	}
}
