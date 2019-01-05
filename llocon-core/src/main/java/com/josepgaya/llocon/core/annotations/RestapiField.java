package com.josepgaya.llocon.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotació per a configurar el comportament del camp al grid i al formulari.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RestapiField {

	public RestapiFieldType value() default RestapiFieldType.AUTO;
	public RestapiFieldType type() default RestapiFieldType.AUTO;
	public boolean disabledForCreate() default false;
	public boolean disabledForUpdate() default false;
	public boolean hiddenInGrid() default false;
	public boolean hiddenInForm() default false;
	public boolean hiddenInLov() default false;
	public boolean lovWithDetailInput() default false;

	public static enum RestapiFieldType {
		AUTO,
		STRING,
		INTEGER,
		FLOAT,
		BOOLEAN,
		DATE,
		BIGDECIMAL,
		TEXTAREA,
		PASSWORD,
		ENUM,
		LOV
	}

}
