import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

// tiger1234.team1234@gmail.com
// pw: tigerteam1234
// testmail: breck.kasandra@0hcow.com

public class LeadsController {
	private Get_XML getxml;
	private Dolibarr_SQL dolisql;
	private Properties pp;
	private String[] array = null;
	private File file;
	private static FileHandler fh;
	private String logFileName;
	private Logger logger = Logger.getLogger(LeadsController.class.getName());
	
	public static void main(String[] args) throws Exception{
		new LeadsController();
	}
	
	private void loggerMethod() throws SecurityException, IOException {
		logger = Logger.getLogger("InfoLogging");
		
		DateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		
		fh = new FileHandler("logs/log"+dt.format(date)+".txt");
		file = new File("logs/log"+dt.format(date)+".txt");
		logFileName = file.toString();
		logger.addHandler(fh);
		SimpleFormatter simpleFormat = new SimpleFormatter();
		fh.setFormatter(simpleFormat);
		
		logger.setLevel(Level.ALL);
	}

	public LeadsController() throws Exception{
		loggerMethod(); //Startar loggning
		getConfig(); //Hämtar config och läser in
		
		try{
			getxml = new Get_XML();
			logger.log(Level.INFO, "Get_XML Connection OK");
		}catch(Exception e){
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		}
		
		try{
			dolisql = new Dolibarr_SQL();
			logger.log(Level.INFO, "Dolibarr_SQL Connection OK");
		}catch(Exception e){
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		}
		
		try {
			Control();
		} catch (SAXException e) {
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		}
	}
	
	public void Control() throws Exception{	//kontrollerar programmet
		try {
			getxml.connection();
			logger.log(Level.INFO, "XML data get OK");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		} catch (SAXException e) {
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		}
		
		try{
			boolean be;
			boolean bDel = true;
			
			for (int i = 0; i < getxml.getNListLength(); i++) {
				array = getxml.getNode(i);
				
				if(array != null){
					array = getxml.filterLead(array);
					
					be = dolisql.sqlChecker(array); 	//kollar så att inte leads redan finns
					
					if(be==false){
						logger.log(Level.SEVERE, "Unexpected error: Duplicates found! Check XML response"); //stänger av program om duplicates hittas, jämför xml och db
						sendEmail();
						System.exit(1);
					}
					
					if(bDel){
						dolisql.sqlDelete(); 				//tar bort alla leads med status STÄNGD & och ej kontakade eller customer/prospects valet som inte ska användas
						System.out.println("KÖR DELTE    xD      : ^D");
						bDel = false;
					}
					
					if(be){
						dolisql.sqlInsert(array); 		// lägger till det SQLChecker tillåter	
					}
					
				}
			}
			logger.log(Level.INFO, "SQL duplicate check OK - SQL delete OK - SQL insert OK");
		}catch(Exception e){
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		}
	}
	
	public void getConfig() throws IOException{	//öppnar config filen
		String fileName = "config.config";
		InputStream is = null;
		pp = new Properties();
		
		try {
			is = new FileInputStream(fileName);
			logger.log(Level.INFO, "Config file read OK");
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		}
		
		try {
			pp.load(is);
			logger.log(Level.INFO, "Config properties loaded OK");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unexpected error: ", e);
			sendEmail();
			e.printStackTrace();
		}
	}
	
	public void sendEmail() throws UnsupportedEncodingException{ 		//skickar mail, använd för rapportera fel i system genom loggning
		Properties mailpp = new Properties();
		String to = pp.getProperty("emailTo").toString();
		String to1 = pp.getProperty("emailTo2").toString();
	

        final String from = pp.getProperty("emailFrom").toString();
        
        mailpp.put("mail.smtp.starttls.enable", "true");
        mailpp.put("mail.smtp.auth", "true");
        mailpp.put("mail.smtp.host", "smtp.gmail.com");
        mailpp.put("mail.smtp.port", "587");

        Session session = Session.getInstance(mailpp,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, "tigerteam1234");
                    }
                });

        MimeMessage message = new MimeMessage(session);
        
        try {

			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(to1));
	        message.setSubject(logFileName);
	        message.setText(logFileName);
	        
	        FileDataSource fileDataSource = new FileDataSource(file);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(fileDataSource.getName());
            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
	        
	        Transport.send(message);
	        logger.log(Level.INFO, "Mail sent, recipient: "+ pp.getProperty("emailTo") +" "+ pp.getProperty("emailTo2"));
		} catch (AddressException e) {
			logger.log(Level.SEVERE, "Cannot send mail: Unexpected error: ", e);
			e.printStackTrace();
		} catch (MessagingException e) {
			logger.log(Level.SEVERE, "Cannot send mail: Unexpected error: ", e);
			e.printStackTrace();
		}
    }
}
