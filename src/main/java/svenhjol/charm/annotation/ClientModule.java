package svenhjol.charm.annotation;

import svenhjol.charm.loader.CommonModule;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClientModule {
    int priority() default 0;

    Class<? extends CommonModule> module() default CommonModule.class;
}
