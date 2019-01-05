/**
 * 
 */
package com.josepgaya.llocon.core.entity;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.josepgaya.llocon.core.dto.SubministramentProveidorEnum;

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
	@Column(name = "usuari", length = 64, nullable = false)
	private String usuari;
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
	public String getContrasenya() throws GeneralSecurityException {
		return desxifrarContrasenya(contrasenya);
	}

	public void update(
			String nom,
			SubministramentProveidorEnum proveidor,
			String usuari,
			String contrasenya) throws GeneralSecurityException {
		this.nom = nom;
		this.proveidor = proveidor;
		this.usuari = usuari;
		this.contrasenya = xifrarContrasenya(contrasenya);
	}

	public static Builder getBuilder(
			String nom,
			SubministramentProveidorEnum proveidor,
			String usuari,
			String contrasenya) throws GeneralSecurityException {
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
				String contrasenya) throws GeneralSecurityException {
			built = new ConnexioEntity();
			built.nom = nom;
			built.proveidor = proveidor;
			built.usuari = usuari;
			built.contrasenya = xifrarContrasenya(contrasenya);
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

	private static final String CLAU_XIFRAT = "L1oc0n";
	private static String xifrarContrasenya(
			String contrasenya) throws GeneralSecurityException {
		byte[] bytes = contrasenya.getBytes(StandardCharsets.UTF_8);
		Cipher cipher = Cipher.getInstance("RC4");
		SecretKeySpec rc4Key = new SecretKeySpec(CLAU_XIFRAT.getBytes(),"RC4");
		cipher.init(Cipher.ENCRYPT_MODE, rc4Key);
		byte[] xifrat = cipher.doFinal(bytes);
		return Base64.getEncoder().encodeToString(xifrat);
	}
	private static String desxifrarContrasenya(String contrasenyaXifrada) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance("RC4");
		SecretKeySpec rc4Key = new SecretKeySpec(CLAU_XIFRAT.getBytes(),"RC4");
		cipher.init(Cipher.DECRYPT_MODE, rc4Key);
		byte[] desxifrat = cipher.doFinal(Base64.getDecoder().decode(contrasenyaXifrada));
		return new String(desxifrat, StandardCharsets.UTF_8);
	}

}
