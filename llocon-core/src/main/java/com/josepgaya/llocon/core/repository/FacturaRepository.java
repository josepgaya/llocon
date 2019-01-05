/**
 * 
 */
package com.josepgaya.llocon.core.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.josepgaya.llocon.core.dto.FacturaEstatEnum;
import com.josepgaya.llocon.core.dto.SubministramentProducteEnum;
import com.josepgaya.llocon.core.dto.SubministramentProveidorEnum;
import com.josepgaya.llocon.core.entity.FacturaEntity;
import com.josepgaya.llocon.core.entity.SubministramentEntity;

/**
 * Repository per a la gestió de factures.
 * 
 * @author josepgaya
 */
public interface FacturaRepository extends JpaRepository<FacturaEntity, Long> {

	FacturaAmbSubministrament findBySubministramentLloguerIdAndId(
			Long lloguerId,
			Long id);

	Page<FacturaAmbSubministrament> findBySubministramentLloguerId(
			Long lloguerId,
			Pageable pageable);

	FacturaEntity findBySubministramentAndNumero(
			SubministramentEntity subministrament,
			String numero);

	/*Page<FacturaEntity> findBySubministramentId(
			Long subministramentId,
			Pageable pageable);

	List<FacturaEntity> findBySubministramentLloguerAndEstatOrderByDataDesc(
			LloguerEntity lloguer,
			FacturaEstatEnum estat);

	Page<FacturaEntity> findBySubministramentLloguerCodi(
			@Param("lloguerCodi") String lloguerCodi,
			Pageable pageable);*/

	@Query(	"select " +
			"    f.subministrament.lloguer.id as lloguerId, " +
			"    sum(f.importt) as importPendent " +
			"from " +
			"    FacturaEntity f " +
			"where " +
			"    f.subministrament.lloguer.id in (:lloguerIds) " +
			"and (f.estat <> :estat) " +
			"group by " +
			"    f.subministrament.lloguer.id ")
	List<LloguerIdAmbImportPendent> sumImportBySubministramentLloguerIdAndNotEstat(
			@Param("lloguerIds") List<Long> lloguerIds,
			@Param("estat") FacturaEstatEnum estat);

	interface FacturaAmbSubministrament {
		Long getId();
		String getNumero();
		Date getData();
		BigDecimal getImportt();
		FacturaEstatEnum getEstat();
		SubministramentAmbConnexio getSubministrament();
	}
	interface SubministramentAmbConnexio {
		Long getId();
		SubministramentProducteEnum getProducte();
		String getContracteNum();
		Date getDarreraActualitzacio();
		Connexio getConnexio();
	}
	interface Connexio {
		Long getId();
		SubministramentProveidorEnum getProveidor();
	}


	interface LloguerIdAmbImportPendent {
		Long getLloguerId();
		BigDecimal getImportPendent();
	}

}
