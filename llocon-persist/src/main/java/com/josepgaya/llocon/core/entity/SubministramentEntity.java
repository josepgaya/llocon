/**
 * 
 */
package com.josepgaya.llocon.core.entity;

import java.util.Date;

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
import com.josepgaya.llocon.logic.api.dto.Subministrament;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entitat de base de dades que representa un subministrament.
 * 
 * @author josepgaya
 */
@Entity
@Table(name = "submin")
@AttributeOverrides({
	@AttributeOverride(name = "embedded.producte", column = @Column(name = "producte", nullable = false)),
	@AttributeOverride(name = "embedded.contracteNum", column = @Column(name = "contracte_num", length = 64, nullable = false)),
	@AttributeOverride(name = "embedded.darreraActualitzacio", column = @Column(name = "darrera_act")),
})
@Getter
@Setter(value = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SubministramentEntity extends AbstractAuditableVersionableEntity<Subministrament, Long> {

	@Embedded
	protected Subministrament embedded;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(
			name = "lloguer_id",
			foreignKey = @ForeignKey(name = "fk_submin_lloguer"))
	private LloguerEntity lloguer;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(
			name = "connexio_id",
			foreignKey = @ForeignKey(name = "fk_submin_connexio"))
	private ConnexioEntity connexio;

	@Builder
	public SubministramentEntity(
			Subministrament embedded,
			LloguerEntity lloguer,
			ConnexioEntity connexio) {
		this.embedded = embedded;
		this.lloguer = lloguer;
		this.connexio = connexio;
	}

	@Override
	public void update(Subministrament embedded) {
		this.embedded = embedded;
	}
	public void updateLloguer(LloguerEntity lloguer) {
		this.lloguer = lloguer;
	}
	public void updateConnexio(ConnexioEntity connexio) {
		this.connexio = connexio;
	}
	public void updateDarreraActualitzacio(Date darreraActualitzacio) {
		this.embedded.setDarreraActualitzacio(darreraActualitzacio);
	}

}
