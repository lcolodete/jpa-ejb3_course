package exercicio26.controleTransacao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
public @interface ExcecaoDeAplicacao 
{	boolean rollback() default true;
}
