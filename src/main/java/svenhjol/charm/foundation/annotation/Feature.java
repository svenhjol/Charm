package svenhjol.charm.foundation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Feature {
    int priority() default 0;

    String description() default "";

    boolean enabledByDefault() default true;

    boolean canBeDisabled() default true;
}
