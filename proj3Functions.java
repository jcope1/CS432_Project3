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
	
	
	public void listCommonJobTitles(){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		HashMap<String, Integer> jobTitleCount = new HashMap<String, Integer>();
		CountComparator compar = new CountComparator(jobTitleCount);
		TreeMap<String, Integer> sorted_jobTitleCount = new TreeMap<String,Integer>(compar);
		
		List<Document> jobList = jobs.find().into(new ArrayList<>());
		
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
		
		//String first = sorted_jobTitleCount.firstKey();
		
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
		List<String> titles = new ArrayList<>();
		Integer count = 0;
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		List<Document> jobList = jobs.find().into(new ArrayList<>());
		
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
		
		List<Document> jobList = jobs.find(findQuery).into(new ArrayList<>());
		
		for(Document job : jobList){
			System.out.println(job);
		}

	}
	
	public void listJobCategories(){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		List<Document> jobList = jobs.find().into(new ArrayList<>());
		
		List<String> categories = new ArrayList<>();
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
		
		List<Document> jobList = jobs.find(findQuery).into(new ArrayList<>());
		
		for(Document job : jobList){
			System.out.println(job);
		}
	}
	
	public void listCompanies(){
		ConnectionString connString = new ConnectionString("mongodb+srv://cs432:cs432@cluster0-bwsn2.mongodb.net/test?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true).build();
		MongoClient mongo = MongoClients.create(settings);
		MongoDatabase database = mongo.getDatabase("test");
		MongoCollection<Document> jobs = database.getCollection("Jobs");
		
		List<Document> companyList = jobs.find().into(new ArrayList<>());
		List<String> companies = new ArrayList<>();
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
		
		List<Document> companyList = jobs.find(findQuery).into(new ArrayList<>());
		
		for(Document company : companyList){
			System.out.println(company);
		}
	}
}
