package org.um.feri.ears.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AlgorithmParameter {
    String name() default "";
    String description() default "";
    String min() default "";
    String max() default "";
}
