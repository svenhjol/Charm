package svenhjol.charm.base.iface;

import svenhjol.charm.Charm;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {
    boolean alwaysEnabled() default false;

    boolean enabledByDefault() default true;

    String description() default "";

    String mod() default "";

    Class<?> client() default Charm.class;
}
