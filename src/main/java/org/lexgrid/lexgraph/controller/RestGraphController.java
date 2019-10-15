package org.lexgrid.lexgraph.controller;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpEntity;
import org.lexgrid.lexgraph.exception.GraphDbNotFoundException;
import org.lexgrid.lexgraph.exception.SystemMetadataNotFoundException;
import org.lexgrid.lexgraph.exception.VertexNotFoundException;
import org.lexgrid.lexgraph.model.GraphDatabase;
import org.lexgrid.lexgraph.model.LexVertex;
import org.lexgrid.lexgraph.model.SystemMetadata;
import org.lexgrid.lexgraph.service.LexVertexService;
import org.lexgrid.lexgraph.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;;

@RestController
public class RestGraphController {

	@Autowired
	SystemService system;

	@Autowired
	LexVertexService graphingService;

	private final Logger log = LoggerFactory.getLogger(RestGraphController.class);


	@GetMapping("/graphDbs/{graphDatabase}")
	public GraphDatabase getGraphDataBaseMetaData(@PathVariable @NotNull String graphDatabase) {
		GraphDatabase db; 
		try {db = system.getGraphDatabaseShell(graphDatabase);
		}
		catch(com.arangodb.ArangoDBException a){
			throw new GraphDbNotFoundException("Graph database \"" + graphDatabase + 
					"\" not found or is lacking crucial elements for graph resolution."  +
					" Database message: " + a.getErrorMessage());
		}
		if(db == null 
				|| db.graphs == null
				|| db.graphDbName == null
				|| db.graphs.size() == 0 
				|| db.graphDbName == "")
			throw new GraphDbNotFoundException("Graph database  \"" + graphDatabase + 
					"\" not found or is lacking crucial elements for graph resolution.");
		return system.getGraphDatabaseShell(graphDatabase);
	}

	@GetMapping("/databases")
	public SystemMetadata getSystemMetadata() {
		SystemMetadata sysMd = system.getSystemMetadata();
		if(sysMd == null
				|| sysMd.getDataBases() == null
				|| !sysMd.getDataBases().iterator().hasNext()){
			throw new SystemMetadataNotFoundException("No graph databases are found on this system");
		}
		return system.getSystemMetadata();
	}

	@GetMapping("/getInbound/{database}/{graph}/{code}")
	public Iterable<LexVertex> getTraversedInboundGraphMembers(@PathVariable @NotNull String database,
			@PathVariable @NotNull String graph, @PathVariable @NotNull String code) {
		Iterable<LexVertex> list = graphingService.resolveAllInBoundEntitiesForGraphAndRoot(database, graph, code);
		if(list == null || !list.iterator().hasNext()){
			throw new VertexNotFoundException("Vertex not found or no inbound edges exist for entity code: " + code);
		}
		else{return list;}
	}

	@GetMapping("/getOutbound/{database}/{graph}/{code}")
	public Iterable<LexVertex> getTraverseOutboundGraphMembers(@PathVariable @NotNull String database,
			@PathVariable @NotNull String graph, @PathVariable @NotNull String code) {
		Iterable<LexVertex> list = graphingService.resolveAllOutBoundEntitiesForGraphAndRoot(database, graph, code);
		if(list == null || !list.iterator().hasNext()){
			throw new VertexNotFoundException("Vertex not found or no outbound edges exist for entity code: " + code);
		}
		else{return list;}
	}

}
