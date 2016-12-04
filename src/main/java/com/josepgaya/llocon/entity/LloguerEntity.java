/**
 * 
 */
package com.josepgaya.llocon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Entitat de base de dades que representa un lloguer.
 * 
 * @author josepgaya
 */
@Entity
@Table(name = "lloguer")
public class LloguerEntity extends AbstractPersistable<Long> {

	@Column(name = "codi", length = 64, nullable = false, unique = true)
	private String codi;
	@Column(name = "nom", length = 255, nullable = false)
	private String nom;
	@Column(name = "adressa", length = 255, nullable = false)
	private String adressa;

	public String getCodi() {
		return codi;
	}
	public String getNom() {
		return nom;
	}
	public String getAdressa() {
		return adressa;
	}

	public void update(
			String codi,
			String nom,
			String adressa) {
		this.codi = codi;
		this.nom = nom;
		this.adressa = adressa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((adressa == null) ? 0 : adressa.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LloguerEntity other = (LloguerEntity) obj;
		if (adressa == null) {
			if (other.adressa != null)
				return false;
		} else if (!adressa.equals(other.adressa))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	private static final long serialVersionUID = 4321255948346484173L;

}
