package co.edu.uniandes.hcm.routes;


import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import co.edu.uniandes.hcm.configuration.AbstractRouteBuilder;
import co.edu.uniandes.hcm.util.Constants;

/**
 * @author Usuario
 * 
 * Class configuration RestDsl. Expose rest service for proxy HCM.
 *
 */
@Component
public class MainRouteService extends AbstractRouteBuilder{
	
	@Override
	public void configure() throws Exception {
		super.configure();
		
		restConfiguration()
		.host("localhost")
		.component("netty-http")
		.bindingMode(RestBindingMode.off)
		.dataFormatProperty("prettyPrint", "true")
		.enableCORS(true)
		.port("{{server.port}}")
		.contextPath("{{camel.component.servlet.mapping.context-path}}")
		
		//swagger information
		.apiContextPath("/api-doc")
		.apiProperty("api.title", "{{api.title}}")
		.apiProperty("api.host", "{{api.host}}")
		.apiProperty("api.description", "{{api.description}}")
		.apiProperty("api.version", "{{api.version}}");
	
		rest("{{resource.rest.path}}")
			.consumes("application/json")
			.produces("application/json")	
			.get("{{uri.get.resource}}{{endpoint.internal.hcm}}").to(Constants.ROUTE_CONSUME_PROXY)	
		
			.get("/sql").to(Constants.ROUTE_CONSUME_SQL);
			
	}

}
