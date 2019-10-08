package org.lexgrid.lexgraph.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.lexgrid.lexgraph.model.LexVertex;
import org.lexgrid.lexgraph.model.SystemMetadata;
import org.lexgrid.lexgraph.service.LexVertexService;
import org.lexgrid.lexgraph.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.arangodb.ArangoDatabase;

@RestController
public class RestGraphController {
   private final Logger log = LoggerFactory.getLogger(RestGraphController.class);

    private final LexVertexService graphingService;
    private final SystemService systemService;

    public RestGraphController(LexVertexService graphingService, SystemService systemService) {
        this.graphingService = graphingService;
        this.systemService = systemService;
    }
    
    /**
     * Find an article by its id
     * @param id
     * @return
     */
    @GetMapping("/{graphdb}/{vertexCollection}/{key}")
    //@GetMapping("/some/text")
    public Iterable<LexVertex> findByKey(@PathVariable @NotNull String graphdb,@PathVariable @NotNull String vertexCollection, @PathVariable @NotNull String key) {
     //   log.debug("Load the article of id: {}", key);
     //   return graphingService.resolveAllInBoundEntitiesForCollectionAndRoot(null,null, null);
        List<LexVertex> iterable = new ArrayList<LexVertex>();
        iterable.add(new LexVertex("ACode", "aNamespace"));
        iterable.add(new LexVertex("notherCode", "NotherNamespace"));
        return iterable;
    }
    
    @GetMapping("/graphs/{graphName}/{vertexCollection}/{key}")
    public Iterable<LexVertex> findByKeyAndCollection(@PathVariable @NotNull String graphName, @PathVariable @NotNull String key, @PathVariable @NotNull String vertexCollection) {
           log.debug("Load the article of id: {}", key);
           return graphingService.resolveAllInBoundEntitiesForCollectionAndRoot(graphName, key, vertexCollection);
       }
    
    @GetMapping("/graphDbs")
    public Iterable<String> getGraphDataBaseNames(){
    	return systemService.getAllGraphDatabases();
    }
    
    @GetMapping("/fullDbs/{graphDatabase}")
    public ArangoDatabase getGraphDataBaseForName(@PathVariable @NotNull String graphDatabase){
    	return systemService.getGraphDatabaseForName(graphDatabase);
    }
    
    @GetMapping("/graphDbs/{graphDatabase}")
    public GraphDatabase getGraphDataBaseMetaData(@PathVariable @NotNull String graphDatabase){
    	return systemService.getGraphDatabaseShellForName(graphDatabase);
    }
    
//    @GetMapping("/getSystemMetadata")
//    public SystemMetadata getSystemMetadata(){
//    	SystemMetadata sysmdt = new SystemMetadata();
//    	sysmdt.setDataBases(systemService.getAllGraphDatabases());
//    	return sysmdt;
//    }

	public LexVertexService getGraphingService() {
		return graphingService;
	}

}
