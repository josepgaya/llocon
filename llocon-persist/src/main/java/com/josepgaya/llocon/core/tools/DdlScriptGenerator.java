/**
 * 
 */
package com.josepgaya.llocon.core.tools;

import com.josepgaya.llocon.core.entity.LloguerEntity;

/**
 * Utilitat per a la generació dels scripts SQL de creació de la BBDD.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class DdlScriptGenerator extends com.josepgaya.base.boot.persist.tools.DdlScriptGenerator {

	public static void main(String[] args) {
		generate("org.hibernate.dialect.HSQLDialect", getAdditionalPackageNames());
	}

	private static String[] getAdditionalPackageNames() {
		return new String[] {
				LloguerEntity.class.getPackage().getName()};
	}

}