package org.lexgrid.lexgraph;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GraphRestControllerTest {
	
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

	@Test
	public void testGetDataBases() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/databases",
                String.class)).contains("owl2lexevs");
	}
	
	@Test
	public void testRandomMemberOfGraphSet() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "graphDbs/owl2lexevs",
                String.class)).contains("subClassOf");
	}
	
	@Test
	public void testFirstMemberOfGraphSet() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "graphDbs/owl2lexevs",
                String.class)).contains("AllDifferent");
	}
	
	@Test
	public void testLastMemberOfGraphSet() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "graphDbs/owl2lexevs",
                String.class)).contains("willing");
	}
	
	@Test
	public void testRoleMemberOfGraphSet() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "graphDbs/owl2lexevs",
                String.class)).contains("RO_0000056");
	}
	
	@Test
	public void testSplitNameMemberOfGraphSet() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "graphDbs/owl2lexevs",
                String.class)).contains("is_specified_output_of");
	}
	
	@Test
	public void testDatabaseNameOfGraphSet() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "graphDbs/owl2lexevs",
                String.class)).contains("owl2lexevs");
	}
	
	@Test
	public void testGetbadDataBasesName() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/databasesShematabases",
                String.class)).contains("404");
	}
	
	@Test
	public void testDatabaseNonNameOfGraphSet() {
		String graphDatabase = "OBIT";
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "graphDbs/" + graphDatabase,
                String.class)).contains("not found or is lacking crucial elements for graph resolution.");
	}
	
	@Test
	public void testDatabaseNonCodeOfGraphSet() {
		String code = "CXXXXX";
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "getInbound/owl2lexevs/subClassOf/" + code,
                String.class)).contains("Vertex not found or no inbound edges exist for entity code: " + code);
	}
	
	@Test
	public void restControllerTestQueryOutbound() throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getOutbound/owl2lexevs/subClassOf/C61410", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("C54443");
		assertThat(json).doesNotContain("C3263");
	}
	
//	@Test
//	public void restControllerTestQueryOutboundSubClassOf() throws DataAccessException, Exception{
//		String json = this.restTemplate.getForObject("http://localhost:" + port + "getOutbound/owl2lexevs/subClassOf/C53684", String.class);
//		assertThat(json).isNotNull();
//		assertThat(json).isNotBlank();
//		assertThat(json).contains("C3263","C27574","C3810");
//		assertThat(json).doesNotContain("C61410");
//	}
	
	@Test
	public void restControllerTestQueryOutboundConceptInSubset() throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getOutbound/owl2lexevs/Concept_In_Subset/C48323", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("C54453","C117743");
		assertThat(json).doesNotContain("C99999");
	}
	
	@Test
	public void restControllerTestQueryInboundConceptInSubset() throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getInbound/owl2lexevs/Concept_In_Subset/C48323", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("C99999","C99998");
		assertThat(json).doesNotContain("C54453");
	}
	
	@Test
	public void restControllerTestQueryInBoundSubClassOfAltVertexCompleteAccounting()
			throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getInbound/owl2lexevs/subClassOf/Patient", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("HappyPatientDrivingAround","SickPatient",
				"HappyPatientWalkingAround","HealthyPatient","CancerPatient",
				"VerySickPatient","MildlySickPatient","MildlySickCancerPatient",
				"PatientWithCold");
		assertThat(json).doesNotContain("Person");
	}
	
	@Test
	public void restControllerTestQueryOutBoundSubClassOfAltVertexCompleteAccounting()
			throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getOutbound/owl2lexevs/subClassOf/Patient", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("Person");
		assertThat(json).doesNotContain("HappyPatientDrivingAround","SickPatient",
				"HappyPatientWalkingAround","HealthyPatient","CancerPatient",
				"VerySickPatient","MildlySickPatient","MildlySickCancerPatient",
				"PatientWithCold", "TotalPerson");
	}
	
	@Test
	public void restControllerTestQueryInBoundSubClassOfAltVertexCompleteAccountingPlus()
			throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getInbound/owl2lexevs/subClassOf/Person", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("HappyPatientDrivingAround","SickPatient",
				"HappyPatientWalkingAround","HealthyPatient","CancerPatient",
				"VerySickPatient","MildlySickPatient","MildlySickCancerPatient",
				"PatientWithCold", "TotalPerson");
	}
	
	
	@Test
	public void restControllerTestQueryInBoundSubClassOfDepthTest()
			throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getInbound/10/owl2lexevs/subClassOf/Disease", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("Cold","NeoplasticDisease",
				"TumorBenign","TumorMalignant");
	}
	
	@Test
	public void restControllerTestQueryInBoundSubClassOfSmallerDepthTest()
			throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getInbound/1/owl2lexevs/subClassOf/Disease", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("Cold","NeoplasticDisease");
	}
	
	@Test
	public void restControllerTestQueryOutBoundSubClassOfDepthTest()
			throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getOutbound/10/owl2lexevs/subClassOf/NeoplasticDisease", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("Disease","DiseasesDisordersFindings");
	}
	
	@Test
	public void restControllerTestQueryOutBoundSubClassOfSmallerDepthTest()
			throws DataAccessException, Exception{
		String json = this.restTemplate.getForObject("http://localhost:" + port + "getOutbound/1/owl2lexevs/subClassOf/NeoplasticDisease", String.class);
		assertThat(json).isNotNull();
		assertThat(json).isNotBlank();

		assertThat(json).contains("Disease");
	}
}
