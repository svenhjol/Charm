package svenhjol.charm.base.iface;

import svenhjol.charm.base.CharmClientModule;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {
    boolean alwaysEnabled() default false;

    boolean enabledByDefault() default true;

    String description() default "";

    String mod() default "";

    Class<? extends CharmClientModule> client() default CharmClientModule.class;
}
