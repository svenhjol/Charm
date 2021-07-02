package svenhjol.charm.annotation;

import svenhjol.charm.loader.CharmModule;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClientModule {
    int priority() default 0;

    Class<? extends CharmModule> module() default CharmModule.class;
}
