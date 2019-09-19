package org.lexgrid.lexgraph.service;

import java.util.Optional;

import org.lexgrid.lexgraph.model.LexVertex;
import org.lexgrid.lexgraph.repositories.LexVertexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LexVertexService {

	  @Autowired
	    private LexVertexRepository lexVertexRepository;



	    /**
	     * Find all saved lexVertexs so far
	     * @return
	     */
	    public Iterable<LexVertex> resolveAllInBoundEntitiesForCollectionAndRoot(String code, String collectionName, String graphName) {
	        return lexVertexRepository.getFullVertexInboundResolveByCodeAndAssociation(code, collectionName, graphName);
	    }


}
