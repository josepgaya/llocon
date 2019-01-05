/**
 * 
 */
package com.josepgaya.llocon.core.exception;

/**
 * Excepció que es llança quan s'intenta accedir a un objecte
 * amb el pare equivocat.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class WrongParentIdException extends RuntimeException {

	private Object parentId;
	private Object objectId;
	private Class<?> objectClass;

	public WrongParentIdException(
			Object parentId,
			Object objectId,
			Class<?> objectClass) {
		super(getExceptionMessage(
				parentId,
				objectId,
				objectClass));
		this.parentId = parentId;
		this.objectId = objectId;
		this.objectClass = objectClass;
	}

	public Object getParentId() {
		return parentId;
	}
	public Object getObjectId() {
		return objectId;
	}
	public Class<?> getObjectClass() {
		return objectClass;
	}

	public static String getExceptionMessage(
			Object parentId,
			Object objectId,
			Class<?> objectClass) {
		StringBuilder sb = new StringBuilder();
		if (objectClass != null)
			sb.append(objectClass.getName());
		else
			sb.append("null");
		sb.append("#");
		if (objectId != null)
			sb.append(objectId.toString());
		else
			sb.append("null");
		sb.append(", ");
		sb.append(parentId);
		return sb.toString();
	}

}
