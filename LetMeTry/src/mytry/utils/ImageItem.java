package mytry.utils;

import java.io.Serializable;

import com.parse.ParseObject;

public class ImageItem implements Serializable{

	private static final long serialVersionUID = 1L;
    private ParseObject parse;
	public ParseObject getParse() {
		return parse;
	}
	public void setParse(ParseObject parse) {
		this.parse = parse;
	}
    
}
