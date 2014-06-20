package mytry.xml;

import java.util.List;

import mytry.model.JpgInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JpgListContentHandler extends DefaultHandler{
	 private List<JpgInfo> infos = null;
     private JpgInfo JpgInfo = null;
     private String tagName = null;
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String temp = new String(ch,start,length);
		if(tagName.equals("id")){
			JpgInfo.setId(temp);
		}
		else if(tagName.equals("name")){
			JpgInfo.setName(temp);
		}
		else if(tagName.equals("size")){
		    JpgInfo.setSize(temp);
		}
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("resource")){
		     infos.add(JpgInfo);
		}
		tagName = "";
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		this.tagName = localName;
		if(tagName.equals("resource")){
			JpgInfo = new JpgInfo();
		}
	}

	public List<JpgInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<JpgInfo> infos) {
		this.infos = infos;
	}

	public JpgListContentHandler(List<JpgInfo> infos) {
		super();
		this.infos = infos;
	}
}
