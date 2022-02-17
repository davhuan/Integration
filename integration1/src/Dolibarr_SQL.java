import java.sql.*;
import java.util.Calendar;

public class Dolibarr_SQL {
	
	public Dolibarr_SQL() throws Exception{
		System.out.println("Dolibarr_SQL konstruktor");
	}
	
    public void sqlDelete() throws SQLException, ClassNotFoundException{		//Delete start
    	String jbdcDriver = "com.mysql.jdbc.Driver"; //ez
	    String dolibarrURL = "jdbc:mysql://localhost:3306/dolibarr"; //ez
	    Class.forName(jbdcDriver);
	    Connection conn = DriverManager.getConnection(dolibarrURL, "dolibarrmysql", "tiger"); //lösenord kan skilja sig åt, standard är changeme
	    String queryDel = null;
	    
	    queryDel = "DELETE FROM llx_societe WHERE client IN (0, 3) or status = 0;"; //tar bort icke kunder / prospects och stängda leads
	    
	    PreparedStatement prepDel = conn.prepareStatement(queryDel);
      	
      	prepDel.executeUpdate();
      	conn.close();
    }
    
	public boolean sqlChecker(String[] arry) throws SQLException, ClassNotFoundException{
	    String jbdcDriver = "com.mysql.jdbc.Driver"; //ez
	    String dolibarrURL = "jdbc:mysql://localhost:3306/dolibarr"; //ez
	    Class.forName(jbdcDriver);
	    Connection conn = DriverManager.getConnection(dolibarrURL, "dolibarrmysql", "tiger"); //lösenord kan skilja sig åt, standard är changeme
	    
	    String query = "SELECT * FROM llx_societe";
	    Statement Stmt = conn.createStatement();
	    ResultSet results = Stmt.executeQuery(query);

		String companyName;
		String alias;
		String phone;
		int id;
		int status;
		String statusString;
		int leadStatus;
		String leadStatusString = "Unknown";
		
		while(results.next()){
			companyName = results.getString("nom");
		    alias = results.getString("name_alias");
		    phone = results.getString("phone");
		    id = results.getInt("rowid");
		    status = results.getInt("status");
		    leadStatus = results.getInt("client");
		    
		    if(status == 0){
		    	statusString = "Closed";
		    }else{
		    	statusString = "Open";
		    }
		    if(leadStatus == 0){
		    	leadStatusString = "Not prospect, or customer";
		    }
		    else if(leadStatus == 1){
		    	leadStatusString = "Customer";
		    }
		    else if(leadStatus == 2){
		    	leadStatusString = "Prospect";
		    }
		    else if(leadStatus == 3){
		    	leadStatusString = "Prospect / Customer"; //använd inte?
		    }
		    
		    if(arry[0].contains(companyName)&&arry[4].contains(alias)&&arry[5].contains(phone)){
		    	System.out.println("-----| ROWID: "+id+" |-----| STATUS: "+statusString+" |-----| CONTACTED: "+leadStatusString+" |-----------");
		    	System.out.println("SQL CHECKER (TRUE): " + companyName + " " + alias + " " + phone + " ");
		    	System.out.println("Duplicates... skipping INSERT into DB!");
		    	System.out.println("-----------------------------------------------------------------------------------------------\n");
		    	conn.close();
		    	return false;
		    }
	    }
		
		conn.close();
		return true;
	}
	
	public void sqlInsert(String[] arry) throws SQLException, ClassNotFoundException{
		String jbdcDriver = "com.mysql.jdbc.Driver"; //ez
		String dolibarrURL = "jdbc:mysql://localhost:3306/dolibarr"; //ez
		Class.forName(jbdcDriver);
		Connection conn = DriverManager.getConnection(dolibarrURL, "dolibarrmysql", "tiger"); //lösenord kan skilja sig åt, standard är changeme
		
		//Insert start
		int index = 0;
		System.out.println(arry[6].toString());
	
		index = Integer.parseInt(arry[6].toString()); // trasig
		
	
		
		if(arry[6]!=null|arry[6]!=""){ 
			if(arry[6].contains("0")|arry[6].contains("")){
				arry[6] = "0";
			}
			if(1<=index&&index<=5){
				arry[6] = "1";
			}
			if(6<=index&&index<=10){
				arry[6] = "2";
			}
			if(11<=index&&index<=50){
				arry[6] = "3";
			}
			if(51<=index&&index<=100){
				arry[6] = "4";
			}
			if(101<=index&&index<=500){
				arry[6] = "5";
			}
			if(501<=index){
				arry[6] = "6";
			}
			
	    	index = Integer.parseInt(arry[6].toString());
		    System.out.println(index);
		}
		
		Calendar c = Calendar.getInstance();
		java.sql.Date currDate = new java.sql.Date(c.getTime().getTime());
		
		String queryInsert = "INSERT INTO llx_societe" //8:e
					+ "(nom, address, zip, town, name_alias, phone, fk_effectif, email, datec ) VALUES"
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		//lite real testing
		//fk_effectif = staff size, där värde 5 är 100 - 500. Kolla i dolibarr, är hårdkodade värden man får välja emellan
		
		PreparedStatement prepStatement = conn.prepareStatement(queryInsert);
	  	
	  	//kanske finns bättre än string då alla inte är string (text) i databasen...
	  	prepStatement.setString (1, arry[0]);
	  	prepStatement.setString (2, arry[1]);
	  	prepStatement.setString (3, arry[2]);
	  	prepStatement.setString (4, arry[3]);
	  	prepStatement.setString (5, arry[4]);
	  	prepStatement.setString (6, arry[5]);
	  	prepStatement.setString (7, arry[6]);
	  	prepStatement.setString (8, arry[7]);
	  	// prepStatement.setString (9, arry[8]);
	  	prepStatement.setDate(9, currDate);
	  	
	  	prepStatement.execute();
	  	conn.close(); //Stäng koppling när man är klar
	}
}
