/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.core.dto.Identificable;
import com.josepgaya.llocon.core.dto.IdentificableWithParent;

/**
 * Mètodes bàsics per als controladors REST que gestionen entitats.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RestController
public abstract class CommonApiController<D extends Identificable<ID>, ID extends Serializable> {

	protected abstract Link getSelfLink();

	protected Resource<D> toResource(D dto) {
		Link selfLink = getSelfLink();
		if (dto.getId() != null) {
			if (dto instanceof IdentificableWithParent) {
				@SuppressWarnings("rawtypes")
				IdentificableWithParent dtoWithParent = (IdentificableWithParent)dto;
				selfLink = selfLink.expand(
						dtoWithParent.getId(),
						dtoWithParent.getParentId());
			} else {
				selfLink = selfLink.expand(dto.getId());
			}
		}
		return new Resource<D>(
				dto,
				selfLink);
	}
	protected Resources<Resource<D>> toResources(
			List<D> dtos) {
		return new Resources<Resource<D>>(
				dtos.stream().map(dto -> toResource(dto)).collect(Collectors.toList()));
	}
	protected PagedResources<Resource<D>> toPagedResources(
			Page<D> page) {
		PageMetadata pageMetadata = new PageMetadata(
				page.getNumberOfElements(),
				page.getNumber(),
				page.getTotalElements(),
				page.getTotalPages());
		return new PagedResources<Resource<D>>(
				page.getContent().stream().map(dto -> toResource(dto)).collect(Collectors.toList()),
				pageMetadata);
	}

}
