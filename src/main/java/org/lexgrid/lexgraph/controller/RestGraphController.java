package org.lexgrid.lexgraph.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.lexgrid.lexgraph.model.LexVertex;
import org.lexgrid.lexgraph.service.LexVertexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestGraphController {
   private final Logger log = LoggerFactory.getLogger(RestGraphController.class);

    private final LexVertexService graphingService;

    public RestGraphController(LexVertexService graphingService) {
        this.graphingService = graphingService;
    }
    
    /**
     * Find an article by its id
     * @param id
     * @return
     */
    @GetMapping("/{vertexCollection}/{id}")
    public Iterable<LexVertex> findByKey(@PathVariable @NotNull String vertexCollection, @PathVariable @NotNull String key) {
        log.debug("Load the article of id: {}", key);
        return graphingService.resolveAllInBoundEntitiesForCollectionAndRoot(null,null, null);
    }

}
