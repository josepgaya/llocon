/**
 * 
 */
package com.josepgaya.llocon.logic.api.modules;

import org.springframework.stereotype.Component;

import com.josepgaya.base.boot.logic.api.dto.Profile;
import com.josepgaya.base.boot.logic.api.module.AbstractModules;
import com.josepgaya.llocon.logic.api.dto.Lloguer;

/**
 * Classe que gestiona els diferents mòduls disponibles.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class Modules extends AbstractModules {

	static {
		init(
				new String[] {
				},
				new String[] {
						Profile.class.getPackage().getName(),
						Lloguer.class.getPackage().getName()
				});
	}

}