/**
 * 
 */
package com.josepgaya.llocon.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.dto.ArxiuDto;
import com.josepgaya.llocon.service.LloguerService;

/**
 * Controlador per als lloguers.
 * 
 * @author josepgaya
 */
@RestController
@RequestMapping("/lloguer")
public class LloguerController {

	@Autowired
	private LloguerService lloguerService;



	@RequestMapping("/{lloguerCodi}/csvPendent")
    public void csvPendent(
    		HttpServletResponse response,
    		@PathVariable String lloguerCodi) throws Exception {
        ArxiuDto arxiu = lloguerService.generarCsvPendent(lloguerCodi);
        response.setContentType(arxiu.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + arxiu.getNom());
        response.getOutputStream().write(arxiu.getContingut());
    }

}
