import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Get_XML {
	private String sf;
	private Document doc;
	private String urlString = "", auth = "";
	private URL url = null;
	private HttpURLConnection conn = null;
	private DocumentBuilderFactory factory = null;
	private DocumentBuilder builder = null;
	private NodeList nList = null;
	private String[] array = null;
	
	public String getSF(){
		return sf;
	}
	
	public String[] filterLead(String[] ary) throws Exception{
		Pattern name = Pattern.compile("[^...a-zåäöA-ZÅÄÖ0-9\\&\\: ]");		//korrekt
		Pattern address = Pattern.compile("[^...a-zåäöA-ZÅÄÖ0-9\\: ]");		//korrekt
		Pattern zip = Pattern.compile("[^...0-9]");							//korrekt
		Pattern city = Pattern.compile("[^...a-zåäöA-ZÅÄÖ ]");				//korrekt
		Pattern contact = Pattern.compile("[^...a-zåäöA-ZÅÄÖ0-9 ]");		//korrekt
		Pattern tele = Pattern.compile("[^...0-9]");						//korrekt
		Pattern size = Pattern.compile("[^...0-9\\-]");						//korrekt
		Pattern email = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z0-9]{2,6}$", Pattern.CASE_INSENSITIVE);		//korrekt
		
		Matcher hasSpecial0 = name.matcher(ary[0]);
		Matcher hasSpecial1 = address.matcher(ary[1]);
		Matcher hasSpecial2 = zip.matcher(ary[2]);
		Matcher hasSpecial3 = city.matcher(ary[3]);
		Matcher hasSpecial4 = contact.matcher(ary[4]);
		Matcher hasSpecial5 = tele.matcher(ary[5]);
		Matcher hasSpecial6 = size.matcher(ary[6]);
		Matcher hasSpecial7 = email.matcher(ary[7]);
		
		// VIKTIGT name-
		if(hasSpecial0.find()){
			ary[0] = hasSpecial0.replaceAll("");
		}
		
		// address-
		if(hasSpecial1.find()){
			ary[1] = hasSpecial1.replaceAll("");
		}
		
		// zip-
		if(hasSpecial2.find()){
			ary[2] = hasSpecial2.replaceAll("");
		}
		
		// city-
		if(hasSpecial3.find()){
			ary[3] = hasSpecial3.replaceAll("");
		}
		
		// VIKTIGT contact-
		if(ary[4].length() <= 40){
			if(hasSpecial4.find()){
				ary[4] = hasSpecial4.replaceAll("");
				System.out.println(ary[4]);
			}
		}
		else{
			if(hasSpecial4.find()){
				ary[4] = hasSpecial4.replaceAll("");
			}
			
			if(!(ary[4].length() <= 40)){
				ary[4] = ary[4].substring(0, 40);
			}
		}
		
		// VIKTIGT tele
		if(hasSpecial5.find()){
			ary[5] = hasSpecial5.replaceAll("");
		}
		if(ary[5].substring(0, 4).contains("0046")){
			String getFirst = ary[5];
			if(getFirst.contains("0046")){
					String getSecond = getFirst.replaceFirst("0046", "+46");
					ary[5] = getSecond;
			}
		}
		
		// size
		if(hasSpecial6.find()){
			ary[6] = hasSpecial6.replaceAll("");
		}
		
		if(!hasSpecial7.find()){
			ary[7] = "";
		}
		
		return ary;
	}
	/*
		public void connection() throws SAXException, IOException, ParserConfigurationException{	//connect till postman
		urlString = "http://130.243.27.132:8280/leadsprovider/v1";
		 auth = "Bearer 608dce79ee1f8bd1bcd1d17ffb93db";
		
		url = new URL(urlString);

		conn = (HttpURLConnection)url.openConnection();
		conn.setRequestProperty ("Authorization", auth);
		
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		doc = builder.parse(conn.getInputStream());
		
		//loggning?
		//Loggning finns i leadscontroller 31 maj
	}
	*/
		
	void connection() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {	//egen inför slutdemo
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
	//	doc = builder.parse(new FileInputStream("data/response.xml"));
		doc = builder.parse(new FileInputStream("data/xml-data-clean3.xml"));
	}
	
	public int getNListLength() {	//hämtar längden på xml filen utifrån antal lead
		doc.getDocumentElement().normalize();
		nList = doc.getElementsByTagName("lead");
		array = new String[nList.getLength()];
		
		return nList.getLength();
	}
	
	public String[] getNode(int i) {	//hämtar leads
        Node nNode = nList.item(i);

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) nNode;
            String uid = elem.getAttribute("000"+nList.getLength()); //?

            Node node1 = elem.getElementsByTagName("name").item(0);
            String name = node1.getTextContent();
            array[0] = name;
            if(array[0].isEmpty()){
    			return null;
    		}

            Node node2 = elem.getElementsByTagName("address").item(0);
            String address = node2.getTextContent();
            array[1] = address;

            Node node3 = elem.getElementsByTagName("zip").item(0);
            String zip = node3.getTextContent();
            array[2] = zip;
            
            Node node4 = elem.getElementsByTagName("city").item(0);
            String city = node4.getTextContent();
            array[3] = city;
            
            Node node5 = elem.getElementsByTagName("contact").item(0);
            String contact = node5.getTextContent();
            array[4] = contact;
            if(array[4].isEmpty()){
    			return null;
    		}
            
            Node node6 = elem.getElementsByTagName("tele").item(0);
            String tele = node6.getTextContent();
            array[5] = tele;
            if(array[5].isEmpty()){
    			return null;
    		}
            
            Node node7 = elem.getElementsByTagName("size").item(0);
            String size = node7.getTextContent();
            int index = size.lastIndexOf('-');
            array[6] = size.substring(index + 1, size.length());
            if(array[6].contains("")||array[6]==null){
            	array[6] = "0";
            }
            
            Node node8 = elem.getElementsByTagName("email").item(0);
            String email = node8.getTextContent();
            array[7] = email;
        }
		
		return array;
	}
}
