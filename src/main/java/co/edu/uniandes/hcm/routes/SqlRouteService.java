package co.edu.uniandes.hcm.routes;

import org.apache.camel.LoggingLevel;
import org.springframework.stereotype.Component;

import co.edu.uniandes.hcm.configuration.AbstractRouteBuilder;
import co.edu.uniandes.hcm.util.Constants;

@Component
public class SqlRouteService extends AbstractRouteBuilder {
	@Override
	public void configure() throws Exception {
		from(Constants.ROUTE_CONSUME_SQL).routeId("routeConsumeSql").streamCaching()
		.log(LoggingLevel.INFO, LOG, "Inicia ruta consumo sql prueba.")
		.to("sqlHcm:select * from usuario")
		.log(LoggingLevel.INFO, LOG, "Resultado query: ${body}");
		
	}

}
