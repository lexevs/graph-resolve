package org.lexgrid.lexgraph.configuration;

import org.lexgrid.lexgraph.property.LexArangoConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.Protocol;
import com.arangodb.ArangoDB.Builder;
import com.arangodb.springframework.config.ArangoConfiguration;

import java.util.stream.Stream;

public class DatabaseSpecificConfigFactory {
	
	private LexArangoConnectionProperties configArango;
	
	@Autowired
	private Environment env;

	public DatabaseSpecificConfigFactory(LexArangoConnectionProperties configArango){
		this.configArango = configArango;
	}

	public ArangoConfiguration getArangoDataBaseConfigurationForName(String dbName) {

		class LocalArangoConfig implements ArangoConfig {
			
			  @Override
			  public Builder arango() {;
			  	String pwd = System.getProperty("pwd");
			  	if(pwd == null){configArango.getPwd();}
			    ArangoDB.Builder arango = new ArangoDB.Builder()
			            .host(configArango.getAddress(), configArango.getPort())
			            .useProtocol(getProtocolForName(configArango.getProtocol()))
			            .user(configArango.getUsr())
			            .password(pwd);
			    return arango;
			  }

			@Override
			public String database() {
				return dbName;
			}

		};
		
		return new LocalArangoConfig();
	}

	public ArangoDB getArangoDriver() {
		
		class LocalArangoConfig implements ArangoConfig {
			
			  @Override
			  public Builder arango() {
				  	String pwd = System.getProperty("pwd");
				  	if(pwd == null){configArango.getPwd();}
			    ArangoDB.Builder arango = new ArangoDB.Builder()
			            .host(configArango.getAddress(), configArango.getPort())
			            .useProtocol(getProtocolForName(configArango.getProtocol()))
			            .user(configArango.getUsr())
			            .password(pwd);
			    return arango;
			  }			
		};
		
		return new LocalArangoConfig().arango().build();
	}

	public ArangoDatabase getDbForDatabaseName(String dbName) {
		return getArangoDataBaseConfigurationForName(dbName).arango().build().db(dbName);
	}
	
	private Protocol getProtocolForName(String name){
		return Stream.of(Protocol.values()).filter(x -> x.name().equals(name)).findAny().get();
	}

}
