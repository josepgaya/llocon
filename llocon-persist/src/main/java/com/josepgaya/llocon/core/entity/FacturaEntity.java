/**
 * 
 */
package com.josepgaya.llocon.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.josepgaya.base.boot.persist.entity.AbstractAuditableVersionableEntity;
import com.josepgaya.llocon.logic.api.dto.Factura;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entitat de base de dades que representa una factura.
 * 
 * @author josepgaya
 */
@Entity
@Table(name = "factura")
@AttributeOverrides({
	@AttributeOverride(name = "embedded.numero", column = @Column(name = "numero", length = 64, nullable = false)),
	@AttributeOverride(name = "embedded.data", column = @Column(name = "data", nullable = false)),
	@AttributeOverride(name = "embedded.importt", column = @Column(name = "import", nullable = false)),
	@AttributeOverride(name = "embedded.estat", column = @Column(name = "estat", nullable = false))
})
@Getter
@Setter(value = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class FacturaEntity extends AbstractAuditableVersionableEntity<Factura, Long> {

	@Embedded
	protected Factura embedded;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(
			name = "subministrament_id",
			foreignKey = @ForeignKey(name = "fk_submin_factura"))
	private SubministramentEntity subministrament;

	@Builder
	public FacturaEntity(
			Factura embedded,
			SubministramentEntity subministrament) {
		this.embedded = embedded;
		this.subministrament = subministrament;
	}

	@Override
	public void update(Factura embedded) {
		this.embedded = embedded;
	}
	public void updateSubministrament(SubministramentEntity subministrament) {
		this.subministrament = subministrament;
	}

}
