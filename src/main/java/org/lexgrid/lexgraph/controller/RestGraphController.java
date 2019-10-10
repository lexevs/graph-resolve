package org.lexgrid.lexgraph.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.lexgrid.lexgraph.configuration.DatabaseSpecificConfigFactory;
import org.lexgrid.lexgraph.model.LexVertex;
import org.lexgrid.lexgraph.model.SystemMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.springframework.config.ArangoConfiguration;

import org.lexgrid.lexgraph.service.SystemService;;


@RestController
public class RestGraphController {
	
   @Autowired 
   SystemService system;
	
   private final Logger log = LoggerFactory.getLogger(RestGraphController.class);

   private DatabaseSpecificConfigFactory configFactory = new DatabaseSpecificConfigFactory();
    

    
//    @GetMapping("/graphs/{graphName}/{vertexCollection}/{key}")
//    public Iterable<LexVertex> findByKeyAndCollection(@PathVariable @NotNull String graphName, @PathVariable @NotNull String key, @PathVariable @NotNull String vertexCollection) {
//           log.debug("Load the article of id: {}", key);
//           return graphingService.resolveAllInBoundEntitiesForCollectionAndRoot(graphName, key, vertexCollection);
//       }


    
    @GetMapping("/graphDbs/{graphDatabase}")
    public GraphDatabase getGraphDataBaseMetaData(@PathVariable @NotNull String graphDatabase){
    	return system.getGraphDatabaseShell(graphDatabase);
    }
    
    @GetMapping("/databases")
    public SystemMetadata getSystemMetadata(){
    	return system.getSystemMetadata();
    }
    
    @GetMapping("/database/{database}")
    	public ArangoDatabase getDatabase(@PathVariable @NotNull String database){
    	ArangoDatabase graphDb = configFactory.getArangoDataBaseConfigurationForName(database).arango().build().db();;
    	graphDb.arango().shutdown();
    	return graphDb;
    }
    
    
       
    @GetMapping("/getInbound/{database}/{graph}/{code}")
	public Iterable<LexVertex> getTraversedGraphMembers(
			@PathVariable @NotNull String database, 
			@PathVariable @NotNull String graph, 
			@PathVariable @NotNull String code){
	
	ArangoConfiguration config = new DatabaseSpecificConfigFactory().getArangoDataBaseConfigurationForName(database);
	ArangoDatabase db = config.arango().build().db(database);
	ArangoGraph graphEntity = db.graph(graph);
	String collectionName = graphEntity.getVertexCollections().iterator().next();
	
	Map<String, Object> bindVars = new HashMap<String, Object>();
	bindVars.put("id", collectionName + "/" + code);
	bindVars.put("graph", graphEntity.name());
	try {
		return config.arangoTemplate().query("FOR v IN 1..10 INBOUND @id GRAPH @graph "
				+ "OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN "
				+ "{code: v._key, namespace: v.namespace}", 
				bindVars, 
				null, 
				LexVertex.class).
				asListRemaining();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	}
	finally{
		if(config != null){
		try {
			config.arangoTemplate().driver().shutdown();
		} catch (ArangoDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	return null;
    
}



}
