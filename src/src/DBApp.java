import java.util.*;
import java.util.Random;

import java.sql.*;

public class DBApp {
	
    public static String userType;
    public static String userName;
    public static String pwd;
    public static Scanner scan;
    static boolean loginSuccess;
    static int attempts;
    static boolean success;
    
    // Alexis Postgres Info
    public static String user = "postgres";
    public static String sqlPwd = "postgres";
    
    public static ResultSet rset;
    
    public static void main(String[] args){
    	
    	 userName = null;
    	 pwd = null;
      	 userType = null;
      	 loginSuccess = false;
      	 attempts = 0;
      	 scan = new Scanner(System.in);
      	 success = false;
      	 
      	 // Sign in the user
      	 signin();
      	 
      	 if (loginSuccess) {
      		 
          	 // Present the user menu
          	 presentMenu(userType);
          	 
      	 }
      	 
      	 scan.close();

    }
    
    
    public static void signin(){
        
        //user log in with password and userName
        System.out.println("------Log In------");
        System.out.println("User name:");
        String userName = scan.next();
        System.out.println("Password:");
        String password = scan.next();
        
        //validate the log in credentials
        while (!validate(userName, password)){
            System.out.println("Invalid username/password!");
            System.out.println();
            attempts += 1;
            
            if (attempts >= 3) {
            	System.out.println("Too many unsuccessful attemps. Try again later.");
            	break;
            }
            else {
            	signin();
            }
        }
        
        System.out.println("Logged in successfully!");
        loginSuccess = true;
        attempts = 0;
    }
    
    
    public static boolean validate(String loginName, String password){
        try {
        		
        	try {
				Class.forName ("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", user, sqlPwd);
       
            Statement stmt = conn.createStatement();
        
            String strSelect = "select *" + "\n" + 
            					"from userTable" + "\n" +
            					"where userName = '" + loginName + "';";
            //System.out.println("The SQL statement is: " + strSelect + "\n" ); // Echo for debugging
            
            ResultSet rset = stmt.executeQuery(strSelect);
            
            while(rset.next()){
                String username = rset.getString("userName");
                String pword = rset.getString("pwd");
                String usertype = rset.getString("userType"); //store static var userType

                
                if(loginName.equals(username)){
                    if(pword.equals(password)){
                    	userName = username;
                    	pwd = password;
                    	userType = usertype;
                        return true;
                    }else{
                        return false;
                    }
                }
            }
            return false;
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        
        return false;
    }
    
    
    public static boolean updateSQL(String query) {
		try {

			// Create connection
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", user, sqlPwd);
			
			// Create statement
			Statement stmt = conn.createStatement();
			
			// Execute statement
			stmt.executeUpdate(query);
			
			// Close connections
			stmt.close();
			conn.close();
			
			return true;
		}
		catch(SQLException sqle) {
			// Handle exceptions
			System.out.println("SQLException: " + sqle);
			
			return false;
		}
    }
    
    
//    public static ResultSet querySQL(String query) {
//		try {
//
//			// Create connection
//			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", user, sqlPwd);
//			
//			// Create statement
//			Statement stmt = conn.createStatement();
//			
//			// Execute statement
//			rset = stmt.executeQuery(query);
//			
//			while(rset.next()) {
//				variable = rset.getString("insurancePremium");
//			}
//			
//			// Close connections
//			stmt.close();
//			conn.close();
//			
//			success = true;
//		
//		}
//		catch(SQLException sqle) {
//			// Handle exceptions
//			System.out.println("SQLException: " + sqle);
//		}
//		return rset;
//    }
//    
    
    public static String stringQuery(String select, String from, String where, String equals) {
    	
    	String variable = "";
    	
    	String query = "SELECT " + select + "\n" +
				"FROM " + from + "\n" +
				"WHERE " + where + " = '" + equals + "';";
    	
    	try {

			// Create connection
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", user, sqlPwd);
			
			// Create statement
			Statement stmt = conn.createStatement();
			
			// Execute statement
			rset = stmt.executeQuery(query);
			
			while(rset.next()) {
				variable = rset.getString(select);
			}
			
			// Close connections
			stmt.close();
			conn.close();
			
			success = true;
		
		}
		catch(SQLException sqle) {
			// Handle exceptions
			System.out.println("SQLException: " + sqle);
		}
		
		return variable;
		
    }
    
    
    public static double doubleQuery(String select, String from, String where, String equals) {
    	
    	double variable = 0;
    	
    	String query = "SELECT " + select + "\n" +
				"FROM " + from + "\n" +
				"WHERE " + where + " = '" + equals + "';";
    	
    	try {

			// Create connection
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", user, sqlPwd);
			
			// Create statement
			Statement stmt = conn.createStatement();
			
			// Execute statement
			rset = stmt.executeQuery(query);
			
			while(rset.next()) {
				variable = rset.getDouble(select);
			}
			
			// Close connections
			stmt.close();
			conn.close();
			
			success = true;
		
		}
		catch(SQLException sqle) {
			// Handle exceptions
			System.out.println("SQLException: " + sqle);
		}
		
		return variable;
    }
    
    public static double aggQuery(String select, String from, String where, String equals, String groupBy) {
    	
    	double variable=0;
    	boolean success=false;
    	
    	String [] selSplit = select.split(",");
    	
    	String query = "SELECT " + selSplit[0] + ", sum" + "\n" +
    					" FROM (select " + select + " from " + from + " group by " + groupBy + ") as temp" + "\n" +
						"WHERE " + where + " = '" + equals + "';";
    	
    	try {

			// Create connection
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", user, sqlPwd);
			
			// Create statement
			Statement stmt = conn.createStatement();
			
			// Execute statement
			rset = stmt.executeQuery(query);
			
			while(rset.next()) {
				variable = rset.getDouble("sum");
			}
			
			// Close connections
			stmt.close();
			conn.close();
			success = true;
		}
		catch(SQLException sqle) {
			success = false;
			// Handle exceptions
			System.out.println("SQLException: " + sqle);
		}
    	
		return variable;
    }
    
    public static void presentMenu(String userType) {
    		
    	 System.out.println();
    	
    	 int temp = 0;
    	 //Scanner scan = new Scanner(System.in);

	 	 if(userType.equals("Admin"))
	 	 {
	 		 System.out.println("MENU" + 
	 				 		"\n" + "1: Add record" + 
	 				 		"\n" + "2: Update record" + 
	 				 		"\n" + "3: Remove employee" + 
	 				 		"\n" + "4: Run report" + 
	 				 		"\n" + "5: Logout");
	 
	 		 //System.out.println("Here!");
	 		 temp = scan.nextInt();
	 		 
	 		 if(temp == 1) // Add record
	 		 {
	 			 addRecord();
	 		 }
	 		 else if(temp == 2) // Update record
	 		 {
	 			 updateRecord();
	 		 }
	 		 else if(temp == 3) // Delete record
	 		 {
	 			 deleteEmployee();
	 		 }
	 		 else if(temp == 4) // Run report
	 		 {
	 			 runReport();
	 		 }
	 		 else if(temp == 5) // Exit
	 		 {
	 			 logout();
	 		 }
	 		 else
	 		 {
	 			System.out.println("Invalid command, please try again");
	 		 }
	 	 }
	 	else if (userType.equals("Manager"))//menu for manager
	 	 {
	 		 System.out.println("MENU" + 
	 				 		"\n" + "1: Update information" + 
	 				 		"\n" + "2: View information" + 
	 				 		"\n" + "3. Logout");
	 		 
	 		temp = scan.nextInt();
			 if(temp == 1) // Update Information
			 {
				 updateInfo();
			 }
			 else if(temp == 2) // View Information
			 {
				 //viewInfo();
			 }
			else if(temp == 3) // Exit
			 {
				 logout();
			 }
			else
			 {
				System.out.println("Invalid command, please try again");
			 }
	 	 }else{//menu for employee
			 
			 System.out.println("MENU" +
								"\n" + "1. View Information" +
								"\n" + "2. Add Benefits Plan" +
								"\n" + "3. Logout");
			temp = scan.nextInt();
			if (temp == 1){
				viewInfo(userName);
			}else if(temp == 2){
				addBenefits(userName);
			}else if(temp == 3){
				logout();
			}else{
				System.out.println("Invalid command, please try again");
			}
		 }
    	
    }
    
    
    public static void addRecord() {
    	
    	 // Instantiate Scanner
		 //Scanner scan = new Scanner(System.in);
		 
		 // Print out options
		 System.out.println("What kind of record would you like to add?" +
				 				"\n" + "1. Employee" +								// Employee
				 				"\n" + "2. Insurance Plan" +						// InsurancePlan
				 				"\n" + "3. Paycheck" +								// Paycheck
				 				"\n" + "4. W2");									// w2
				 				
		 int selection = scan.nextInt();
		 if (selection == 1) {				// Employee
			 addEmployee();
		 }
		 else if (selection == 2) {			// InsurancePlan
			 addInsurancePlan();
		 }
		 else if (selection == 3) {			// Paycheck
			addPaycheck();
		 }
		 else if (selection == 4) {			// w2
			addW2();
		 }
		 else {
			System.out.println("Invalid command, please try again");
		 }

		 presentMenu(userType);
    	
    }
    
    
    // 3 Folding Points to this function ( not sure why )
    public static void addEmployee() {
    	
    	int federalTaxBracket, ssn;
    	String employeeID, firstName, lastName, jobTitle, salaryType, stateName;
    	double bonus;
    	
    	// Instantiate Scanner
    	//Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the following information:");
		 
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// Employee Name
		System.out.println("Employee First Name: ");
		firstName = scan.next();
		System.out.println("Employee Last Name: ");
		lastName = scan.next();
		
		// Job Title
		System.out.println("Job Title: ");
		jobTitle = scan.nextLine();
		
		String x = scan.nextLine();
		
		// Salary Type
		System.out.println("Salary Type (W2/Hourly): ");
		salaryType = scan.next();
		
		x = scan.nextLine();
		
		// Federal Tax Bracket
		System.out.println("Federal Tax Bracket (1/2/3): ");
		federalTaxBracket = scan.nextInt();
		
		// State
		System.out.println("Employee State of Residence: ");
		stateName = scan.next();
		
		// SSN
		System.out.println("Employee SSN (No dashes): ");
		ssn = scan.nextInt();
		
		String otherTax="";
		if (salaryType.equals("W2")) {
			
			System.out.println("Yearly Bonus Percentage (as decimal): ");
			bonus = scan.nextDouble();
			
			x = scan.nextLine();
			
			otherTax = "insert into otherTaxes" + "\n" +
							"values('" + ssn + "'," + 0.075 + "," + 0.075 + "," + 0.025 + ");";

		} else {
			
			bonus = 0.0;
			
			otherTax = "insert into otherTaxes" + "\n" +
					"values('" + ssn + "'," + 0.15 + "," + 0.00 + "," + 0.025 + ");";
		}
		
		boolean result = updateSQL(otherTax);
		if (result) {
			result = false;
			
			String update = "insert into Employee" + "\n" +
					"values('" + employeeID + "','" + firstName + "','" + lastName + "','" +
					jobTitle + "','" + salaryType + "'," + bonus + "," + federalTaxBracket + ",'" +
					stateName + "'," + ssn + ");";
			
			result = updateSQL(update);
			
			if (result) {
				result = false;
				
				update = "insert into userTable" + "\n" +
						"values('" + employeeID + "','" + "pwd" + "','" + "Emp" + "');";
				
				result = updateSQL(update);
				if (result) {
					System.out.println("Employee added successfully");
				}
				
			}
			
		}
		
    }
    
    
    public static void addInsurancePlan() {
    	
    	double indEmpCont, indCompCont, famEmpCont, famCompCont;
    	String planID, employeeID;
    	
		System.out.println("Please provide the following information:");
		 
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// Plan ID
		System.out.println("Plan ID (Humana/UnitedHealth/BCBS): ");
		planID = scan.next();
		
		// Individual Employee Contribution
		System.out.println("Individual Employee Contribution: ");
		indEmpCont = scan.nextDouble();
		
		// Individual Company Contribution
		System.out.println("Individual Company Contribution: ");
		indCompCont = scan.nextDouble();
		
		// Family Employee Contribution
		System.out.println("Family Employee Contribution: ");
		famEmpCont = scan.nextDouble();
		
		// Family Company Contribution
		System.out.println("Family Company Contribution: ");
		famCompCont = scan.nextDouble();
		
		
		// SQL CODE
		String update = "insert into InsurancePlan" + "\n" +
						"values('" + planID + "'," + indEmpCont + "," + indCompCont + "," +
						famEmpCont + "," + famCompCont + ",'" + employeeID + "');";
		
		boolean result = updateSQL(update);
		if (result) {
			System.out.println("Insurance Plan added successfully");
		}

		
    }
    
    
    public static void addBenefits(String employeeID) {
    	
    	double f1kEmpCont, f1kCompCont;
    	String planAccNum, healthPlan, attorneyPlan, lifeIns;
    	
    	// Instantiate Scanner
    	//Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the following information:");
		
		// 401k Employee Contribution
		System.out.println("401K Contribution: ");
		f1kEmpCont = scan.nextDouble();
		
		if (f1kEmpCont > 0.07) {
			f1kCompCont = 0.07;
		} else {
			f1kCompCont = f1kEmpCont;
		}
		
		// Plan Account Number
		Random random = new Random();
		planAccNum = "A" + String.valueOf(random.nextInt(10000));
		
		// Health Plan
		System.out.println("Health Plan (Y/N): ");
		healthPlan = scan.next();
		
		// Attorney Plan
		System.out.println("Attorney Plan(Y/N): ");
		attorneyPlan = scan.next();
		
		// Life Insurance
		System.out.println("Life Insurance (Tier1/Tier2/Tier3/None): ");
		lifeIns = scan.next();
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		
		// SQL CODE
		String update = "insert into Benefits" + "\n" +
						"values(" + f1kEmpCont + "," + f1kCompCont + ",'" + planAccNum + "','" +
						healthPlan + "','" + attorneyPlan + "','" + lifeIns + "','" + employeeID + "');";
			
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Benefit Plan added successfully");
		}

    	
    }
    
    
    public static void addPaycheck() {

    	String employeeID, paycheckID;
    	double income, stateTax, fedTax, socialSecurity, medicare, f1kDeduction, insPremium, totalDeductions;
    	
		System.out.println("Please provide the following information:");
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// Paycheck ID
		System.out.println("Paycheck Date (Formatted as 01/01/1999: ");
		paycheckID = scan.next();
		
		// Income
		System.out.println("Gross Pay: ");
		income = scan.nextDouble();
		
		// stateTax
		stateTax = getStateTax(employeeID, income);
		
		// fedTax
		fedTax = getFedTax(employeeID, income);
		
		// Social Security & medicare
		socialSecurity = getSS(employeeID) * income;
		medicare = getMedicare(employeeID) * income;
		
		// 401K Deduction
		f1kDeduction = get401(employeeID) * income;
		
		// Insurance Premium
		insPremium = getInsPremium(employeeID, income);
		
		//TotalDeductions
		totalDeductions = stateTax + fedTax + socialSecurity + medicare;
		
		// SQL CODE
		String update = "insert into Paycheck" + "\n" +
						"values('" + employeeID + "','" + paycheckID + "'," + income + "," + stateTax + "," +
						fedTax + "," + socialSecurity + "," + medicare + "," + f1kDeduction + "," + insPremium + "," + totalDeductions + ");";
		
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Paycheck ID "+ paycheckID + " for Employee " + employeeID + " added successfully");
		}

    }
    
    
    public static double getStateTax(String employeeID, double income) {
    	//Initialize variables
		String stateName = "";
		double stateTaxRate = 0.0;
		double stateTax = 0;
		
    	stateName = stringQuery("stateName", "Employee", "employeeID", employeeID);
    	stateTaxRate = doubleQuery("stateTaxRate", "States", "stateName", stateName);
    	
    	// Multiply state tax rate * income
		stateTax = stateTaxRate * income;

		return stateTax;
    }
    
    
    public static double getFedTax(String employeeID, double income) {
    	//Initialize variables
		String taxBracket = "";
		double fedTaxRate = 0.0;
		double fedTax = 0;
		
    	taxBracket = stringQuery("federalTaxBracket", "Employee", "employeeID", employeeID);
    	fedTaxRate = doubleQuery("fedTaxRate", "federalTax", "federalTaxBracket", taxBracket);
    	fedTax = fedTaxRate * income;

		return fedTax;
    }
    
    
    public static double getSS(String employeeID) {
    	double ss = 0.0;
    	String ssn="";
    	
    	ssn = stringQuery("employeeSSN", "Employee", "employeeID", employeeID);
    	ss = doubleQuery("ssEmpPortion", "otherTaxes", "employeeSSN", ssn);
    	
    	return ss;
    }
    
    
    public static double getMedicare(String employeeID) {
    	double medicare = 0.0;
    	String ssn = "";
    	
    	ssn = stringQuery("employeeSSN", "Employee", "employeeID", employeeID);
    	medicare = doubleQuery("medicare", "otherTaxes", "employeeSSN", ssn);
    	
    	return medicare;
    }
    
    
    public static double get401(String employeeID) {
    	double f1k = 0.0;
    	
    	f1k = doubleQuery("fourOneKEmployeeContr", "Benefits", "employeeID", employeeID);
    	
    	return f1k;
    }
    
    
    public static double getInsPremium(String employeeID, double income) {
    	double premium = 0.0, ind = 0.0, fam = 0.0;
    	
    	ind = doubleQuery("individualEmployeeContr", "InsurancePlan", "employeeID", employeeID);
    	ind = ind * income;
    	
    	fam = doubleQuery("familyEmployeeContr", "InsurancePlan", "employeeID", employeeID);
    	fam = fam * income;

    	premium = ind + fam;
    	
        return premium;
    }
    
    
    public static void addW2() {
    	double income, deductions, bonuses, bonusPct;
    	String employeeID, w2ID;
    	
		System.out.println("Please provide the following information:");
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// W2 ID
		System.out.println("W2 Year: ");
		w2ID = scan.next();
		
		// Yearly Income
		income = aggQuery("employeeID, sum(income)", "Paycheck", "employeeID", employeeID, "employeeID");
	
		// Deductions
		deductions = aggQuery("employeeID, sum(totalDeductions)", "Paycheck", "employeeID", employeeID, "employeeID");
		
		// Bonuses
		bonusPct = doubleQuery("bonus", "Employee", "employeeID", employeeID);
		bonuses = bonusPct*income;
		
		// SQL CODE
		String update = "insert into W2" + "\n" +
						"values(" + employeeID + "," + w2ID + "," + income + "," +
						deductions + "," + bonuses + ");";
		
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("W2 added successfully");
		}

    }
    
    
    public static void updateRecord() {
		 
		 // Print out options
		 System.out.println("What kind of record would you like to update?" +
				 				"\n" + "1. Employee" +								// Employee
				 				"\n" + "2. State Tax Rate" +						// State
				 				"\n" + "3. Federal Tax Rate" +						// federalTax
				 				"\n" + "4. Company Contribution");					// otherTaxes, Benefits, InsurancePlan
		 int selection = scan.nextInt();
		 if (selection == 1) {				// Employee
			 updateEmployee();
		 }
		 else if (selection == 2) {			// State
			 updateState();
		 }
		 else if (selection == 3) {			// federalTax
			 updateFederal();
		 }
		 else if (selection == 4) {			// otherTaxes, Benefits, InsurancePlan
			 
			System.out.println("Which contribution would you like to update?" +
	 				"\n" + "1. Social Security" +						// otherTaxes
	 				"\n" + "2. 401K" +									// Benefits
	 				"\n" + "3. Insurance Plan");						// InsurancePlan
			
			selection = scan.nextInt();
			if (selection == 1 || selection == 2 || selection == 3) {
				updateContribution(selection);
			}
			else {
 				System.out.println("Invalid command, Please try again");
 				updateRecord();
 			}
			
		 }
		 else {
			System.out.println("Invalid command, Please try again");
			updateRecord();
		 }
		 
		 presentMenu(userType);
    }
    
    
    public static void updateEmployee() {
    	 String update = "", set="";
    	 
    	
		 // Instantiate Scanner
		 //Scanner scan = new Scanner(System.in);
		 
		 System.out.println("Please enter the Employee's ID: ");
		 String employeeID = scan.next();
		 
		// Print out options
		 System.out.println("What you like to update?" +
				 				"\n" + "1. Job Title" +								// jobTitle
				 				"\n" + "2. Salary Type" +							// salaryType
				 				"\n" + "3. Bonus" +									// bonus
				 				"\n" + "4. State Tax Rate");						// stateName
		 
		 int selection = scan.nextInt();
		 if (selection == 1) {				// jobTitle
			 System.out.println("New Job Title: ");
			 String jobTitle = scan.next();
			 
			 update = "UPDATE Employee" + "\n" +
					 	"SET jobTitle = '" + jobTitle + "'\n" +
					 	"WHERE employeeID = '" + employeeID + "';";
		 }
		 else if (selection == 2) {			// salaryType
			 System.out.println("W2 or Hourly?: ");
			 String salaryType = scan.next();
			 
			 update = "UPDATE Employee" + "\n" +
					 	"SET salaryType = '" + salaryType + "'\n" +
					 	"WHERE employeeID = '" + employeeID + "';";
		 }
		 else if (selection == 3) {			// bonus
			 System.out.println("Enter Bonus Amount: $");
			 int bonus = scan.nextInt();
			 
			 update = "UPDATE Employee" + "\n" +
					 	"SET bonus = " + bonus + "\n" +
					 	"WHERE employeeID = '" + employeeID + "';";
		 }
		 else if (selection == 4) {			// stateName
			 System.out.println("State Name: ");
			 String stateName = scan.next();
			 
			 update = "UPDATE Employee" + "\n" +
					 	"SET stateName = '" + stateName + "'\n" +
					 	"WHERE employeeID = '" + employeeID + "';";
		 }

		 
		 // SQL CODE HERE
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Employee updated successfully");
		}
			
    }
    
    
    public static void updateState() {
    	
    	 String stateName, update;
    	 double taxRate;
    	
		 // Instantiate Scanner
		 //Scanner scan = new Scanner(System.in);
		 
		 System.out.println("Enter state name: ");
		 stateName = scan.next();
		 
		 System.out.println("Enter new tax rate (0.##): ");
		 taxRate = scan.nextDouble();
		 
		 update = "UPDATE States" + "\n" +
				 	"SET stateTaxRate = " + taxRate + "\n" +
				 	"WHERE stateName = '" + stateName + "';";
		 
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("State Tax Rate updated successfully");
		}

    }
    
    
    public static void updateFederal() {
	   	 
    	 String fedBracket, update;
	   	 double taxRate;
   	
		 // Instantiate Scanner
		 //Scanner scan = new Scanner(System.in);
		 
		 System.out.println("Enter federal tax bracket: ");
		 fedBracket = scan.next();
		 
		 System.out.println("Enter new tax rate (0.##): ");
		 taxRate = scan.nextDouble();
		 
		 update = "UPDATE federalTax" + "\n" +
				 	"SET fedTaxRate = " + taxRate + "\n" +
				 	"WHERE federalTaxBracket = '" + fedBracket + "';";
		 
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Federal Tax Rate updated successfully");
		}

    }
    
    
    public static void updateContribution(int selection) {
    	
    	String employeeID, update, ssn="";
    	double ss, f1k;
    	boolean success;
    	
		// Instantiate Scanner
		//Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the Employee ID: ");
		employeeID = scan.next();
    	
    	if (selection == 1) {				// otherTaxes -> Social Security
    		System.out.println("What is the new contribution amount: ");
    		ss = scan.nextDouble();
    		
    		ssn = stringQuery("employeeSSN", "Employee", "employeeID", employeeID);
    		
    		update = "UPDATE otherTaxes" + "\n" +
					 "SET ssCompanyPortion = " + ss + "\n" +
					"WHERE employeeSSN = '" + ssn + "';";
			
			success = updateSQL(update);
			
			if (success) {
				System.out.println("Social Security Contribution update successfully");
			} else {
				System.out.println("Error: Please try again later");
			}
    		
		}
		else if (selection == 2) {			// Benefits -> 401k
    		System.out.println("What is the new contribution amount: ");
    		f1k = scan.nextDouble();
    		
    		update = "UPDATE Benefits" + "\n" +
					 "SET fourOneKCompanyContr = " + f1k + "\n" +
					 "WHERE employeeID = '" + employeeID + "');";
    		
    		success = updateSQL(update);
			
			if (success) {
				System.out.println("401K Contribution update successfully");
			} else {
				System.out.println("Error: Please try again later");
			}
		}
		else if (selection == 3) {			// InsurancePlan -> InsurancePlan
			
			System.out.println("Which insurance contribution would you like to update?" +
	 				"\n" + "1. Individual" +								// individualCompanyContr
	 				"\n" + "2. Family" +									// familyCompanyContr
	 				"\n" + "3. Both");										// Both
			
			int newSelection = scan.nextInt();
			
			if (newSelection == 1) {
	    		indCont(employeeID);
			} else if (newSelection == 2) {
				familyCont(employeeID);
			} else if (newSelection == 3) {
				indCont(employeeID);
				familyCont(employeeID);
			} else {
				System.out.println("Invalid command. Please try again");
				updateContribution(3);
			}
		}

    }
    
    
    public static void indCont(String employeeID) {
    	
    	String update;
    	double ind;
    	boolean success;
    	
		// Instantiate Scanner
		//Scanner scan = new Scanner(System.in);
		
		System.out.println("New Individual Contribution Amount: ");
		ind = scan.nextDouble();
		
		update = "UPDATE InsurancePlan" + "\n" +
				 "SET individualCompanyContr = " + ind + "\n" +
				 "WHERE employeeID = '" + employeeID + "';";
		
		success = updateSQL(update);
		
		if (success) {
			System.out.println("Individual Insurance Contribution update successfully");
		} else {
			System.out.println("Error: Please try again later");
		}

    }
    
    
    public static void familyCont(String employeeID) {
    	
    	String update;
    	double fam;
    	boolean success;
    	
		// Instantiate Scanner
		//Scanner scan = new Scanner(System.in);
		fam = scan.nextDouble();
		
		update = "UPDATE InsurancePlan" + "\n" +
				 "SET familyCompanyContr = " + fam + "\n" +
				 "WHERE employeeID = '" + employeeID + "');";
		
		success = updateSQL(update);
		
		if (success) {
			System.out.println("Family Insurance Contribution update successfully");
		} else {
			System.out.println("Error: Please try again later");
		}

    }
    
    
    public static void deleteEmployee() {
    	
    	String employeeID = "", ssn="";
    	String answer;
    	
    	// Instantiate Scanner
    	//Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the Employee ID:");
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		ssn = stringQuery("employeeSSN", "Employee", "employeeID", employeeID);
		 
		System.out.println("This operation is permanent and cannot be undone. Continue? (Y/N)");
		answer = scan.next();
		
		if (answer.equals("Y")) {
			
			String delete = "DELETE FROM Employee WHERE employeeID = '" + employeeID + "';";
			
			boolean result = updateSQL(delete);
			
			if (result) {
				
				result = false;
				
				delete = "DELETE FROM otherTaxes WHERE employeeSSN = '" + ssn + "';";
				result = updateSQL(delete);
				
				delete = "DELETE FROM userTable WHERE userName = '" + employeeID + "';";
				result = updateSQL(delete);
				
				System.out.println("Employee deleted successfully");
			}
			
		}
		else {
			System.out.println("Exiting operation...");
		}

		presentMenu(userType);
    }
    
    
    public static void runReport() {
    	
    	String employeeID="", reportID="", ssn="", query="", update="";
    	double wages=0, bonus=0, f1k=0, ss=0, ins=0, ind=0, fam=0;
    	boolean success=false;
    	
    	
		// Instantiate Scanner
		//Scanner scan = new Scanner(System.in);
		
		System.out.println("Please provide the Employee ID:");
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// Create reportID
		Random random = new Random();
		int reportIDInt = random.nextInt(1000);
		reportID = String.valueOf(reportIDInt);
		
		// wages so far
		//wages = doubleQuery("income", "Paycheck", "employeeID", employeeID);
		wages = aggQuery("employeeID, sum(income)", "Paycheck", "employeeID", employeeID, "employeeID");
		
		//bonus
		bonus = doubleQuery("bonus", "Employee", "employeeID", employeeID) * wages;
		
		//f1k
		f1k = doubleQuery("fourOneKCompanyContr", "Benefits", "employeeID", employeeID) * wages;
		
		//ss
		ssn = stringQuery("employeeSSN", "Employee", "employeeID", employeeID);
		ss = doubleQuery("ssCompanyPortion", "otherTaxes", "employeeSSN", ssn) * wages;
		
		//ins
		ind = doubleQuery("individualCompanyContr", "InsurancePlan", "employeeID", employeeID) * wages;
		fam = doubleQuery("familyCompanyContr", "InsurancePlan", "employeeID", employeeID) * wages;
		ins = ind + fam;

		// Add ExpenseReport
		update = "insert into ExpenseReport" + "\n" +
				"values(" + employeeID + "," + reportID + "," + wages + "," + bonus + "," +
				f1k + "," + ss + "," + ins + ");";

		success = updateSQL(update);
		
		if (success) {
			success = false;
			System.out.println("Report Generated:");
		}
		
		// Print out report:
		System.out.println("Employee ID: " + employeeID);
		System.out.println("ReportID: " + reportID);
		System.out.println("Wages: " + wages);
		System.out.println("Bonus: " + bonus);
		System.out.println("401K Contribution: " + f1k);
		System.out.println("Social Security Contribution: " + ss);
		System.out.println("Insurance Contribution: " + ins);
    	System.out.println();
		
    	presentMenu(userType);
    }
    
    
    public static void updateInfo(){
    	
		// Instantiate Scanner
		//Scanner scan = new Scanner(System.in);
    	
		// Print out options
		System.out.println("What would you like to update?" +
				 				"\n" + "1. Employee Performance" +							// InsurancePlan
				 				"\n" + "2. Benefits Package" +								// Benefits
				 				"\n" + "3. Add Dependent");									// Dependent
		int selection = scan.nextInt();
		
		if (selection == 1) {
			
		} else if (selection == 2) {
			
		} else if (selection == 3) {
			
		} else {
			System.out.println("Invalid command. Please try again");
			updateInfo();
		}
		
    	presentMenu(userType);
    	
    }
    
    public static void updatePerformance() {
    	
    	String employeeID="", salaryType="";
    	boolean isW2 = false;
    	double multiplier = 0;
    	
    	System.out.println("Enter the employeeID: ");
		employeeID = scan.next();
		
		
		salaryType = stringQuery("salaryType", "Employee", "employeeID", employeeID);
		if (salaryType.equals("Hourly")) {
			System.out.println("Error: Only W2 employees receive performance reviews for bonuses.");
		} else {
			System.out.println("How would you rate this employee's performance?" +
	 				"\n" + "1. Super" +							
	 				"\n" + "2. Good" +	
	 				"\n" + "3. Just OK" +
	 				"\n" + "4. Poor");		
			
			int selection = scan.nextInt();
			
			if (selection == 1) {
				multiplier = 1.5;
			} else if (selection == 2) {
				multiplier = 1.0;
			} else if (selection == 3) {
				multiplier = 0.5;
			} else if (selection == 4) {
				multiplier = 0;
			} else {
				System.out.println("Invalid command. Please try again");
				updatePerformance();
			}
		}
		
		// CODE FOR UPDATING BONUS USING MULTIPLIER GOES HERE
		
	
    }
    
    
    
    public static void viewInfo(String employeeID) {
		// Print out options
		System.out.println("What would you like to view?" +
				 				"\n" + "1. Employee Profile" +						// Employee
				 				"\n" + "2. Paycheck" +								// Paycheck
				 				"\n" + "3. W2" +									// W2
				 				"\n" + "4. Insurance Plan" +						// InsurancePlan
				 				"\n" + "5. Benefits");								// Benefits
		int selection = scan.nextInt();
		
		if (selection == 1) {
			viewProfile(employeeID);
		} else if (selection == 2) {
			viewPaycheck(employeeID);
		} else if (selection == 3) {
			viewW2(employeeID);
		} else if (selection == 4) {
			viewInsurance(employeeID);
		} else if (selection == 5) {
			viewBenefits(employeeID);
		} else {
			System.out.println("Invalid command. Please try again");
			updateInfo();
		}

		
    	presentMenu(userType);
    	
    }
    
    
    public static void viewProfile(String employeeID) {
    	
    	String first="", last="", jobTitle="", sType="";
    	double bonus=0;
    	
    	//Employee ID
    	System.out.println("EmployeeID: " + employeeID);
    	
    	//First Name
    	first = stringQuery("firstName", "Employee", "employeeID", employeeID);
		System.out.println("First Name: " + first);
    	
    	//Last Name
		last = stringQuery("lastName", "Employee", "employeeID", employeeID);
		System.out.println("Last Name: " + last);
    	
    	//Job Title
		jobTitle = stringQuery("jobTitle", "Employee", "employeeID", employeeID);
		System.out.println("Job Title: " + jobTitle);
    	
    	//Salary Type
		sType = stringQuery("salaryType", "Employee", "employeeID", employeeID);
		System.out.println("Salary Type: " + sType);
    	
    	//Bonus
		bonus = doubleQuery("bonus", "Employee", "employeeID", employeeID);
		System.out.println("Bonus Amount: " + bonus);
		
		System.out.println();
		presentMenu(userType);
    }
    
    
    public static void viewPaycheck(String employeeID) {
    	
    	String paycheckID="";
    	double income=0, stateTax=0, fedTax=0, socialSecurity=0, medicare=0, f1k=0, ins=0;
    	
    	//PaycheckID
    	paycheckID = stringQuery("paycheckID", "Paycheck", "employeeID", employeeID);
		System.out.println("Paycheck ID: " + paycheckID);
    	
    	//Income
		income = doubleQuery("income", "Paycheck", "employeeID", employeeID);
		System.out.println("Gross Pay: " + income);
    	
    	//stateTax
		stateTax = doubleQuery("stateTax", "Paycheck", "employeeID", employeeID);
		System.out.println("State Taxes: " + stateTax);
    	
    	//fedTax
		fedTax = doubleQuery("fedTax", "Paycheck", "employeeID", employeeID);
		System.out.println("Federal Taxes: " + fedTax);
		
    	//socialSecurity
		socialSecurity = doubleQuery("socialSecurity", "Paycheck", "employeeID", employeeID);
		System.out.println("Social Security: " + (socialSecurity));
		
		//medicare
		medicare = doubleQuery("medicare", "Paycheck", "employeeID", employeeID);
		System.out.println("Medicare: " + (medicare));
		
		//401K
		f1k = doubleQuery("fourOneKDeduction", "Paycheck", "employeeID", employeeID);
		System.out.println("401K Deduction: " + f1k);
		
		//insurancePremium
		ins = doubleQuery("insurancePremium", "Paycheck", "employeeID", employeeID);
		System.out.println("Insurance Premium: " + ins);
		
		double netPay = income - stateTax - fedTax - (socialSecurity) - (medicare) - f1k - ins;
		System.out.println("NET PAY = " + netPay);
		
		System.out.println();
		presentMenu(userType);
		
    }
    
    
    public static void viewW2(String employeeID) {
    	
    	String w2ID="";
    	double yearly=0, ded=0, bonus=0;
    	
    	//W2ID
    	w2ID = stringQuery("w2ID", "W2", "employeeID", employeeID);
		System.out.println("W2 ID: " + w2ID);
    	
    	//yearlyIncome
		yearly = doubleQuery("yearlyIncome", "W2", "employeeID", employeeID);
		System.out.println("Year Income: " + yearly);
    	
    	//deducions
    	ded = doubleQuery("deductions", "W2", "employeeID", employeeID);
    	System.out.println("Deductions: " + ded);
				
    	//bonuses
    	bonus = doubleQuery("bonuses", "W2", "employeeID", employeeID);
    	System.out.println("Bonuses: " + bonus);
    	
    	System.out.println();
		presentMenu(userType);
    }
    
    
    public static void viewInsurance(String employeeID) {
    	
    	String planID="";
    	double indEmpCont=0, indCompCont=0, famIndCont=0, famCompCont=0;
    	
    	//planID
    	planID = stringQuery("planID", "InsurancePlan", "employeeID", employeeID);
		System.out.println("Health Plan ID: " + planID);
    	
    	//individualEmployeeContr
		indEmpCont = doubleQuery("individualEmployeeContr", "InsurancePlan", "employeeID", employeeID);
		System.out.println("Individual Employee Contribution: " + indEmpCont);
    	
    	//individualCompanyContr
		indCompCont = doubleQuery("individualCompanyContr", "InsurancePlan", "employeeID", employeeID);
		System.out.println("Individual Company Contribution: " + indCompCont);
		
    	//familyEmployeeContr
		famIndCont = doubleQuery("familyEmployeeContr", "InsurancePlan", "employeeID", employeeID);
		System.out.println("Family Employee Contribution: " + famIndCont);
		
    	//familyCompanyContr
		famCompCont = doubleQuery("familyCompanyContr", "InsurancePlan", "employeeID", employeeID);
		System.out.println("Family Company Contribution: " + famCompCont);
    	
    	System.out.println();
		presentMenu(userType);
    	
    }
    
    
    public static void viewBenefits(String employeeID) {
    	
    	String accountNum="", healthPlan="", attorneyPlan="", lifeIns="";
    	double f1kEmp=0, f1kComp=0;
    	
    	//planAccountNum
    	accountNum = stringQuery("planAccountNum", "Benefits", "employeeID", employeeID);
		System.out.println("Account Number: " + accountNum);
    	
    	//healthPlan
		healthPlan = stringQuery("healthPlan", "Benefits", "employeeID", employeeID);
		System.out.println("Health Plan: " + healthPlan);
    	
    	//fourOneKEmployeeContr
		f1kEmp = doubleQuery("fourOneKEmployeeContr", "Benefits", "employeeID", employeeID);
		System.out.println("401K Employee Contribution: " + f1kEmp);
    	
    	//fourOneKCompanyContr
		f1kComp = doubleQuery("fourOneKCompanyContr", "Benefits", "employeeID", employeeID);
		System.out.println("401K Company Contribution: " + f1kComp);
    	
    	//attorneyPlan
		attorneyPlan = stringQuery("attorneyPlan", "Benefits", "employeeID", employeeID);
		System.out.println("Attorney Plan: " + attorneyPlan);
    	
    	//lifeInsurance
		lifeIns = stringQuery("lifeInsurance", "Benefits", "employeeID", employeeID);
		System.out.println("Life Insurance: " + lifeIns);
    	
    	System.out.println();
		presentMenu(userType);
    	
    }
    
    
    public static void logout() {
    	
	   	 userName = null;
	  	 pwd = null;
	  	 userType = null;
	  	 
	  	 System.out.println("Session Terminated");
    	
    }
    
}