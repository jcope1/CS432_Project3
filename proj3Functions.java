package com.mongodb.quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClientSettings;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class proj3Functions{

	public proj3Functions(){
		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(java.util.logging.Level.SEVERE);
	}
	
	
	public void listCommonJobTitles(){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		HashMap<String, Integer> jobTitleCount = new HashMap<String, Integer>();
		CountComparator compar = new CountComparator(jobTitleCount);
		TreeMap<String, Integer> sorted_jobTitleCount = new TreeMap<String,Integer>(compar);
		
		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		for(Document job : jobList){
			String title = job.getString("Business Title");
			if(jobTitleCount.containsKey(title)){
				Integer count = jobTitleCount.get(title);
				jobTitleCount.put(title, count + 1);
			}
			else{
				jobTitleCount.put(title, 1);
			}
		}
		
		sorted_jobTitleCount.putAll(jobTitleCount);
		
		Map.Entry<String, Integer> title = sorted_jobTitleCount.pollFirstEntry();
		System.out.println(title);
		
		for(int i = 0; i < 50; i++){
			title = sorted_jobTitleCount.pollFirstEntry();
			System.out.println(title);
		}		
	}
	
	class CountComparator implements Comparator<String> {
		Map<String, Integer> base;
		
		public CountComparator(Map<String, Integer> base){
			this.base = base;
		}
		
		public int compare(String a, String b){
			if(base.get(a) >= base.get(b)){
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	public void listTitles(){
		List<String> titles = new ArrayList<String>();
		Integer count = 0;
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		for(Document job : jobList){
			String title = job.getString("Business Title");
			if(!titles.contains(title)){
				titles.add(title);
				count++;
			}
		}
		
		for(String ti : titles){
			System.out.println(ti);
		}
		//System.out.println(count);
	}
	
	public void searchTitle(String str){
		
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		Document regQuery = new Document();
		regQuery.append("$regex", "^(?)" + Pattern.quote(str));
		regQuery.append("$options", "i");
		Document findQuery = new Document();
		findQuery.append("Business Title", regQuery);
		
		List<Document> jobList = jobs.find(findQuery).into(new ArrayList<Document>());
		
		String title = "";
		Integer jobId = 0;
		if(!jobList.isEmpty()){
			for(Document job : jobList){
				title = job.getString("Business Title");
				jobId = job.getInteger("Job ID");
				System.out.println(jobId + "\t" + title);
			}
		}
		else{
			System.out.println("Search returned Empty\n");
		}
	}
	
	public void listJobCategories(){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		List<Document> jobList = jobs.find().into(new ArrayList<Document>());
		
		List<String> categories = new ArrayList<String>();
		Integer count = 0;
		
		for(Document job : jobList){
			String category = job.getString("Job Category");
			if(!categories.contains(category)){
				categories.add(category);
				count++;
			}
		}
		
		for(String cat : categories){
			System.out.println(cat);
		}
		//System.out.println(count);
		
	}
	
	public void searchJobCategory(String str){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		Document regQuery = new Document();
		regQuery.append("$regex", "^(?)" + Pattern.quote(str));
		regQuery.append("$options", "i");
		Document findQuery = new Document();
		findQuery.append("Job Category", regQuery);
		
		List<Document> jobList = jobs.find(findQuery).into(new ArrayList<Document>());
		
		String category = "";
		Integer jobId = 0;
		
		if(!jobList.isEmpty()){
			for(Document job : jobList){
				category = job.getString("Job Category");
				jobId = job.getInteger("Job ID");
				System.out.println(jobId + "\t" + category);
			}
		}
		else{
			System.out.println("Search returned Empty\n");
		}
	}
	
	public void listCompanies(){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		List<Document> companyList = jobs.find().into(new ArrayList<Document>());
		List<String> companies = new ArrayList<String>();
		Integer count = 0;
		
		for(Document company : companyList){
			String companyName = company.getString("Agency");
			if(!companies.contains(companyName)){
				companies.add(companyName);
				count++;
			}
		}
		
		for(String comp : companies){
			System.out.println(comp);
		}
		System.out.println(count);
	}
	
	public void searchCompany(String str){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		Document regQuery = new Document();
		regQuery.append("$regex", "^(?)" + Pattern.quote(str));
		regQuery.append("$options", "i");
		Document findQuery = new Document();
		findQuery.append("Agency", regQuery);
		
		List<Document> companyList = jobs.find(findQuery).into(new ArrayList<Document>());
		
		String comp = "";
		Integer jobId = 0;
		String title = "";
		
		if(!companyList.isEmpty()){
			for(Document company : companyList){
				comp = company.getString("Agency");
				jobId = company.getInteger("Job ID");
				title = company.getString("Business Title");
				System.out.println(jobId + "\t" + comp + "\t" + title);
			}
		}
		else{
			System.out.println("Search returned Empty\n");
		}
	}
	
	public void searchHoursType(String str){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		Document findQuery = new Document();
		findQuery.append("Full-Time/Part-Time indicator", str);

		List<Document> jobListHours = jobs.find(findQuery).into(new ArrayList<Document>());

		Integer jobId = 0;
		String title = "";

		for(Document job : jobListHours){
			title = job.getString("Business Title");
			jobId = job.getInteger("Job ID");
			System.out.println(jobId + " : " + title);
		}
	}

	public void searchJobId(Integer id){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		Document findQuery = new Document();
		findQuery.append("Job ID", id);

		List<Document> jobList = jobs.find(findQuery).into(new ArrayList<Document>());
		if(!jobList.isEmpty()){
			for(Document job : jobList){
				System.out.println("Job ID:\t" + job.getInteger("Job ID"));
				System.out.println("Agency:\t" + job.getString("Agency"));
				System.out.println("# of Positions:\t" + job.getInteger("# Of Positions"));
				System.out.println("Business Title:\t" + job.getString("Business Title"));
				System.out.println("Civil Service Title:\t" + job.getString("Civil Service Title"));
				System.out.println("Title Code No:\t" + job.getInteger("Title Code No"));
				System.out.println("Level:\t" + job.getInteger("Level"));
				System.out.println("Job Category:\t" + job.getString("Job Category"));
				System.out.println("Full-Time/Part-Time:\t" + job.getString("Full-Time/Part-Time indicator"));
				System.out.println("Salary Frequency:\t" + job.getString("Salary Frequency"));
				
				Object r1 = job.get("Salary Range From");
				Object r2 = job.get("Salary Range To");
				Double range1;
				Double range2;
				
				//System.out.println(r1.getClass().toString());
				
				if (r1.getClass().toString().contains("Integer")) {
					range1 = new Double((Integer)r1);
				} else {
					range1 = (Double)r1;
				}
				
				if (r2.getClass().toString().contains("Integer")) {
					range2 = new Double((Integer)r2);
				} else {
					range2 = (Double)r2;
				}
				
				System.out.println("Salary Range:\t" + range1 + "-" + range2);
				System.out.println("Work Location:\t" + job.getString("Work Location"));
				System.out.println("Division:\t" + job.getString("Division/Work Unit"));
				System.out.println("Job Description:\t" + job.getString("Job Description"));
				System.out.println("Minimum Qualifying Requirements:\t" + job.getString("Minimum Qual Requirements"));
				System.out.println("Preferred Skill:\t" + job.getString("Preferred Skills"));
				System.out.println("Additional Information:\t" + job.getString("Additional Information"));
				System.out.println("Residency Requirement:\t" + job.getString("Residency Requirement"));
				System.out.println("Posting Date:\t" + job.getString("Posting Date"));
			}
		}
		else{
			System.out.println("Search returned Empty\n");
		}
	}
}
