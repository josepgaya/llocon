/**
 * 
 */
package com.josepgaya.llocon.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Entitat de base de dades per a una connexió amb un proveidor.
 * 
 * @author josepgaya
 */
@Entity
@Table(name = "factura")
public class FacturaEntity extends AbstractPersistable<Long> {

	@Column(name = "numero", length = 64, nullable = false)
	private String numero;
	@Column(name = "data", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date data;
	@Column(name = "import", nullable = false)
	private BigDecimal importt;
	@Column(name = "estat", nullable = false)
	private FacturaEstatEnum estat;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(
			name = "subministrament_id",
			foreignKey = @ForeignKey(name = "fk_submin_factura"))
	private SubministramentEntity subministrament;

	public String getNumero() {
		return numero;
	}
	public Date getData() {
		return data;
	}
	public BigDecimal getImportt() {
		return importt;
	}
	public FacturaEstatEnum getEstat() {
		return estat;
	}
	public SubministramentEntity getSubministrament() {
		return subministrament;
	}

	public void update(
			FacturaEstatEnum estat) {
		this.estat = estat;
	}

	public static Builder getBuilder(
			SubministramentEntity subministrament,
			String numero,
			Date data,
			BigDecimal importt) {
		return new Builder(
				subministrament,
				numero,
				data,
				importt);
	}

	public static class Builder {
		FacturaEntity built;
		Builder(
				SubministramentEntity subministrament,
				String numero,
				Date data,
				BigDecimal importt) {
			built = new FacturaEntity();
			built.subministrament = subministrament;
			built.numero = numero;
			built.data = data;
			built.importt = importt;
			built.estat = FacturaEstatEnum.PENDENT;
		}
		public FacturaEntity build() {
			return built;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((subministrament == null) ? 0 : subministrament.hashCode());
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
		FacturaEntity other = (FacturaEntity) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (subministrament == null) {
			if (other.subministrament != null)
				return false;
		} else if (!subministrament.equals(other.subministrament))
			return false;
		return true;
	}

	private static final long serialVersionUID = 2905347521394793418L;

}
