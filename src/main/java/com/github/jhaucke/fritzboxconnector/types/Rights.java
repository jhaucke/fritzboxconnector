package com.github.jhaucke.fritzboxconnector.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Type that represents the rights that has been granted to the signed-in user.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Rights")
public class Rights {

	@XmlElement(name = "Name")
	private List<String> nameList;
	@XmlElement(name = "Access")
	private List<String> accessList;

	public List<Right> getRights() {

		List<Right> rights = new ArrayList<Right>();

		for (int i = 0; nameList.size() > i; i++) {
			rights.add(new Right(nameList.get(i), accessList.get(i)));
		}

		return rights;
	}

	/**
	 * This type does actually not exist in the response of the FritzBox, but
	 * makes the handling of the rights more comfortable.
	 */
	class Right {

		private String name;
		private String access;

		public Right(String name, String access) {
			super();
			this.name = name;
			this.access = access;
		}

		public String getName() {
			return name;
		}

		public String getAccess() {
			return access;
		}
	}
}
