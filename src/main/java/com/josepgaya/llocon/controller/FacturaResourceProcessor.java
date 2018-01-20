/**
 * 
 */
package com.josepgaya.llocon.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import com.josepgaya.llocon.entity.FacturaEntity;

/**
 * ResourceProcessor per a les factures.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Component
public class FacturaResourceProcessor implements ResourceProcessor<Resource<FacturaEntity>> {

	@Autowired
    EntityLinks entityLinks;

	@Override
	public Resource<FacturaEntity> process(Resource<FacturaEntity> resource) {
		resource.add(
				linkTo(methodOn(FacturaCustomController.class).canviEstat(resource.getContent().getId())).withRel("canviEstat"));
		resource.add(
				entityLinks.linkForSingleResource(FacturaEntity.class, resource.getContent().getId()).slash("descarregar").withRel("descarregar"));
		return resource;
	}

}
