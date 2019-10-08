package org.lexgrid.lexgraph.controller;

import java.util.List;

public class GraphDatabase {

	/**
	 * @return the graphDbName
	 */
	public String getGraphDbName() {
		return graphDbName;
	}

	/**
	 * @param graphDbName the graphDbName to set
	 */
	public void setGraphDbName(String graphDbName) {
		this.graphDbName = graphDbName;
	}

	/**
	 * @return the graphs
	 */
	public List<String> getGraphs() {
		return graphs;
	}

	/**
	 * @param graphs the graphs to set
	 */
	public void setGraphs(List<String> graphs) {
		this.graphs = graphs;
	}

	public String graphDbName;
	
	public List<String> graphs;

}
