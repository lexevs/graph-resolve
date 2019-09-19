package org.lexgrid.lexgraph.repositories;

import org.lexgrid.lexgraph.model.LexVertex;

import com.arangodb.springframework.annotation.Param;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;

public interface LexVertexRepository extends ArangoRepository<LexVertex> {

    // Supposing that article is the collection name to perform the query on
    @Query("FOR v IN 1..10 INBOUND '@collection/@key GRAPH' @graph OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN {code: v._key, namespace: v.namespace}")
    Iterable<LexVertex> getFullVertexInboundResolveByCodeAndAssociation(@Param("key") String code, @Param("collection") String collectionName, @Param("graph") String graphName);

    Iterable<LexVertex> findByCode(String code);
}
