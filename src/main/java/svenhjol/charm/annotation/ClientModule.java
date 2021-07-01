package svenhjol.charm.annotation;

import svenhjol.charm.loader.CharmCommonModule;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClientModule {
    int priority() default 0;

    Class<? extends CharmCommonModule> module() default CharmCommonModule.class;
}
