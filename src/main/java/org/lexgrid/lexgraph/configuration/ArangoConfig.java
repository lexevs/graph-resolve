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
  default public String database() {
    return "NCI_Thesaurus";
  }
 
}
