package org.lexgrid.lexgraph.configuration;

import org.springframework.context.annotation.Configuration;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;

public class DatabaseSpecificConfigFactory  {

	public ArangoConfiguration getArangoDataBaseConfigurationForName(String dbName){
		
		@Configuration
		@EnableArangoRepositories(basePackages = { "org.lexgrid.lexgraph.repositories" })
		class LocalArangoConfig implements ArangoConfig {

			@Override
			public String database() {
				return dbName;
			}

		};
		
		LocalArangoConfig config = new LocalArangoConfig();
		
		return config;
	}
	
	public ArangoDB getArangoDriver(){

		class LocalArangoConfig implements ArangoConfig {

		};
		
		LocalArangoConfig config = new LocalArangoConfig();
		
		return config.arango().build();
	}
	
	
	public ArangoDatabase getDbForDatabaseName(String dbName){
		return getArangoDataBaseConfigurationForName(dbName).arango().build().db(dbName);
	}
	
	

}
