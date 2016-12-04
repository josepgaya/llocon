/**
 * 
 */
package com.josepgaya.llocon.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Entitat de base de dades que representa un subministrament.
 * 
 * @author josepgaya
 */
@Entity
@Table(name = "submin")
public class SubministramentEntity extends AbstractPersistable<Long> {

	@Column(name = "producte", nullable = false)
	private SubministramentProducteEnum producte;
	@Column(name = "contracte_num", length = 64, nullable = false)
	private String contracteNum;
	@Column(name = "darrera_act")
	private Date darreraActualitzacio;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(
			name = "lloguer_id",
			foreignKey = @ForeignKey(name = "fk_lloguer_submin"))
	private LloguerEntity lloguer;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(
			name = "connexio_id",
			foreignKey = @ForeignKey(name = "fk_connexio_submin"))
	private ConnexioEntity connexio;



	public SubministramentProducteEnum getProducte() {
		return producte;
	}
	public String getContracteNum() {
		return contracteNum;
	}
	public Date getDarreraActualitzacio() {
		return darreraActualitzacio;
	}
	public LloguerEntity getLloguer() {
		return lloguer;
	}
	public ConnexioEntity getConnexio() {
		return connexio;
	}

	public void update(
			ConnexioEntity connexio,
			SubministramentProducteEnum producte,
			String contracteNum) {
		this.connexio = connexio;
		this.producte = producte;
		this.contracteNum = contracteNum;
	}
	public void updateDarreraActualitzacio(
			Date darreraActualitzacio) {
		this.darreraActualitzacio = darreraActualitzacio;
	}

	public static Builder getBuilder(
			LloguerEntity lloguer,
			ConnexioEntity connexio,
			SubministramentProducteEnum producte,
			String contracteNum) {
		return new Builder(
				lloguer,
				connexio,
				producte,
				contracteNum);
	}

	public static class Builder {
		SubministramentEntity built;
		Builder(
				LloguerEntity lloguer,
				ConnexioEntity connexio,
				SubministramentProducteEnum producte,
				String contracteNum) {
			built = new SubministramentEntity();
			built.lloguer = lloguer;
			built.connexio = connexio;
			built.producte = producte;
			built.contracteNum = contracteNum;
		}
		public SubministramentEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((connexio == null) ? 0 : connexio.hashCode());
		result = prime * result + ((contracteNum == null) ? 0 : contracteNum.hashCode());
		result = prime * result + ((lloguer == null) ? 0 : lloguer.hashCode());
		result = prime * result + ((producte == null) ? 0 : producte.hashCode());
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
		SubministramentEntity other = (SubministramentEntity) obj;
		if (connexio == null) {
			if (other.connexio != null)
				return false;
		} else if (!connexio.equals(other.connexio))
			return false;
		if (contracteNum == null) {
			if (other.contracteNum != null)
				return false;
		} else if (!contracteNum.equals(other.contracteNum))
			return false;
		if (lloguer == null) {
			if (other.lloguer != null)
				return false;
		} else if (!lloguer.equals(other.lloguer))
			return false;
		if (producte != other.producte)
			return false;
		return true;
	}

	private static final long serialVersionUID = -3350731354190395696L;

}
