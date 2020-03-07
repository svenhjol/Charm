package svenhjol.meson.iface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    String mod();

    String category();

    String name() default "";

    String description() default "";

    @SuppressWarnings("unused")
    boolean hasSubscriptions() default false;

    @SuppressWarnings("unused")
    boolean enabledByDefault() default true;

    @SuppressWarnings("unused")
    boolean alwaysEnabled() default false;

    @SuppressWarnings("unused")
    boolean client() default true;

    @SuppressWarnings("unused")
    boolean server() default true;
}
