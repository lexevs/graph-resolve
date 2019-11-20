package org.lexgrid.lexgraph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
		List<String> graphs = shell.getGraphs();
		assertThat(shell.getGraphs().size()).isEqualTo(61);
		assertThat(graphs.stream().anyMatch(x -> x.equals("subClassOf"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("AllDifferent"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("Anatomic_Structure_Has_Location"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("editor_preferred_term"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("Concept_In_Subset"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("willing"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("unwilling"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("is_specified_output_of"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("has_grain"))).isTrue();
		assertThat(graphs.stream().anyMatch(x -> x.equals("domain"))).isTrue();
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
	

	@Test
	public void lexVertexServiceTestQueryOutboundSubClassOf() throws DataAccessException, Exception{
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGraphAndRoot("owl2lexevs", "subClassOf", "C61410");
		assertThat(vertexs).isNotNull();
		assertThat(vertexs.iterator().hasNext()).isTrue();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C54443"))).isTrue();
	}
	
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
	
	public void lexVertexServiceTestQueryInboundConceptInSubset2() throws DataAccessException, Exception{
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGraphAndRoot("owl2lexevs", "subClassOf", "C19448");
		assertThat(vertexs).isNotNull();
		Iterator<LexVertex> itr = vertexs.iterator();
		assertThat(itr.hasNext()).isTrue();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("EpithelialCell")));
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("string")));
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("C123")));
	}
	
	public void lexVertexServiceTestQueryInBoundPhasPrognosis2() throws DataAccessException, Exception{
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGraphAndRoot("owl2lexevs","patient_has_prognosis", 
				"CancerPatient");
		assertThat(vertexs).isNotNull();
		Iterator<LexVertex> itr = vertexs.iterator();
		assertThat(itr.hasNext()).isFalse();
	}
	
	public void lexVertexServiceTestQueryOutBoundPhasPrognosis2() throws DataAccessException, Exception{
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGraphAndRoot("owl2lexevs","patient_has_prognosis", 
				"CancerPatient");
		List<String> list = new ArrayList<String>();
		list.add("PrognosisGood");
		list.add("PrognosisBad");
		assertThat(vertexs).isNotNull();
		Iterator<LexVertex> itr = vertexs.iterator();
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("PrognosisGood")));
		assertThat(StreamSupport.stream(vertexs.spliterator(), false).anyMatch(x -> x.getCode().equals("PrognosisBad")));
		assertThat(list.remove(itr.next().getCode()));
		assertThat(list.remove(itr.next().getCode()));
		assertThat(itr.hasNext()).isFalse();
	}
	
	public void lexVertexServiceTestQueryOutBoundConceptNSubset() throws DataAccessException, Exception{
	List<String> codes = new ArrayList<String>();
	codes.add("C117743");
	codes.add("C54453");
	Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGraphAndRoot("owl2lexevs",
			"Concept_In_Subset", 
			"C48323");
	assertThat(vertexs).isNotNull();
	Iterator<LexVertex> itr = vertexs.iterator();
	assertTrue(itr.hasNext());
	assertTrue(codes.remove(itr.next().getCode()));
	assertTrue(codes.remove(itr.next().getCode()));
	assertTrue(codes.remove(itr.next().getCode()));
	assertFalse(itr.hasNext());
	}
	
	public void lexVertexServiceTestQueryOutBoundConceptNSubset2() throws DataAccessException, Exception{
	List<String> codes = new ArrayList<String>();
	codes.add("C48323");
	codes.add("C54453");
	Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGraphAndRoot("owl2lexevs",
			"Concept_In_Subset", 
			"C99999");
	assertThat(vertexs).isNotNull();
	Iterator<LexVertex> itr = vertexs.iterator();
	assertTrue(itr.hasNext());
	assertTrue(codes.remove(itr.next().getCode()));
	assertTrue(codes.remove(itr.next().getCode()));
	assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnly4() throws DataAccessException, Exception {

		List<String> codes = new ArrayList<String>();
		codes.add("C99999");
		codes.add("C99998");
		codes.add("C99988");
		codes.add("C99989");
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGraphAndRoot("owl2lexevs", 
				"Concept_In_Subset", 
				"C48323");
		Iterator<LexVertex> itr = vertexs.iterator();		
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInGoingOnlyEmpty() throws DataAccessException, Exception {
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGraphAndRoot("owl2lexevs",  
				"gene_related_to_disease", 
				"NeoplasticDisease");
		Iterator<LexVertex> itr = vertexs.iterator();		
		assertNotNull(itr);
		assertFalse(itr.hasNext());
		
	}
	
	@Test
	public void testInGoingOnlyExactMatchCodeDepth1() throws DataAccessException, Exception {

		List<String> codes = new ArrayList<String>();
		codes.add("C99999");
		codes.add("C99998");
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGrapDepthAndRoot("1","owl2lexevs", 
				"Concept_In_Subset", 
				"C48323");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	
	@Test
	public void testInGoingOnlyExactMatchCodeDepth2() throws DataAccessException, Exception {

		List<String> codes = new ArrayList<String>();
		codes.add("C99999");
		codes.add("C99998");
		codes.add("C99988");
		codes.add("C99989");
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGrapDepthAndRoot( "2",
				"owl2lexevs",
				"Concept_In_Subset", 
				"C48323");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingOnlyExactMatchCodeDepth1() throws DataAccessException, Exception {
		List<String> codes = new ArrayList<String>();
		codes.add("C117743");
		codes.add("C54453");
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGrapDepthAndRoot("1","owl2lexevs",  
				"Concept_In_Subset", 
				"C48323");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingExactMatchCodeDepthPatient1() throws DataAccessException, Exception {
		List<String> codes = new ArrayList<String>();
		codes.add("Person");
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGrapDepthAndRoot("1","owl2lexevs", 
				"subClassOf", 
				"Patient");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testOutGoingExactMatchCodeDepthPatient2NoChange() throws DataAccessException, Exception {
		List<String> codes = new ArrayList<String>();
		codes.add("Person");
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGrapDepthAndRoot( "2",
				"owl2lexevs",
				"subClassOf", 
				"Patient");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInComingExactMatchCodeDepthPatientDepth1() throws DataAccessException, Exception {

		List<String> codes = new ArrayList<String>();
		codes.add("HappyPatientDrivingAround");
		codes.add("HappyPatientWalkingAround");
		codes.add("HealthyPatient");
		codes.add("SickPatient");
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGrapDepthAndRoot("1","owl2lexevs", 
				"subClassOf", 
				"Patient");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}

	@Test
	public void testInComingExactMatchCodeDepthPatientDepth2() throws DataAccessException, Exception {

		List<String> codes = new ArrayList<String>();
		codes.add("HappyPatientDrivingAround");
		codes.add("HappyPatientWalkingAround");
		codes.add("HealthyPatient");
		codes.add("SickPatient");
		codes.add("VerySickPatient");
		codes.add("MildlySickPatient");
		codes.add("CancerPatient");
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGrapDepthAndRoot( "2",
				"owl2lexevs", 
				"subClassOf", 
				"Patient");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testInComingExactMatchCodeDepthPatientDepth3() throws DataAccessException, Exception {

		List<String> codes = new ArrayList<String>();
		codes.add("HappyPatientDrivingAround");
		codes.add("HappyPatientWalkingAround");
		codes.add("HealthyPatient");
		codes.add("SickPatient");
		codes.add("VerySickPatient");
		codes.add("MildlySickPatient");
		codes.add("CancerPatient");
		codes.add("VerySickCancerPatient");
		codes.add("PatientWithCold");
		codes.add("MildlySickCancerPatient");
		Iterable<LexVertex> vertexs = graphService.resolveAllInBoundEntitiesForGrapDepthAndRoot( "3",
				"owl2lexevs",  
				"subClassOf", 
				"Patient");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	
	
	
	@Test
	public void testOutGoingOnlyExactMatchCodeDepth2NoChange() throws DataAccessException, Exception {

		List<String> codes = new ArrayList<String>();
		codes.add("C117743");
		codes.add("C54453");
		Iterable<LexVertex> vertexs = graphService.resolveAllOutBoundEntitiesForGrapDepthAndRoot( "2",
				"owl2lexevs",  
				"Concept_In_Subset", 
				"C48323");
		Iterator<LexVertex> itr = vertexs.iterator();
		assertNotNull(itr);
		assertTrue(itr.hasNext());
		assertTrue(codes.remove(itr.next().getCode()));
		assertTrue(codes.remove(itr.next().getCode()));
		assertFalse(itr.hasNext());
	}
	
	
}
