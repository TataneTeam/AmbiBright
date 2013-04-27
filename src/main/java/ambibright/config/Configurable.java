package ambibright.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Configurable {
	String key();

	String label();

	String description() default "";

	String defaultValue();

	boolean forceValue() default false;

	String group() default Config.GROUP_CONFIG;

}
