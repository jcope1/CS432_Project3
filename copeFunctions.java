
package com.mongodb.quickstart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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

public class copeFunctions {
	
	public void printExperienceAverages(MongoCollection<Document> jobs) {

		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		ArrayList<String> exp = new ArrayList<String>();
		
		Integer lastJobID = -1;
		
		for (Document doc : jobList) {
			Integer jobID = doc.getInteger("Job ID");
			if (jobID.equals(lastJobID)) continue;
			lastJobID = jobID;
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
		
		System.out.println("No minimum requirements: " + (((float)exp.size() - (highschool + assoc + bacc + masters)) / exp.size()) * 100 + "% (" + (exp.size() - (highschool + assoc + bacc + masters)) + ")");
		System.out.println("Minimum high-school: " + (highschool / (float)exp.size()) * 100 + "% (" + highschool + ")");
		System.out.println("Minimum associates degree: " + ((assoc) / (float)exp.size()) * 100 + "% (" + assoc + ")");
		System.out.println("Minimum bachelors degree: " + ((bacc) / (float)exp.size()) * 100 + "% (" + bacc + ")");
		System.out.println("Minimum masters degree: " + ((masters) / (float)exp.size()) * 100 + "% (" + masters + ")");
	}
	
	public void printHighestSalaryRange(MongoCollection<Document> jobs) {

		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		Double maxRange = 0.0;
		Double[] maxEnds = new Double[2];
		
		String maxTitle = "";
		
		Integer lastJobID = -1;
		
		for (Document doc : jobList) {
			Integer jobID = doc.getInteger("Job ID");
			if (jobID.equals(lastJobID)) continue;
			lastJobID = jobID;
			
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
	
	public void printLocationHeatmap(MongoCollection<Document> jobs) {

		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		Hashtable<String, Integer> locations = new Hashtable<String, Integer>();
		
		Integer lastJobID = -1;
		
		for (Document doc : jobList) {
			Integer jobID = doc.getInteger("Job ID");
			if (jobID.equals(lastJobID)) continue;
			lastJobID = jobID;
			
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
			delim.close();
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
	
	public void printJobTrends(MongoCollection<Document> jobs) {
		
		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Date> dates = new ArrayList<Date>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Integer lastJobID = -1;
		
		for (Document doc : jobList) {
			Integer jobID = doc.getInteger("Job ID");
			if (jobID.equals(lastJobID)) continue;
			lastJobID = jobID;
			
			String n = doc.getString("Business Title");
			Date d;
			try {
				if (doc.getString("Posting Date").equals("")) continue;
				d = sdf.parse(doc.getString("Posting Date"));
				names.add(n);
				dates.add(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
		
		SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
		
		System.out.println("Oldest job postings (least popular):");
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
			System.out.println(oldestName + ": " + dtf.format(oldest));
		}
		
		System.out.println("\nNewest job postings (most popular):");
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
			System.out.println(newestName + ": " + dtf.format(newest));
		}
	}
}
