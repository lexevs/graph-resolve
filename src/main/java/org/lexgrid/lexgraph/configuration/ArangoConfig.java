package org.lexgrid.lexgraph.configuration;

import org.springframework.context.annotation.Configuration;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDB.Builder;
import com.arangodb.Protocol;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
 
@Configuration
@EnableArangoRepositories(basePackages = { "org.lexgrid.lexgraph.repositories" })
public interface ArangoConfig extends ArangoConfiguration {
	
  @Override
  default public Builder arango() {;
    ArangoDB.Builder arango = new ArangoDB.Builder()
            .host("127.0.0.1", 8529)
            .useProtocol(Protocol.HTTP_JSON)
            .user("root")
            .password("lexgrid");
    return arango;
  }
 
  @Override
  default public String database() {
    return "NCI_Thesaurus";
  }
  
  
}
