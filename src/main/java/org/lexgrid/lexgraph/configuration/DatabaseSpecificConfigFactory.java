package org.lexgrid.lexgraph.configuration;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.springframework.config.ArangoConfiguration;

public class DatabaseSpecificConfigFactory {

	public ArangoConfiguration getArangoDataBaseConfigurationForName(String dbName) {

		class LocalArangoConfig implements ArangoConfig {

			@Override
			public String database() {
				return dbName;
			}

		};
		
		return new LocalArangoConfig();
	}

	public ArangoDB getArangoDriver() {
		
		class LocalArangoConfig implements ArangoConfig {};
		
		return new LocalArangoConfig().arango().build();
	}

	public ArangoDatabase getDbForDatabaseName(String dbName) {
		return getArangoDataBaseConfigurationForName(dbName).arango().build().db(dbName);
	}

}
