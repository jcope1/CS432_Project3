
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoMain {
	
	public void printExperienceAverages() {
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		ArrayList<String> exp = new ArrayList<String>();
		
		for (Document doc : jobList) {
            exp.add(doc.getString("Minimum Qual Requirements"));
        }
		
		int highschool = 0;
		int assoc = 0;
		int bacc = 0;
		int masters = 0;
		
		for (String s : exp) {
			s = s.toLowerCase();
			if (s.contains("master")) masters += 1;
			else if (s.contains("baccalaureate") || s.contains("bachelor")) bacc += 1;
			else if (s.contains("associate")) assoc += 1;
			else if (s.contains("high-school") || s.contains("high school")) highschool += 1;
		}
		
		System.out.println("No minimum requirements: " + (((float)exp.size() - (highschool + assoc + bacc + masters)) / exp.size()) * 100 + "%");
		System.out.println("Minimum high-school: " + (highschool / (float)exp.size()) * 100 + "%");
		System.out.println("Minimum associates degree: " + ((assoc) / (float)exp.size()) * 100 + "%");
		System.out.println("Minimum bachelors degree: " + ((bacc) / (float)exp.size()) * 100 + "%");
		System.out.println("Minimum masters degree: " + ((masters) / (float)exp.size()) * 100 + "%");
	}
	
	public void printHighestSalaryRange() {
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		Double maxRange = 0.0;
		Double[] maxEnds = new Double[2];
		
		String maxTitle = "";
		
		for (Document doc : jobList) {
			Double[] range = new Double[2];
			
			Object r1 = doc.get("Salary Range From");
			Object r2 = doc.get("Salary Range To");
			
			//System.out.println(r1.getClass().toString());
			
			if (r1.getClass().toString().contains("Integer")) {
				range[0] = new Double((Integer)r1);
			} else {
				range[0] = (Double)r1;
			}
			
			if (r2.getClass().toString().contains("Integer")) {
				range[1] = new Double((Integer)r2);
			} else {
				range[1] = (Double)r2;
			}
			
			Double r = range[1] - range[0];
			if (r > maxRange && range[0] != 0.0) {
				maxRange = r;
				maxEnds[0] = range[0];
				maxEnds[1] = range[1];
				maxTitle = doc.getString("Business Title");
			}
        }
		
		System.out.println(maxTitle + ": $" + maxRange + " range [$" + maxEnds[0] + ", $" + maxEnds[1] + "]");
	}
	
	public void printLocationHeatmap() {
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		Hashtable<String, Integer> locations = new Hashtable<String, Integer>();
		
		for (Document doc : jobList) {
			String s = doc.getString("Work Location").toLowerCase();
			
			Scanner delim = new Scanner(s);
			delim.useDelimiter(" ");
			while (delim.hasNext()) {
				String keyword = delim.next();
				if (!keyword.matches("[a-z]*") || keyword.equals("street") || keyword.length() <= 3
						|| keyword.equals("york") || keyword.equals("expway") || keyword.equals("blvd")
						|| keyword.equals("junction") || keyword.equals("city")) continue;
				
				if (!locations.containsKey(keyword)) locations.put(keyword, 1);
				else locations.put(keyword, locations.get(keyword) + 1);
			}
        }
		
		Set<String> keys = locations.keySet();
		for (int i = 0; i < 5; i++) {
			int maxOcc = 0;
			String maxKey = "";
			
	        for(String key: keys){
	            if (locations.get(key) > maxOcc) {
	            	maxOcc = locations.get(key);
	            	maxKey = key;
	            }
	        }
	        
	        locations.remove(maxKey);
	        System.out.println(maxKey + ": " + maxOcc + " occurances");
		}
	}
	
	public void printJobTrends() {
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Date> dates = new ArrayList<Date>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		for (Document doc : jobList) {
			String n = doc.getString("Business Title");
			Date d;
			try {
				d = sdf.parse(doc.getString("Posting Date"));
				names.add(n);
				dates.add(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
		
		System.out.println("Oldest job postings:");
		for (int i = 0; i < 10; i++) {
			int oldestIndex = dates.size() - 1;
			Date oldest = dates.get(dates.size() - 1);
			String oldestName = names.get(dates.size() - 1);
			
			for (int j = dates.size() - 2; j > 0; j--) {
				if (dates.get(j).compareTo(oldest) < 0) {
					oldest = dates.get(j);
					oldestName = names.get(j);
					oldestIndex = j;
				}
			}
			
			dates.remove(oldestIndex);
			names.remove(oldestIndex);
			System.out.println(oldestName + ": " + oldest.toString());
		}
		
		System.out.println("\nNewest job postings:");
		for (int i = 0; i < 10; i++) {
			int newestIndex = dates.size() - 1;
			Date newest = dates.get(dates.size() - 1);
			String newestName = names.get(dates.size() - 1);
			
			for (int j = dates.size() - 2; j > 0; j--) {
				if (dates.get(j).compareTo(newest) > 0) {
					newest = dates.get(j);
					newestName = names.get(j);
					newestIndex = j;
				}
			}
			
			dates.remove(newestIndex);
			names.remove(newestIndex);
			System.out.println(newestName + ": " + newest.toString());
		}
	}
}