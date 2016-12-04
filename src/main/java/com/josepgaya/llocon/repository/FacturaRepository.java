/**
 * 
 */
package com.josepgaya.llocon.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.josepgaya.llocon.entity.FacturaEntity;
import com.josepgaya.llocon.entity.FacturaEstatEnum;
import com.josepgaya.llocon.entity.LloguerEntity;
import com.josepgaya.llocon.entity.SubministramentEntity;
import com.josepgaya.llocon.entity.projection.FacturaAmbSubministrament;

/**
 * Repository per a la gestió de factures.
 * 
 * @author josepgaya
 */
@RepositoryRestResource(
		collectionResourceRel = "factura",
		path = "factura",
		excerptProjection = FacturaAmbSubministrament.class)
public interface FacturaRepository extends JpaRepository<FacturaEntity, Long> {

	List<FacturaEntity> findBySubministramentLloguerOrderByDataDesc(
			LloguerEntity lloguer);

	List<FacturaEntity> findBySubministramentLloguerAndEstatOrderByDataDesc(
			LloguerEntity lloguer,
			FacturaEstatEnum estat);

	Page<FacturaEntity> findBySubministramentLloguerCodi(
			@Param("lloguerCodi") String lloguerCodi,
			Pageable pageable);

	@Query(	"select " +
			"    sum(f.importt) " +
			"from " +
			"    FacturaEntity f " +
			"where " +
			"    f.subministrament.lloguer.codi = :lloguerCodi " +
			"and (f.estat <> :estat)")
	BigDecimal sumImportBySubministramentLloguerCodiAndNotEstat(
			@Param("lloguerCodi") String lloguerCodi,
			@Param("estat") FacturaEstatEnum estat);

	FacturaEntity findBySubministramentAndNumero(
			SubministramentEntity subministrament,
			String numero);

}
