package dev.ui.util;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@RequestScoped//without this bean defining annotation, CDI was not kicking in. CDI would not treat this as a bean unless annotated with BeanDefining Annotation
//this a producer method; it returns an injectable logger
public class LoggerProducer {
	@Produces	
	public Logger produceLogger(InjectionPoint injectionPoint ) {//the callers context gets here
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());			//this injectable resource will be produced
	}
}
