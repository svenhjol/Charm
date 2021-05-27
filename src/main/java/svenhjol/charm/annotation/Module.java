package svenhjol.charm.annotation;

import svenhjol.charm.module.CharmClientModule;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {
    boolean alwaysEnabled() default false;

    boolean enabledByDefault() default true;

    int priority() default 0;

    String description() default "";

    String mod() default "";

    String[] requiresMixins() default {};

    Class<? extends CharmClientModule> client() default CharmClientModule.class;
}
