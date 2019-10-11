package org.lexgrid.lexgraph.controller;

import javax.validation.constraints.NotNull;

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
		return system.getGraphDatabaseShell(graphDatabase);
	}

	@GetMapping("/databases")
	public SystemMetadata getSystemMetadata() {
		return system.getSystemMetadata();
	}

	@GetMapping("/getInbound/{database}/{graph}/{code}")
	public Iterable<LexVertex> getTraversedInboundGraphMembers(@PathVariable @NotNull String database,
			@PathVariable @NotNull String graph, @PathVariable @NotNull String code) {

		return graphingService.resolveAllInBoundEntitiesForGraphAndRoot(database, graph, code);

	}

	@GetMapping("/getOutbound/{database}/{graph}/{code}")
	public Iterable<LexVertex> getTraverseOutboundGraphMembers(@PathVariable @NotNull String database,
			@PathVariable @NotNull String graph, @PathVariable @NotNull String code) {

		return graphingService.resolveAllOutBoundEntitiesForGraphAndRoot(database, graph, code);

	}

}
