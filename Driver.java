package com.mongodb.quickstart;

import java.util.Scanner;

class Driver{
	public static void main (String args []) {
		Scanner in = new Scanner(System.in);
		
		proj3Functions func = new proj3Functions();
		copeFunctions cope = new copeFunctions();
	
		System.out.println("\n\n\nWelcome to Project 3 by Jake Cope and Aaron Thailer\n");

		System.out.println("\nPlease select from the following options:\n");
		System.out.println("1) List Common Job Titles");
		System.out.println("2) List Job Titles");
		System.out.println("3) List Companies");
		System.out.println("4) List Job Categories");
		System.out.println("5) List Greatest Salary Range");
		System.out.println("6) List Average Experience Level");
		System.out.println("7) List Common Locations");
		System.out.println("8) List Citizen Interest");
		System.out.println("9) Search");
		System.out.println("0) Exit");
		System.out.print("\n>>> ");
		
		while(true){
			String option = in.nextLine();
			System.out.println();

			if(check_input(option, 9) != 0){
				System.out.println("Please enter a valid option from the menu.");
			}
			else{
				try {
					if(option.equals("1")){
						func.listCommonJobTitles();
					}
					else if(option.equals("2")){
						func.listTitles();
					}
					else if(option.equals("3")){
						func.listCompanies();
					}
					else if(option.equals("4")){
						func.listJobCategories();
					}
					else if(option.equals("5")){
						cope.printHighestSalaryRange();
					}
					else if(option.equals("6")){
						cope.printExperienceAverages();
					}
					else if(option.equals("7")){
						cope.printLocationHeatmap();
					}
					else if(option.equals("8")){
						cope.printJobTrends();
					}
					else if(option.equals("0")){
						exit_menu();
						in.close();
						return;
					}
					else if(option.equals("9")){
						while(true) {
							System.out.println("\n\nPlease select from the following options how you wish to search:\n");
							System.out.println("1) Job ID");
							System.out.println("2) Job Title");
							System.out.println("3) Company");
							System.out.println("4) Job Category");
							System.out.println("5) Salary");
							System.out.println("6) Experience Level");
							System.out.println("7) Full-Time/Part-Time");
							System.out.println("8) Time Since Posted");
							System.out.println("0) Back");
							System.out.print("\n>>> ");
						
							option = in.nextLine();
							
							if(check_input(option, 9) != 0) {
								System.out.println("Please enter a valid option from the menu.");
							}
							else {
								if(option.equals("1")){
									System.out.println("Please Enter the Job ID:");
									System.out.println("\n>>>");
									String id = in.nextLine();
									try {
										int idNum = Integer.parseInt(id);
										func.searchJobId(idNum);
									}
									catch(Exception e) {
										System.out.println(e.getMessage());
										System.out.println("Please enter a valid ID Number.\n");
									}
								}
								else if(option.equals("2")){
									System.out.println("Please Enter the Job Title:");
									System.out.println("\n>>>");
									String title = in.nextLine();
									func.searchTitle(title);
								}
								else if(option.equals("3")){
									System.out.println("Please Enter the Company Name");
									System.out.println("\n>>>");
									String name = in.nextLine();
									
									func.searchCompany(name);
								}
								else if(option.equals("4")){
									System.out.println("Please Enter the Job Category:");
									System.out.println("\n>>>");
									String category = in.nextLine();
									func.searchJobCategory(category);
								}
								else if(option.equals("5")){
									//TODO
								}
								else if(option.equals("6")){
									//TODO
								}
								else if(option.equals("7")){
									//TODO
								}
								else if(option.equals("8")){
									//TODO
								}
								else if(option.equals("0")){
									break;
								}
								else {
									System.out.println("\n*** UNKNOWN ERROR***\n");
								}
							}
						}
					}
					else{
						System.out.println("\n*** UNKNOWN ERROR ***\n");
					}
				} catch (Exception e) {
					System.out.println("\n*** Exception Occurred ***\n" + e.getMessage());
				}
			}
			System.out.println("\nPlease select from the following options:\n");
			System.out.println("1) List Common Job Titles");
			System.out.println("2) List Job Titles");
			System.out.println("3) List Companies");
			System.out.println("4) List Job Categories");
			System.out.println("5) List Greatest Salary Range");
			System.out.println("6) List Average Experience Level");
			System.out.println("7) List Common Locations");
			System.out.println("8) List Citizen Interest");
			System.out.println("9) Search");
			System.out.println("0) Exit");
			System.out.print("\n>>> ");
		}
	}
	
	private static void exit_menu() {
		System.out.println("Thank you for using our program!");
	}
	
	private static int check_input(String user_option, int value){
		int valid = -1;
		for(int i = 0; i <= value; i++){
			if(user_option.equals(Integer.toString(i))){
				valid = 0;
			}
		}
		return valid;
	}
}