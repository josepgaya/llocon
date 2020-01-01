/**
 * 
 */
package com.josepgaya.llocon.logic.api.dto;

import java.io.Serializable;

/**
 * Interfície que han d'implementar tots els DTOs identificables.
 * 
 * @author josepgaya
 */
public interface Identificable<ID extends Serializable> {

	public ID getId();
	public String getDefaultIdentifier();

}
