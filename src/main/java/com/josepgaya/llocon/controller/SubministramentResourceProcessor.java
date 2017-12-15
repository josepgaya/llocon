/**
 * 
 */
package com.josepgaya.llocon.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import com.josepgaya.llocon.entity.SubministramentEntity;

/**
 * ResourceProcessor per als subministraments.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class SubministramentResourceProcessor implements ResourceProcessor<Resource<SubministramentEntity>> {

	@Override
	public Resource<SubministramentEntity> process(Resource<SubministramentEntity> resource) {
		resource.add(
				linkTo(methodOn(SubministramentCustomController.class).refrescar(resource.getContent().getId())).withRel("refrescar"));
		return resource;
	}

}
