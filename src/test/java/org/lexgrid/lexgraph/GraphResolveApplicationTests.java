package org.lexgrid.lexgraph;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lexgrid.lexgraph.controller.RestGraphController;
import org.lexgrid.lexgraph.model.GraphDatabase;
import org.lexgrid.lexgraph.model.LexVertex;
import org.lexgrid.lexgraph.model.SystemMetadata;
import org.lexgrid.lexgraph.service.LexVertexService;
import org.lexgrid.lexgraph.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphResolveApplicationTests {
	
	@Autowired
	private RestGraphController controller;
	
	@Autowired
	private SystemService system;
	
	@Autowired
	private LexVertexService graphService;

	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(system).isNotNull();
		assertThat(graphService).isNotNull();
	}
	
	@Test
	public void systemServiceTestGraphDbMetadata(){
		GraphDatabase shell = system.getGraphDatabaseShell("owl2lexevs");
		assertThat(shell.getGraphDbName()).isEqualTo("owl2lexevs");
		shell.getGraphs().stream().sorted().forEach(x -> System.out.println(x));
//		assertThat(shell.getGraphs().size()).isEqualTo(65);
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("subClassOf"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("AllDifferent"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("Anatomic_Structure_Has_Location"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("editor_preferred_term"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("Concept_In_Subset"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("willing"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("unwilling"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("is_specified_output_of"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("has_grain"))).isTrue();
		assertThat(shell.getGraphs().stream().anyMatch(x -> x.equals("domain"))).isTrue();
	}
	
	
	@Test
	public void systemServiceTestDatabases(){
		SystemMetadata systemMeta = system.getSystemMetadata();
		assertThat(StreamSupport.stream(systemMeta.getDataBases().spliterator(), false).anyMatch(x -> x.equals("owl2lexevs"))).isTrue();
	}
	
	@Test
	public void lexVertexServiceTestQueryOutbound() throws DataAccessException, Exception{
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGraphAndRoot("owl2lexevs", "subClassOf", "C61410");
		assertThat(vertexs).isNotNull();
		Iterator<LexVertex> itr = vertexs.iterator();
		assertThat(itr.hasNext()).isTrue();
		assertThat("C54443").isEqualTo(itr.next().getCode());
		assertThat(itr.hasNext()).isFalse();
	}
	
//	@Test
//	public void lexVertexServiceTestQueryOutboundSubClassOf() throws DataAccessException, Exception{
//		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGraphAndRoot("owl2lexevs", "subClassOf", "C53684");
//		assertThat(vertexs).isNotNull();
//		assertThat(vertexs.iterator().hasNext()).isTrue();
//		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C3263"))).isTrue();
//		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C27574"))).isTrue();
//		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C3810"))).isTrue();
//		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C61410"))).isFalse();
//	}
	
	@Test
	public void lexVertexServiceTestQueryOutboundConceptInSubset() throws DataAccessException, Exception{
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGraphAndRoot("owl2lexevs", "Concept_In_Subset", "C48323");
		assertThat(vertexs).isNotNull();
		assertThat(vertexs.iterator().hasNext()).isTrue();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C54453"))).isTrue();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C117743"))).isTrue();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C99999"))).isFalse();
	}
	
	@Test
	public void lexVertexServiceTestQueryInboundConceptInSubset() throws DataAccessException, Exception{
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGraphAndRoot("owl2lexevs", "Concept_In_Subset", "C48323");
		assertThat(vertexs).isNotNull();
		Iterator<LexVertex> itr = vertexs.iterator();
		assertThat(itr.hasNext()).isTrue();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C99999"))).isTrue();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C99998"))).isTrue();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C54453"))).isFalse();
	}
}
