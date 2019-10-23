package org.lexgrid.lexgraph.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.lexgrid.lexgraph.configuration.DatabaseSpecificConfigFactory;
import org.lexgrid.lexgraph.model.GraphDatabase;
import org.lexgrid.lexgraph.model.SystemMetadata;
import org.lexgrid.lexgraph.property.LexArangoConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.arangodb.ArangoDB;
import com.arangodb.entity.GraphEntity;
import com.arangodb.springframework.config.ArangoConfiguration;

@Service
public class SystemService {
	
	@Value("${address}")
	private String address;
	
	
	@Autowired
	private LexArangoConnectionProperties props;


	public SystemMetadata getSystemMetadata() {
		SystemMetadata sysmdt = new SystemMetadata();
		ArangoDB arango = new DatabaseSpecificConfigFactory(props).getArangoDriver();
		sysmdt.setDataBases( arango.getAccessibleDatabases().stream()
				.filter(x -> !x.equals("_system")).collect(Collectors.toList()));
		 arango.shutdown();
		return sysmdt;
	}

	public GraphDatabase getGraphDatabaseShell(String database) {
		GraphDatabase dbShell = new GraphDatabase();
		dbShell.setGraphDbName(database);
		dbShell.setGraphs(getGraphs(database));
		return dbShell;
	}


	private List<String> getGraphs(String dbName) {
		DatabaseSpecificConfigFactory configFactory = new DatabaseSpecificConfigFactory(props);
		Collection<GraphEntity> entities = configFactory.getDbForDatabaseName(dbName).getGraphs();
		configFactory.getArangoDriver().shutdown();
		return entities.stream().map(x -> x.getName()).collect(Collectors.toList());
	}

	/**
	 * @return the props
	 */
	public LexArangoConnectionProperties getProps() {
		return props;
	}

	/**
	 * @param props the props to set
	 */
	public void setProps(LexArangoConnectionProperties props) {
		this.props = props;
	}
}
