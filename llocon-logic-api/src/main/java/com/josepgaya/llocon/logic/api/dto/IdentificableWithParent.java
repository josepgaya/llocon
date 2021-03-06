/**
 * 
 */
package com.josepgaya.llocon.logic.api.dto;

import java.io.Serializable;

/**
 * Interfície que han d'implementar tots els DTOs identificables amb pare.
 * 
 * @author josepgaya
 */
public interface IdentificableWithParent<ID extends Serializable, PID extends Serializable> extends Identificable<ID> {

	public PID getParentId();
	public void setParentId(PID parentId);

}
