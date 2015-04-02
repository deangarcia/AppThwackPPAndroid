package fileReaders;

import java.io.FileReader;
import java.util.Iterator;

import objects.Accounts;
import objects.MVPD;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import driver.AndroidLaunch;

public abstract class JsonRead extends AndroidLaunch {
	
	private static Accounts account;
	private static MVPD mvp;	
	
	public static void readJsonFromFile(String filePath){
		
		try {
			
			FileReader reader = new FileReader(filePath);
		    JSONParser jsonParser = new JSONParser();
		    JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		    JSONArray mvpds = (JSONArray) jsonObject.get("mvpds");
		    Iterator i = mvpds.iterator();
		    while ( mvpds.iterator().hasNext()) {
		    	
		    	JSONObject mvpJsonObject = (JSONObject)i.next();
		    	mvp = new MVPD(mvpJsonObject.get("name").toString());
		    	JSONObject accounts = (JSONObject) mvpJsonObject.get("accounts");
		    	JSONArray stage = (JSONArray) accounts.get("stage");
		    	JSONArray prod = (JSONArray) accounts.get("prod");
		    	Iterator j = stage.iterator();
		    	while (j.hasNext()){
		    	
		    		JSONObject obj = (JSONObject) j.next();
		    		account = new Accounts(obj.get("username").toString(),obj.get("password").toString());
		    		mvp.addAccounts(account);
		    	}	
		    	j = prod.iterator();
		    	while (j.hasNext()) {
		    	
		    		JSONObject obj = (JSONObject) j.next();
		    		account = new Accounts(obj.get("username").toString(),obj.get("password").toString());
		    		mvp.addAccounts(account);	
		    	}
		    	mvpd.add(mvp);    		
		    }
		    
		} catch (Exception e){
		        	//System.out.println(e.getStackTrace().toString());
		}
		 
	}

}