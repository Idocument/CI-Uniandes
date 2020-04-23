package co.edu.uniandes.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import co.edu.uniandes.hcm.routes.ProxyRouteService;
import co.edu.uniandes.hcm.util.Constants;
import co.edu.uniandes.test.util.ConstantsTest;

public class HcmTest extends CamelSpringTestSupport {


	@Produce(uri = Constants.ROUTE_CONSUME_PROXY)
	protected ProducerTemplate producerTemplate;
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;
    protected ProxyRouteService proxyRoute;
    private String uri;
    private Map<String, Object> headers;
    
    @Before
    public void loadHcmTest() {
    	headers = new HashMap<>();
    	headers.put("Authorization", ConstantsTest.authorization);
    	proxyRoute = new ProxyRouteService();
    	uri = "/hcm/fuse-uniandes".concat(ConstantsTest.path_api_version);
    }

	@Test
	public void testHcmUserAccountsFindPrimaryKey() throws Exception {
		headers.put("CamelHttpQuery", ConstantsTest.query_params_primaryKey);		
		headers.put("CamelHttpUri", uri.concat(ConstantsTest.path_api_source_user_accounts));
		invokeProducerEndpoint();
	}
	
	@Test
	public void testHcmWorkers() throws Exception {
		headers.put("CamelHttpQuery", null);		
		headers.put("CamelHttpUri", uri.concat(ConstantsTest.path_api_source_workers));
		invokeProducerEndpoint();
	}

	private void setUpProperties(ModelCamelContext context) {
		PropertiesComponent pc = context.getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:uniandes.hgc.properties");
	}

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/context.xml");
	}

	private void invokeProducerEndpoint() throws Exception, InterruptedException {
		setUpProperties(context);
		context.addRoutes(proxyRoute);
		producerTemplate.sendBodyAndHeaders(null, headers);
		resultEndpoint.assertIsSatisfied();
	}
}
