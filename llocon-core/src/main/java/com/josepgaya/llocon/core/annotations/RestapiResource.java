package com.josepgaya.llocon.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotació per a configurar el comportament del camp al grid i al formulari.
 * 
 * @author josepgaya
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestapiResource {

	public String descriptionField() default "";
	public RestapiGrid[] grids() default {};

}
