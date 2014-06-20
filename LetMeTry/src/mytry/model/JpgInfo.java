package mytry.model;

import java.io.Serializable;

public class JpgInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String size;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public JpgInfo(String id, String name, String size) {
		super();
		this.id = id;
		this.name = name;
		this.size = size;
	}
	public JpgInfo() {
		super();
	}
	@Override
	public String toString() {
		return "JpgInfo [id=" + id + ", jpgName=" + name + ", jpgSize="
				+ size +  "]";
	}
}
