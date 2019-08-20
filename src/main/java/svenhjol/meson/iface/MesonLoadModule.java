package svenhjol.meson.iface;

public @interface MesonLoadModule
{
    String category();
    String name() default "";
    String description() default "";

    boolean hasSubscriptions() default false;
    boolean enabledByDefault() default true;
}
