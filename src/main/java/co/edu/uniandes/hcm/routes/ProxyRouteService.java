package co.edu.uniandes.hcm.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import co.edu.uniandes.hcm.configuration.AbstractRouteBuilder;
import co.edu.uniandes.hcm.util.Constants;

/**
 * @author Usuario
 * 
 * Generic route for consume Oracle Hcm Services
 *
 */
@Component
public class ProxyRouteService extends AbstractRouteBuilder{

	@Override
	public void configure() throws Exception {
		from(Constants.ROUTE_CONSUME_PROXY).routeId("routeConsumeOracleServices").streamCaching()
		.log(LoggingLevel.INFO, LOG, "Inicia ruta genérica para el consumo proxy de servicios HGC.")
		.log(LoggingLevel.DEBUG, LOG, "Headers de entrada: ${headers}")
		.setProperty("internalPath", simple("{{fuse.internal.path}}"))
		.process(new Processor() {						
			//Camel proccess to obtain path restdl and convert path to Oracle HCM services
			@Override
			public void process(Exchange exchange) throws Exception {				
				String path = (String)exchange.getIn().getHeader("CamelHttpUri");
				String internalPath = (String)exchange.getProperty("internalPath");
				Integer indexPath = path.lastIndexOf(internalPath);
				path = path.substring(indexPath, path.length());
				path = path.substring(path.indexOf("/"), path.length());
				exchange.setProperty("pathService", path);
			}
		})
		.log(LoggingLevel.INFO, LOG, "path del servicio: ${property.pathService}")
		.removeHeaders("Camel*", "CamelHttpQuery")
		.log(LoggingLevel.INFO, LOG, "URL oracle service: https://{{servicio.url}}${property.pathService}?${header.CamelHttpQuery}")
		.toD("http4://{{servicio.url}}${property.pathService}?throwExceptionOnFailure=false?bridgeEndpoint=true")
		.log(LoggingLevel.DEBUG, LOG, "Response del consumo a oracle services HCM: ${body} headers: ${headers}")
		.log(LoggingLevel.INFO, LOG, "Finaliza ruta uniandes-hcm.");
	}

}
