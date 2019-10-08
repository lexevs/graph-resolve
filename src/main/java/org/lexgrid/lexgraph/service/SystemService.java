package org.lexgrid.lexgraph.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.lexgrid.lexgraph.configuration.ArangoConfig;
import org.lexgrid.lexgraph.controller.GraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arangodb.ArangoDatabase;

@Service
public class SystemService {
	
	  @Autowired
	    private ArangoConfig config;
	  
		public Iterable<String> getAllGraphDatabases(){
			return config.getGraphDatabases();
		}
		
		public ArangoDatabase getGraphDatabaseForName(String name){
				 return config.getGraphDatabaseForName(name);
		}

		public GraphDatabase getGraphDatabaseShellForName(String graphDatabase) {
			GraphDatabase gphdb = new GraphDatabase();
			ArangoDatabase db = config.arango().build().db(graphDatabase);
			gphdb.setGraphDbName(graphDatabase);
			gphdb.setGraphs(db.getGraphs().stream().map(x -> x.getName()).collect(Collectors.toList()));
			return gphdb;
		}

}
