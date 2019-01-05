/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.reflections.Reflections;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.core.annotations.RestapiField;
import com.josepgaya.llocon.core.annotations.RestapiField.RestapiFieldType;
import com.josepgaya.llocon.core.annotations.RestapiGrid;
import com.josepgaya.llocon.core.annotations.RestapiResource;

/**
 * Controlador per a obtenir la informació dels camps d'un DTO necessària
 * per a construir els formularis de la part web.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RestController
@RequestMapping(value = AbstractApiController.API_PREFIX + FormApiController.API_CONTROLLER_PATH)
public class FormApiController {

	public static final String API_CONTROLLER_PATH = "/forms";

	private static final String[] DTO_PACKAGES_TO_SCAN = new String[] {
			"com.josepgaya.llocon.core.dto"
	};

	private static final String[] CONTROLLER_PACKAGES_TO_SCAN = new String[] {
			"com.josepgaya.llocon.back.controller"
	};

	@GetMapping(
			value = "/{resourceClassName}")
	public ResponseEntity<RestapiResourceConfig> metadata(
			@PathVariable final String resourceClassName) {
		try {
			RestapiResourceConfig response = new RestapiResourceConfig();
			Class<?> dtoClass = getDtoClassForName(resourceClassName);
			Class<?> controllerClass = getControllerClassForDto(dtoClass);
			if (controllerClass != null) {
				response.setApiUrl(
						getApiUrl(
								dtoClass,
								controllerClass));
			}
			response.setFields(getFields(dtoClass, true));
			RestapiResource resourceAnnotation = dtoClass.getAnnotation(RestapiResource.class);
			if (resourceAnnotation != null) {
				if (!resourceAnnotation.descriptionField().isEmpty()) {
					response.setDescriptionField(resourceAnnotation.descriptionField());
				}
				if (resourceAnnotation.grids().length > 0) {
					List<RestapiGridConfig> grids = new ArrayList<RestapiGridConfig>();
					for (RestapiGrid grid: resourceAnnotation.grids()) {
						RestapiGridConfig gridConfig = new RestapiGridConfig();
						gridConfig.setResourceName(grid.value());
						grids.add(gridConfig);
					}
					response.setGrids(grids);
				}
			}
			return ResponseEntity.ok(response);
		} catch (ClassNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	private Class<?> getDtoClassForName(
			String resourceClassName) throws ClassNotFoundException {
		Class<?> dtoClass = null;
		for (String packageToScan: DTO_PACKAGES_TO_SCAN) {
			dtoClass = getClassForName(packageToScan + "." + resourceClassName);
			if (dtoClass == null) {
				dtoClass = getClassForName(packageToScan + "." + StringUtils.capitalize(resourceClassName));
			}
			if (dtoClass != null) {
				break;
			}
		}
		if (dtoClass != null) {
			return dtoClass;
		} else {
			throw new ClassNotFoundException("Classe pel recurs " + resourceClassName + " no trobada");
		}
	}

	private Class<?> getClassForName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException ignored) {
			return null;
		}
	}

	private Class<?> getControllerClassForDto(
			Class<?> dtoClass) throws ClassNotFoundException {
		Class<?> controllerClass = null;
		for (String packageToScan: CONTROLLER_PACKAGES_TO_SCAN) {
			Reflections reflections = new Reflections(packageToScan);
			Set<Class<?>> candidats = reflections.getTypesAnnotatedWith(RestController.class);
			for (Class<? extends Object> candidat: candidats) {
				Type t = candidat.getGenericSuperclass();
				if (t != null && t instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType)t;
					Type[] typeArgs = parameterizedType.getActualTypeArguments();
					if (typeArgs.length > 0) {
						if (typeArgs[0].equals(dtoClass)) {
							controllerClass = candidat;
							break;
						}
					}
				}
			}
		}
		return controllerClass;	
	}

	private String getApiUrl(
			Class<?> dtoClass,
			Class<?> controllerClass) throws ClassNotFoundException {
		RequestMapping requestMappingAnnotation = controllerClass.getAnnotation(RequestMapping.class);
		if (requestMappingAnnotation != null) {
			String[] values = requestMappingAnnotation.value();
			if (values.length > 0) {
				return values[0];
			}
		}
		return null;
	}

	private List<RestapiFieldConfig> getFields(
			Class<?> dtoClass,
			boolean emplenarInformacioLovs) {
		List<RestapiFieldConfig> fields = new ArrayList<RestapiFieldConfig>();
		for (Field field: dtoClass.getDeclaredFields()) {
			RestapiFieldConfig fieldConfig = new RestapiFieldConfig();
			fieldConfig.setName(field.getName());
			fieldConfig.setType(getFieldType(field));
			if (field.getAnnotation(NotEmpty.class) != null) {
				fieldConfig.setRequired(true);
			}
			if (field.getAnnotation(NotNull.class) != null) {
				fieldConfig.setRequired(true);
			}
			Size size = field.getAnnotation(Size.class);
			if (size != null) {
				if (size.min() > 0) {
					fieldConfig.setMinLength(size.min());
				}
				if (size.max() < Integer.MAX_VALUE) {
					fieldConfig.setMaxLength(size.max());
				}
			}
			RestapiField restapiField = field.getAnnotation(RestapiField.class);
			if (restapiField != null) {
				RestapiFieldType fieldType = restapiField.type();
				if (fieldType == RestapiFieldType.AUTO) {
					fieldType = restapiField.value();
				}
				if (fieldType != RestapiFieldType.AUTO) {
					fieldConfig.setType(fieldType);
				}
				fieldConfig.setDisabledForCreate(
						restapiField.disabledForCreate());
				fieldConfig.setDisabledForUpdate(
						restapiField.disabledForUpdate());
				fieldConfig.setHiddenInGrid(
						restapiField.hiddenInGrid());
				fieldConfig.setHiddenInForm(
						restapiField.hiddenInForm());
				fieldConfig.setHiddenInLov(
						restapiField.hiddenInLov());
			}
			if (fieldConfig.getType() == RestapiFieldType.ENUM) {
				fieldConfig.setEnumValues(
						field.getType().getEnumConstants());
			}
			if (fieldConfig.getType() == RestapiFieldType.LOV) {
				Class<?> lovResourceClass = field.getType();
				RestapiResource lovResourceAnnotation = lovResourceClass.getAnnotation(RestapiResource.class);
				fieldConfig.setLovResource(lovResourceClass.getSimpleName().toLowerCase());
				if (lovResourceAnnotation != null && !lovResourceAnnotation.descriptionField().isEmpty()) {
					fieldConfig.setLovDetailFieldName(lovResourceAnnotation.descriptionField());
				} else {
					fieldConfig.setLovDetailFieldName("defaultIdentifier");
				}
				fieldConfig.setLovWithDetailInput(restapiField.lovWithDetailInput());
			}
			fields.add(fieldConfig);
		}
		return fields;
	}

	private RestapiFieldType getFieldType(Field field) {
		Class<?> resourceType = field.getType();
		String simpleName = resourceType.getSimpleName();
		if (resourceType.isEnum()) {
			return RestapiFieldType.ENUM;
		} else if ("int".equals(simpleName) || "Integer".equals(simpleName)) {
			return RestapiFieldType.INTEGER;
		} else if ("long".equals(simpleName) || "Long".equals(simpleName)) {
			return RestapiFieldType.INTEGER;
		} else if ("float".equals(simpleName) || "Float".equals(simpleName)) {
			return RestapiFieldType.FLOAT;
		} else if ("double".equals(simpleName) || "Double".equals(simpleName)) {
			return RestapiFieldType.FLOAT;
		} else if ("boolean".equals(simpleName) || "Boolean".equals(simpleName)) {
			return RestapiFieldType.BOOLEAN;
		} else if ("Date".equals(simpleName)) {
			return RestapiFieldType.DATE;
		} else if ("BigDecimal".equals(simpleName)) {
			return RestapiFieldType.BIGDECIMAL;
		} else {
			return RestapiFieldType.STRING;
		}
	}

	public static class RestapiResourceConfig {
		private String apiUrl;
		private String descriptionField;
		List<RestapiFieldConfig> fields;
		List<RestapiGridConfig> grids;
		public String getApiUrl() {
			return apiUrl;
		}
		public void setApiUrl(String apiUrl) {
			this.apiUrl = apiUrl;
		}
		public String getDescriptionField() {
			return descriptionField;
		}
		public void setDescriptionField(String descriptionField) {
			this.descriptionField = descriptionField;
		}
		public List<RestapiFieldConfig> getFields() {
			return fields;
		}
		public void setFields(List<RestapiFieldConfig> fields) {
			this.fields = fields;
		}
		public List<RestapiGridConfig> getGrids() {
			return grids;
		}
		public void setGrids(List<RestapiGridConfig> grids) {
			this.grids = grids;
		}
	}
	public static class RestapiFieldConfig {
		private String name;
		private RestapiFieldType type;
		private boolean required;
		private Integer minLength;
		private Integer maxLength;
		private boolean disabledForCreate;
		private boolean disabledForUpdate;
		private boolean hiddenInGrid;
		private boolean hiddenInForm;
		private boolean hiddenInLov;
		private Object[] enumValues;
		private String lovResource;
		private String lovDetailFieldName;
		private boolean lovWithDetailInput;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public RestapiFieldType getType() {
			return type;
		}
		public void setType(RestapiFieldType type) {
			this.type = type;
		}
		public boolean isRequired() {
			return required;
		}
		public void setRequired(boolean required) {
			this.required = required;
		}
		public Integer getMinLength() {
			return minLength;
		}
		public void setMinLength(Integer minLength) {
			this.minLength = minLength;
		}
		public Integer getMaxLength() {
			return maxLength;
		}
		public void setMaxLength(Integer maxLength) {
			this.maxLength = maxLength;
		}
		public boolean isDisabledForCreate() {
			return disabledForCreate;
		}
		public void setDisabledForCreate(boolean disabledForCreate) {
			this.disabledForCreate = disabledForCreate;
		}
		public boolean isDisabledForUpdate() {
			return disabledForUpdate;
		}
		public void setDisabledForUpdate(boolean disabledForUpdate) {
			this.disabledForUpdate = disabledForUpdate;
		}
		public boolean isHiddenInGrid() {
			return hiddenInGrid;
		}
		public void setHiddenInGrid(boolean hiddenInGrid) {
			this.hiddenInGrid = hiddenInGrid;
		}
		public boolean isHiddenInForm() {
			return hiddenInForm;
		}
		public void setHiddenInForm(boolean hiddenInForm) {
			this.hiddenInForm = hiddenInForm;
		}
		public boolean isHiddenInLov() {
			return hiddenInLov;
		}
		public void setHiddenInLov(boolean hiddenInLov) {
			this.hiddenInLov = hiddenInLov;
		}
		public Object[] getEnumValues() {
			return enumValues;
		}
		public void setEnumValues(Object[] enumValues) {
			this.enumValues = enumValues;
		}
		public String getLovResource() {
			return lovResource;
		}
		public void setLovResource(String lovResource) {
			this.lovResource = lovResource;
		}
		public String getLovDetailFieldName() {
			return lovDetailFieldName;
		}
		public void setLovDetailFieldName(String lovDetailFieldName) {
			this.lovDetailFieldName = lovDetailFieldName;
		}
		public boolean isLovWithDetailInput() {
			return lovWithDetailInput;
		}
		public void setLovWithDetailInput(boolean lovWithDetailInput) {
			this.lovWithDetailInput = lovWithDetailInput;
		}
	}
	public static class RestapiGridConfig {
		private String resourceName;
		public String getResourceName() {
			return resourceName;
		}
		public void setResourceName(String resourceName) {
			this.resourceName = resourceName;
		}
	}

}
