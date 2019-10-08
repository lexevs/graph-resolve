package org.lexgrid.lexgraph.repositories;


import com.arangodb.ArangoDatabase;
import com.arangodb.springframework.repository.ArangoRepository;


public interface SystemRepository<T, ID> extends ArangoRepository<T,ID>{
	
//	public Iterable<String> getAllGraphDatabases();
//	
//	public ArangoDatabase getGraphDatabaseForName(String name);

}
