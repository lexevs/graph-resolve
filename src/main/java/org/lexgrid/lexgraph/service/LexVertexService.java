package org.lexgrid.lexgraph.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.lexgrid.lexgraph.configuration.DatabaseSpecificConfigFactory;
import org.lexgrid.lexgraph.model.LexVertex;
import org.lexgrid.lexgraph.property.LexArangoConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.springframework.config.ArangoConfiguration;

@Service
public class LexVertexService {
	
	@Autowired
	LexArangoConnectionProperties props;

	
	static final String INBOUND_EDGES = "FOR v IN 1..50 INBOUND @id GRAPH @graph "
			+ "OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN " + "{code: v._key, namespace: v.namespace, description: v.description}";

	static final String OUTBOUND_EDGES = "FOR v IN 1..50 OUTBOUND @id GRAPH @graph "
			+ "OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN " + "{code: v._key, namespace: v.namespace, description: v.description}";
	
	static final String INBOUND_EDGES_WDEPTH = "FOR v IN 1..#depth INBOUND @id GRAPH @graph "
			+ "OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN " + "{code: v._key, namespace: v.namespace, description: v.description}";

	static final String OUTBOUND_EDGES_WDEPTH = "FOR v IN 1..#depth OUTBOUND @id GRAPH @graph "
			+ "OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN " + "{code: v._key, namespace: v.namespace, description: v.description}";

	public Iterable<LexVertex> resolveAllInBoundEntitiesForGraphAndRoot(String database,  String graph, String code) throws DataAccessException, Exception {
		ArangoConfiguration config = new DatabaseSpecificConfigFactory(props)
				.getArangoDataBaseConfigurationForName(database);
		ArangoDatabase db = config.arango().build().db(database);
		ArangoGraph graphEntity = db.graph(graph);
		String collectionName = graphEntity.getVertexCollections().iterator().next();

		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("id", collectionName + "/" + code);
		bindVars.put("graph", graphEntity.name());
		List<LexVertex> list = config.arangoTemplate().query(INBOUND_EDGES, bindVars, null, LexVertex.class).asListRemaining();
		config.arangoTemplate().driver().shutdown();
		return list;
	}

	public Iterable<LexVertex> resolveAllOutBoundEntitiesForGraphAndRoot(String database, String graph, String code) throws DataAccessException, Exception {
		ArangoConfiguration config = new DatabaseSpecificConfigFactory(props)
				.getArangoDataBaseConfigurationForName(database);
		ArangoDatabase db = config.arango().build().db(database);
		ArangoGraph graphEntity = db.graph(graph);
		String collectionName = graphEntity.getVertexCollections().iterator().next();

		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("id", collectionName + "/" + code);
		bindVars.put("graph", graphEntity.name());
		List<LexVertex> list = config.arangoTemplate().query(OUTBOUND_EDGES, bindVars, null, LexVertex.class).asListRemaining();
		config.arangoTemplate().driver().shutdown();
		return list;
	}

	public Iterable<LexVertex> resolveAllInBoundEntitiesForGrapDepthAndRoot(@NotNull String depth,
			@NotNull String database, @NotNull String graph, @NotNull String code) throws DataAccessException, Exception {
		ArangoConfiguration config = new DatabaseSpecificConfigFactory(props)
				.getArangoDataBaseConfigurationForName(database);
		ArangoDatabase db = config.arango().build().db(database);
		ArangoGraph graphEntity = db.graph(graph);
		String collectionName = graphEntity.getVertexCollections().iterator().next();
		String finalQuery = INBOUND_EDGES_WDEPTH.replaceAll("#depth", depth);
		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("id", collectionName + "/" + code);
		bindVars.put("graph", graphEntity.name());
		List<LexVertex> list = config.arangoTemplate().query(finalQuery, bindVars, null, LexVertex.class).asListRemaining();
		config.arangoTemplate().driver().shutdown();
		return list;
	}

	public Iterable<LexVertex> resolveAllOutBoundEntitiesForGrapDepthAndRoot(@NotNull String depth,
			@NotNull String database, @NotNull String graph, @NotNull String code) throws DataAccessException, Exception {
		ArangoConfiguration config = new DatabaseSpecificConfigFactory(props)
				.getArangoDataBaseConfigurationForName(database);
		ArangoDatabase db = config.arango().build().db(database);
		ArangoGraph graphEntity = db.graph(graph);
		String collectionName = graphEntity.getVertexCollections().iterator().next();
		String finalQuery = OUTBOUND_EDGES_WDEPTH.replaceAll("#depth", depth);
		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("id", collectionName + "/" + code);
		bindVars.put("graph", graphEntity.name());
		List<LexVertex> list = config.arangoTemplate().query(finalQuery, bindVars, null, LexVertex.class).asListRemaining();
		config.arangoTemplate().driver().shutdown();
		return list;
	}

}
