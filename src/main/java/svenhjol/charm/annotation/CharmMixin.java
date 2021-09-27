package svenhjol.charm.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CharmMixin {
    String[] enableIfModsPresent() default { };

    String[] disableIfModsPresent() default { };

    boolean required() default false;
}
