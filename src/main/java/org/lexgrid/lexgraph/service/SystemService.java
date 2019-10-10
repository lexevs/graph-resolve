package org.lexgrid.lexgraph.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.lexgrid.lexgraph.configuration.DatabaseSpecificConfigFactory;
import org.lexgrid.lexgraph.controller.GraphDatabase;
import org.lexgrid.lexgraph.model.SystemMetadata;
import org.springframework.stereotype.Service;

import com.arangodb.entity.GraphEntity;

@Service
public class SystemService {

	private DatabaseSpecificConfigFactory configFactory = new DatabaseSpecificConfigFactory();

	public SystemMetadata getSystemMetadata() {
		SystemMetadata sysmdt = new SystemMetadata();
		sysmdt.setDataBases(
				configFactory.getArangoDriver()
				.getAccessibleDatabases()
				.stream()
				.filter(x -> !x.equals("_system"))
				.collect(Collectors.toList()));
		configFactory.getArangoDriver().shutdown();
		return sysmdt;
	}
	
	public GraphDatabase getGraphDatabaseShell(String database){
		GraphDatabase dbShell = new GraphDatabase();
		dbShell.setGraphDbName(database);
		dbShell.setGraphs(getGraphs(database));
		return dbShell;
	}
	
	public List<String> getGraphs(String dbName){
		Collection<GraphEntity> entities = configFactory
		.getDbForDatabaseName(dbName)
		.getGraphs();
		configFactory.getArangoDriver().shutdown();
		return entities.stream()
				.map(x -> x.getName())
				.collect(Collectors.toList());
	}

}
