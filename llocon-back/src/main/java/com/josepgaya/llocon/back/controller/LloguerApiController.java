/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.core.dto.Lloguer;
import com.josepgaya.llocon.core.service.GenericService;
import com.josepgaya.llocon.core.service.LloguerService;

/**
 * Controlador per al servei de gestió de lloguers.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RestController
@RequestMapping(value = AbstractApiController.API_PREFIX + LloguerApiController.API_CONTROLLER_PATH)
public class LloguerApiController extends AbstractApiController<Lloguer, Long> {

	public static final String API_CONTROLLER_PATH = "/lloguers";

	@Autowired
	private LloguerService lloguerService;

	@Override
	protected GenericService<Lloguer, Long> getService() {
		return lloguerService;
	}

	@Override
	protected Class<Lloguer> getDtoClass() {
		return Lloguer.class;
	}

}
