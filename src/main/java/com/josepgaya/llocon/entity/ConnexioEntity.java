/**
 * 
 */
package com.josepgaya.llocon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entitat de base de dades per a una connexió amb un proveidor.
 * 
 * @author josepgaya
 */
@Entity
@Table(name = "connexio")
public class ConnexioEntity extends AbstractPersistable<Long> {

	@Column(name = "nom", length = 255, nullable = false)
	private String nom;
	@Column(name = "proveidor", nullable = false)
	private SubministramentProveidorEnum proveidor;
	@JsonIgnore
	@Column(name = "usuari", length = 64, nullable = false)
	private String usuari;
	@JsonIgnore
	@Column(name = "contrasenya", length = 64, nullable = false)
	private String contrasenya;

	public String getNom() {
		return nom;
	}
	public SubministramentProveidorEnum getProveidor() {
		return proveidor;
	}
	public String getUsuari() {
		return usuari;
	}
	public String getContrasenya() {
		return contrasenya;
	}

	public void update(
			String nom,
			SubministramentProveidorEnum proveidor,
			String usuari,
			String contrasenya) {
		this.nom = nom;
		this.proveidor = proveidor;
		this.usuari = usuari;
		this.contrasenya = contrasenya;
	}

	public static Builder getBuilder(
			String nom,
			SubministramentProveidorEnum proveidor,
			String usuari,
			String contrasenya) {
		return new Builder(
				nom,
				proveidor,
				usuari,
				contrasenya);
	}

	public static class Builder {
		ConnexioEntity built;
		Builder(
				String nom,
				SubministramentProveidorEnum proveidor,
				String usuari,
				String contrasenya) {
			built = new ConnexioEntity();
			built.nom = nom;
			built.proveidor = proveidor;
			built.usuari = usuari;
			built.contrasenya = contrasenya;
		}
		public ConnexioEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contrasenya == null) ? 0 : contrasenya.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((proveidor == null) ? 0 : proveidor.hashCode());
		result = prime * result + ((usuari == null) ? 0 : usuari.hashCode());
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
		ConnexioEntity other = (ConnexioEntity) obj;
		if (contrasenya == null) {
			if (other.contrasenya != null)
				return false;
		} else if (!contrasenya.equals(other.contrasenya))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (proveidor != other.proveidor)
			return false;
		if (usuari == null) {
			if (other.usuari != null)
				return false;
		} else if (!usuari.equals(other.usuari))
			return false;
		return true;
	}

	private static final long serialVersionUID = 2905347521394793418L;

}
