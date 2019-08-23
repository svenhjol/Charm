package svenhjol.meson.iface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module
{
    String mod();
    String category();
    String name() default "";
    String description() default "";

    boolean hasSubscriptions() default false;
    boolean enabledByDefault() default true;
}
