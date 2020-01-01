/**
 * 
 */
package com.josepgaya.llocon.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.josepgaya.base.boot.persist.entity.AbstractAuditableVersionableEntity;
import com.josepgaya.llocon.logic.api.dto.Lloguer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entitat de base de dades que representa un lloguer.
 * 
 * @author josepgaya
 */
@Entity
@Table(name = "lloguer")
@AttributeOverrides({
	@AttributeOverride(name = "embedded.codi", column = @Column(name = "codi", length = 10, nullable = false)),
	@AttributeOverride(name = "embedded.nom", column = @Column(name = "nom", length = 100, nullable = false)),
	@AttributeOverride(name = "embedded.adressa", column = @Column(name = "adressa", length = 200, nullable = false)),
	@AttributeOverride(name = "embedded.importPendent", column = @Column(name = "import_pend")),
})
@Getter
@Setter(value = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LloguerEntity extends AbstractAuditableVersionableEntity<Lloguer, Long> {

	@Embedded
	protected Lloguer embedded;

	@Builder
	public LloguerEntity(Lloguer embedded) {
		this.embedded = embedded;
	}

	@Override
	public void update(Lloguer embedded) {
		this.embedded = embedded;
	}

}
