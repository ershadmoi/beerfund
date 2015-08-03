package com.junaid.beerfund.dynamo;

import java.util.List;

/**
 * An interface that encapsulates operations supported by a DynamoDBReadWriter
 * @author Ershad
 *
 * @param <T>		This is a generic to the backing table entry type
 */
public interface IDynamoDBReadWriter<T> {
	/**
	 * Reads a typed generic resource using [primary, primary+rangekey]
	 * @param clazz			The class which represents the dynamoTable being used
	 * @param primaryKey	The primaryKey for this table
	 * @param rangeKey		The rangeKey for this table
	 * @return				An instance of the type T or null if row not found
	 */
	T readResource(final Class<T> clazz, final String primaryKey, final String rangeKey);
	
	/**
	 * Writes a given entry to its mapped tableName
	 * @param t		The table entry which needs to be saved using a dynamodb mapper
	 */
	void writeResource(final T t);
	
	/**
	 * Reads an entire Table as a list using the getTableName() value
	 * @return
	 */
	List<T> readTable();
	
	/**
	 * Returns the table name that the typed concrete implementation is backing
	 * @return	The TableName backing the concrete implementation
	 */
	String getTableName();
}
