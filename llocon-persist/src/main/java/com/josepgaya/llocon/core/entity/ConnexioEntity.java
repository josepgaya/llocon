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
import com.josepgaya.llocon.logic.api.dto.Connexio;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entitat de base de dades que representa una connexió amb un proveidor.
 * 
 * @author josepgaya
 */
@Entity
@Table(name = "connexio")
@AttributeOverrides({
	@AttributeOverride(name = "embedded.nom", column = @Column(name = "nom", length = 100, nullable = false)),
	@AttributeOverride(name = "embedded.proveidor", column = @Column(name = "proveidor", nullable = false)),
	@AttributeOverride(name = "embedded.usuari", column = @Column(name = "usuari", length = 64, nullable = false)),
	@AttributeOverride(name = "embedded.contrasenya", column = @Column(name = "contrasenya", length = 64, nullable = false))
})
@Getter
@Setter(value = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ConnexioEntity extends AbstractAuditableVersionableEntity<Connexio, Long> {

	@Embedded
	protected Connexio embedded;

	@Builder
	public ConnexioEntity(Connexio embedded) {
		this.embedded = embedded;
	}

	@Override
	public void update(Connexio embedded) {
		this.embedded = embedded;
	}

}
