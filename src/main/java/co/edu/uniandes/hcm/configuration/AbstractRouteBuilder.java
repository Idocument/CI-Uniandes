package co.edu.uniandes.hcm.configuration;


import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.sql.SqlComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jndi.JndiTemplate;

import com.fasterxml.jackson.core.JsonParseException;

import co.edu.uniandes.hcm.util.Constants;

/**
 * Base class for routes. Contains global configuration for routes.
 */
public abstract class AbstractRouteBuilder extends RouteBuilder {

	public Logger LOG = LoggerFactory.getLogger(Constants.LOG_NAME);
//	public DataSource datasource;
	/*
	 * Default Configuration for camel routes
	 */
	@Override
	public void configure() throws Exception {
		// Inject datasource
		JndiTemplate jndi = new JndiTemplate();
//		datasource = (DataSource)jndi.lookup("osgi:service/jdbc/uni_andes_banner9");	
//		SqlComponent sqlHcm = new SqlComponent();
//		sqlHcm.setDataSource(datasource);
//		getContext().addComponent(Constants.SQL_COMPONENT, sqlHcm);
		// Inject properties in camel context
		PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
		pc.setLocation("ref:props");		

		// Default Exception Handler
		onException(Exception.class)
				.transform(simple("${exception.message}\n\nStacktrace Details:\n\n${exception.stacktrace}"))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Constants.CODE_INTERNAL_ERROR))
				.log(LoggingLevel.ERROR, LOG, "Error procesando información: ${exception.message}");

		onException(JsonParseException.class)
				.transform(simple("${exception.message}\n\nStacktrace Details:\n\n${exception.stacktrace}"))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Constants.CODE_BAD_REQUEST))
				.log(LoggingLevel.ERROR, LOG, "Error procesando información: ${exception.message}");
	}


}
