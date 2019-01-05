/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.core.dto.Connexio;
import com.josepgaya.llocon.core.service.ConnexioService;
import com.josepgaya.llocon.core.service.GenericService;

/**
 * Controlador per al servei de gestió de connexions.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RestController
@RequestMapping(value = AbstractApiController.API_PREFIX + ConnexioApiController.API_CONTROLLER_PATH)
public class ConnexioApiController extends AbstractApiController<Connexio, Long> {

	public static final String API_CONTROLLER_PATH = "/connexions";

	@Autowired
	private ConnexioService connexioService;

	@Override
	protected GenericService<Connexio, Long> getService() {
		return connexioService;
	}

	@Override
	protected Class<Connexio> getDtoClass() {
		return Connexio.class;
	}

}
