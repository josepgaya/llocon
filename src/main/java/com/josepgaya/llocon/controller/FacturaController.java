/**
 * 
 */
package com.josepgaya.llocon.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.dto.ArxiuDto;
import com.josepgaya.llocon.entity.FacturaEstatEnum;
import com.josepgaya.llocon.service.FacturaService;

/**
 * Controlador per a les factures.
 * 
 * @author josepgaya
 */
@RestController
@RequestMapping("/factura")
public class FacturaController {

	@Autowired
	private FacturaService facturaService;
	
	@RequestMapping("/importPendent/{lloguerCodi}")
    public BigDecimal importPendent(
    		@PathVariable String lloguerCodi) {
        return facturaService.calcularImportPendent(lloguerCodi);
    }

	@RequestMapping("/{facturaId}/canviEstat")
    public FacturaEstatEnum canviEstat(
    		@PathVariable Long facturaId) {
        return facturaService.canviEstat(facturaId);
    }

	@RequestMapping("/{facturaId}/descarregar")
    public void descarregar(
    		HttpServletResponse response,
    		@PathVariable Long facturaId) throws Exception {
        ArxiuDto arxiu = facturaService.descarregar(facturaId);
        response.setContentType(arxiu.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + arxiu.getNom());
        response.getOutputStream().write(arxiu.getContingut());
    }

}
