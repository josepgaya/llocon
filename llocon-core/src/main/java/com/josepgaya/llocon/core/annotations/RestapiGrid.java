/**
 * 
 */
package com.josepgaya.llocon.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotació per a configurar un grid associat a un formulari.
 * 
 * @author josepgaya
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestapiGrid {

	public String value();

}
