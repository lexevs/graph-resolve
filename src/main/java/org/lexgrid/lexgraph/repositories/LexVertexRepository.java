package org.lexgrid.lexgraph.repositories;

import org.lexgrid.lexgraph.model.LexVertex;
import org.springframework.data.repository.query.Param;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;

public interface LexVertexRepository extends ArangoRepository<LexVertex, String> {

    // Supposing that article is the collection name to perform the query on
    @Query("FOR v IN 1..10 INBOUND @id GRAPH @graph OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN {code: v._key, namespace: v.namespace}")
    Iterable<LexVertex> getFullVertexInboundResolveByCodeAndAssociation(@Param("graph") String graph, @Param("id") String id);

    Iterable<LexVertex> findByCode(String code);
 
}
