import java.util.*;
import java.sql.*;

public class TestApp {
	
    public static String userType;
    public static String userName;
    public static String pwd;
    static boolean loginSuccess;
    static int attempts;
    
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
      	 
      	 // Sign in the user
      	 signin();
      	 
      	 if (loginSuccess) {
      		 
          	 // Present the user menu
          	 presentMenu(userType);
          	 
      	 }

    }
    
    
    public static void signin(){
        Scanner input = new Scanner(System.in);
        
        //user log in with password and userName
        System.out.println("------Log In------");
        System.out.println("User name:");
        String userName = input.next();
        System.out.println("Password:");
        String password = input.next();
        
        //validate the log in credentials
        while (!validate(userName, password)){
            System.out.println("Invalid username/password!");
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
        
        input.close();
        
    }
    
    
    public static boolean validate(String loginName, String password){
        try( 
            Connection conn = DriverManager.getConnection("jdbc:postgressql://localhost:5432/postgres", user, sqlPwd);
       
            Statement stmt = conn.createStatement();
        ){
            String strSelect = "select userName, pwd, userType from userTable";
            System.out.println("The SQL statement is: " + strSelect + "\n" ); // Echo for debugging
            
            ResultSet rset = stmt.executeQuery(strSelect);
            
            while(rset.next()){
                String username = rset.getString("userName");
                String pword = rset.getString("pwd");
                String usertype = rset.getString("userType"); //store static var userType
                
                if(loginName == username){
                    if(pword == password){
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
			Connection conn = DriverManager.getConnection("jdbc:postgressql://localhost:5432/postgres", user, sqlPwd);
			
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
    
    
    public static boolean querySQL(String query) {
		try {

			// Create connection
			Connection conn = DriverManager.getConnection("jdbc:postgressql://localhost:5432/postgres", user, sqlPwd);
			
			// Create statement
			Statement stmt = conn.createStatement();
			
			// Execute statement
			rset = stmt.executeQuery(query);
			
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
    
    
    public static void presentMenu(String userType) {
    	
    	 int temp = 0;
    	 Scanner scan = new Scanner(System.in);

	 	 if(userType == "Admin")
	 	 {
	 		 System.out.println("MENU" + 
	 				 		"\n" + "1: Add record" + 
	 				 		"\n" + "2: Update record" + 
	 				 		"\n" + "3: Remove employee" + 
	 				 		"\n" + "4: Run report" + 
	 				 		"\n" + "5: Logout");
	 		 
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
	 	 else
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
				 viewInfo();
			 }
			else if(temp == 3) // Exit
			 {
				 logout();
			 }
			else
			 {
				System.out.println("Invalid command, please try again");
			 }
	 	 }
	 	 
	 	 scan.close();
    	
    }
    
    
    public static void addRecord() {
    	
    	 // Instantiate Scanner
		 Scanner scan = new Scanner(System.in);
		 
		 // Print out options
		 System.out.println("What kind of record would you like to add?" +
				 				"\n" + "1. Employee" +								// Employee
				 				"\n" + "2. Insurance Plan" +						// InsurancePlan
				 				"\n" + "3. Benefits" +								// Benefits
				 				"\n" + "4. Paycheck" +								// Paycheck
				 				"\n" + "5. W2");									// w2
				 				
		 int selection = scan.nextInt();
		 if (selection == 1) {				// Employee
			 addEmployee();
		 }
		 else if (selection == 2) {			// InsurancePlan
			 addInsurancePlan();
		 }
		 else if (selection == 3) {			// Benefits
			 addBenefits();
		 }
		 else if (selection == 4) {			// Paycheck
			addPaycheck();
		 }
		 else if (selection == 5) {			// w2
			addW2();
		 }
		 else {
			System.out.println("Invalid command, please try again");
		 }
		 
		 scan.close();
		 
		 presentMenu(userType);
    	
    }
    
    
    // 3 Folding Points to this function ( not sure why )
    public static void addEmployee() {
    	
    	int bonus, federalTaxBracket, ssn;
    	String employeeID, firstName, lastName, jobTitle, salaryType, stateName;
    	
    	// Instantiate Scanner
    	Scanner scan = new Scanner(System.in);
    	
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
		jobTitle = scan.next();
		
		// Salary Type
		System.out.println("Salary Type (W2/Hourly): ");
		salaryType = scan.next();
		
		// Bonus
		System.out.println("Yearly Bonus: ");
		bonus = scan.nextInt();
		
		// Federal Tax Bracket
		System.out.println("Federal Tax Bracket (1/2/3): ");
		federalTaxBracket = scan.nextInt();
		
		// State
		System.out.println("Employee State of Residence: ");
		stateName = scan.next();
		
		// SSN
		System.out.println("Employee SSN (No dashes): ");
		ssn = scan.nextInt();
		
		// SQL CODE
		
		String update = "insert into Employee" + "\n" +
				"values(" + employeeID + "," + firstName + "," + lastName + "," +
				jobTitle + "," + salaryType + "," + bonus + "," + federalTaxBracket +
				stateName + "," + ssn + ");";
		
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Employee added successfully");
		}
		
		scan.close();
		
    }
    
    
    public static void addInsurancePlan() {
    	
    	int indEmpCont, indCompCont, famEmpCont, famCompCont;
    	String planID, employeeID;
    	
    	// Instantiate Scanner
    	Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the following information:");
		 
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// Plan ID
		System.out.println("Plan ID: ");
		planID = scan.next();
		
		// Individual Employee Contribution
		System.out.println("Individual Employee Contribution: ");
		indEmpCont = scan.nextInt();
		
		// Individual Company Contribution
		System.out.println("Individual Company Contribution: ");
		indCompCont = scan.nextInt();
		
		// Family Employee Contribution
		System.out.println("Family Employee Contribution: ");
		famEmpCont = scan.nextInt();
		
		// Family Company Contribution
		System.out.println("Family Company Contribution: ");
		famCompCont = scan.nextInt();
		
		
		// SQL CODE
		String update = "insert into InsurancePlan" + "\n" +
						"values(" + planID + "," + indEmpCont + "," + indCompCont + "," +
						famEmpCont + "," + famCompCont + "," + employeeID + ");";
		
		boolean result = updateSQL(update);
		if (result) {
			System.out.println("Insurance Plan added successfully");
		}
		
		scan.close();
		
    }
    
    
    public static void addBenefits() {
    	
    	int f1kEmpCont, f1kCompCont;
    	String planAccNum, healthPlan, attorneyPlan, lifeIns, employeeID;
    	
    	// Instantiate Scanner
    	Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the following information:");
		
		// 401k Employee Contribution
		System.out.println("401k Employee Contribution: ");
		f1kEmpCont = scan.nextInt();
		
		// 401k Company Contribution
		System.out.println("401k Company Contribution: ");
		f1kCompCont = scan.nextInt();
		
		// Plan Account Number
		System.out.println("Plan Account Number: ");
		planAccNum = scan.next();
		
		// Health Plan
		System.out.println("Health Plan: ");
		healthPlan = scan.next();
		
		// Attorney Plan
		System.out.println("Attorney Plan: ");
		attorneyPlan = scan.next();
		
		// Life Insurance
		System.out.println("Life Insurance: ");
		lifeIns = scan.next();
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		
		// SQL CODE
		String update = "insert into Benefits" + "\n" +
						"values(" + f1kEmpCont + "," + f1kCompCont + "," + planAccNum + "," +
						healthPlan + "," + attorneyPlan + "," + lifeIns + "," + employeeID + ");";
			
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Benefit Plan added successfully");
		}
		
		scan.close();
    	
    }
    
    
    public static void addPaycheck() {

    	String employeeID, paycheckID;
    	double income, stateTax, fedTax, socialSecurity, medicare, f1kDeduction, insPremium;
    	
    	
    	// Instantiate Scanner
    	Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the following information:");
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// Paycheck ID
		System.out.println("Paycheck ID: ");
		paycheckID = scan.next();
		
		// Income
		System.out.println("Gross Pay: ");
		income = scan.nextDouble();
		
		// stateTax
		stateTax = getStateTax(employeeID, income);
		
		// fedTax
		fedTax = getFedTax(employeeID, income);
		
		// Social Security & medicare
		socialSecurity = getSS(employeeID);
		medicare = getMedicare(employeeID);
		
		// 401K Deduction
		f1kDeduction = get401(employeeID);
		
		// Insurance Premium
		insPremium = getInsPremium(employeeID);
		
		// SQL CODE
		String update = "insert into Paycheck" + "\n" +
						"values(" + employeeID + "," + paycheckID + "," + income + "," + stateTax + "," +
						fedTax + "," + socialSecurity + "," + medicare + "," + f1kDeduction + "," + insPremium + ");";
		
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Paycheck added successfully");
		}
		
		scan.close();
    }
    
    
    public static double getStateTax(String employeeID, double income) {
    	//Initialize variables
		String stateName = "";
		String query = "";
		double stateTaxRate = 0.0;
		double stateTax = 0;
    	boolean success;
		
		// 1. Get Employee stateName
		query = "SELECT stateName" + "\n" +
				"FROM Employee" + "\n" +
				"WHERE employeeID =" + employeeID + ");";
		
		success = querySQL(query);
		if (success) {
			
			// Reset success variable
			success = false;
			
			try {
				while(rset.next()) {
					stateName = rset.getString("stateName");
				}
			}
			catch(SQLException sqle) {
				// Handle exceptions
				System.out.println("SQLException: " + sqle);
			}
			
			// 2. Get stateTaxRate from States using stateName
			query = "SELECT stateTaxRate" + "\n" +
					"FROM States" + "\n" +
					"WHERE stateName = " + stateName + ");";
			
			success = querySQL(query);
			if (success) {
				
				try {
					while(rset.next()) {
						stateTaxRate = rset.getDouble("stateTaxRate");
					}
				}
				catch(SQLException sqle) {
					// Handle exceptions
					System.out.println("SQLException: " + sqle);
				}
				
				// 3. Multiply state tax rate * income
				stateTax = stateTaxRate * income;
			}	
		}
		return stateTax;
    }
    
    
    public static double getFedTax(String employeeID, double income) {
    	//Initialize variables
		String taxBracket = "";
		String query = "";
		double fedTaxRate = 0.0;
		double fedTax = 0;
    	boolean success;
		
    	// 1. Get Employee fedTaxBracket
		query = "SELECT federalTaxBracket" + "\n" +
				"FROM Employee" + "\n" +
				"WHERE employeeID =" + employeeID + ");";
		
		success = querySQL(query);
		if (success) {
			
			// Reset success variable
			success = false;
			
			try {
				while(rset.next()) {
					taxBracket = rset.getString("federalTaxBracket");
				}
			}
			catch(SQLException sqle) {
				// Handle exceptions
				System.out.println("SQLException: " + sqle);
			}
			
			// 2. Get fedTaxRate from federalTax using fedTaxBracket
			query = "SELECT fedTaxRate" + "\n" +
					"FROM federalTax" + "\n" +
					"WHERE federalTaxBracket = " + taxBracket + ");";
			
			success = querySQL(query);
			if (success) {
				
				try {
					while(rset.next()) {
						fedTaxRate = rset.getDouble("stateTaxRate");
					}
				}
				catch(SQLException sqle) {
					// Handle exceptions
					System.out.println("SQLException: " + sqle);
				}
				
				// 3. Multiply federal tax rate * income
				fedTax = fedTaxRate * income;
			}	
		}
		return fedTax;
    }
    
    
    public static double getSS(String employeeID) {
    	double ss = 0.0;
    	String query, ssn="";
    	boolean success = false;
    	
    	// 1. Get Employee employeeSSN
		query = "SELECT employeeSSN" + "\n" +
				"FROM Employee" + "\n" +
				"WHERE employeeID =" + employeeID + ");";
		success = querySQL(query);
		
		if (success) {
			
			// Reset success variable
			success = false;
			
			try {
				while(rset.next()) {
					ssn = rset.getString("employeeSSN");
				}
			}
			catch(SQLException sqle) {
				// Handle exceptions
				System.out.println("SQLException: " + sqle);
			}
			
			// 2. Get ssEmpPortion from otherTaxes using employeeSSN
			query = "SELECT ssEmpPortion" + "\n" +
					"FROM otherTaxes" + "\n" +
					"WHERE employeeSSN =" + ssn + ");";
			success = querySQL(query);
			
			if (success) {
				
				// Reset success variable
				success = false;
				
				try {
					while(rset.next()) {
						ss = rset.getDouble("ssEmpPortion");
					}
				}
				catch(SQLException sqle) {
					// Handle exceptions
					System.out.println("SQLException: " + sqle);
				}
			}
		}
    	return ss;
    }
    
    
    public static double getMedicare(String employeeID) {
    	double medicare = 0.0;
    	String query, ssn = "";
    	boolean success = false;
    	
    	// 1. Get Employee employeeSSN
		query = "SELECT employeeSSN" + "\n" +
				"FROM Employee" + "\n" +
				"WHERE employeeID =" + employeeID + ");";
		success = querySQL(query);
		
		if (success) {
			
			// Reset success variable
			success = false;
			
			try {
				while(rset.next()) {
					ssn = rset.getString("employeeSSN");
				}
			}
			catch(SQLException sqle) {
				// Handle exceptions
				System.out.println("SQLException: " + sqle);
			}
			
			// 2. Get medicare from otherTaxes using employeeSSN
			query = "SELECT medicare" + "\n" +
					"FROM otherTaxes" + "\n" +
					"WHERE employeeSSN =" + ssn + ");";
			success = querySQL(query);
			
			if (success) {
				
				// Reset success variable
				success = false;
				
				try {
					while(rset.next()) {
						medicare = rset.getDouble("medicare");
					}
				}
				catch(SQLException sqle) {
					// Handle exceptions
					System.out.println("SQLException: " + sqle);
				}
			}
		}
    	return medicare;
    }
    
    
    public static double get401(String employeeID) {
    	double f1k = 0.0;
    	String query;
    	boolean success = false;
    	
		// 1. Get fourOneKEmployeeContr from Benefits using employeeID
		query = "SELECT fourOneKEmployeeContr" + "\n" +
				"FROM Benefits" + "\n" +
				"WHERE employeeID =" + employeeID + ");";
		success = querySQL(query);
		
		if (success) {
			
			// Reset success variable
			success = false;
			
			try {
				while(rset.next()) {
					f1k = rset.getDouble("fourOneKEmployeeContr");
				}
			}
			catch(SQLException sqle) {
				// Handle exceptions
				System.out.println("SQLException: " + sqle);
			}
		}
    	return f1k;
    }
    
    
    public static double getInsPremium(String employeeID) {
    	double premium = 0.0, ind = 0.0, fam = 0.0;
    	String query;
    	boolean success = false;
    	
		// 1. Get individualEmployeeContr from InsurancePlan using employeeID
		query = "SELECT individualEmployeeContr" + "\n" +
				"FROM InsurancePlan" + "\n" +
				"WHERE employeeID =" + employeeID + ");";
		success = querySQL(query);
		
		if (success) {
			
			// Reset success variable
			success = false;
			
			try {
				while(rset.next()) {
					ind = rset.getDouble("individualEmployeeContr");
				}
			}
			catch(SQLException sqle) {
				// Handle exceptions
				System.out.println("SQLException: " + sqle);
			}
		}
		
		// 3. Get familyEmployeeContr from Benefits using employeeID
		query = "SELECT familyEmployeeContr" + "\n" +
				"FROM InsurancePlan" + "\n" +
				"WHERE employeeID =" + employeeID + ");";
		success = querySQL(query);
		
		if (success) {
			
			// Reset success variable
			success = false;
			
			try {
				while(rset.next()) {
					fam = rset.getDouble("familyEmployeeContr");
				}
			}
			catch(SQLException sqle) {
				// Handle exceptions
				System.out.println("SQLException: " + sqle);
			}
		}
    	
		// 4. Add individualEmployeeContr + familyEmployeeContr
    	premium = ind + fam;
    	
        return premium;
    }
    
    
    public static void addW2() {
    	int income, deductions, bonuses;
    	String employeeID, w2ID;
    	
    	
    	// Instantiate Scanner
    	Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the following information:");
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// W2 ID
		System.out.println("W2 ID: ");
		w2ID = scan.next();
		
		// Yearly Income
		System.out.println("Gross Pay: ");
		income = scan.nextInt();
		
		// Deductions
		System.out.println("Deductions: ");
		deductions = scan.nextInt();
		
		// Bonuses
		System.out.println("Bonuses: ");
		bonuses = scan.nextInt();
		
		// SQL CODE
		String update = "insert into W2" + "\n" +
						"values(" + employeeID + "," + w2ID + "," + income + "," +
						deductions + "," + bonuses + ");";
		
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("W2 added successfully");
		}

		scan.close();
    }
    
    
    public static void updateRecord() {
    	
		 // Instantiate Scanner
		 Scanner scan = new Scanner(System.in);
		 
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
		 
		 scan.close();
		 
		 presentMenu(userType);
    }
    
    
    public static void updateEmployee() {
    	 String update = "";
    	
		 // Instantiate Scanner
		 Scanner scan = new Scanner(System.in);
		 
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
					 	"SET jobTitle = " + jobTitle + "\n" +
					 	"WHERE employeeID = " + employeeID + ");";
		 }
		 else if (selection == 2) {			// salaryType
			 System.out.println("W2 or Hourly?: ");
			 String salaryType = scan.next();
			 
			 update = "UPDATE Employee" + "\n" +
					 	"SET salaryType = " + salaryType + "\n" +
					 	"WHERE employeeID = " + employeeID + ");";
		 }
		 else if (selection == 3) {			// bonus
			 System.out.println("Enter Bonus Amount: $");
			 int bonus = scan.nextInt();
			 
			 update = "UPDATE Employee" + "\n" +
					 	"SET bonus = " + bonus + "\n" +
					 	"WHERE employeeID = " + employeeID + ");";
		 }
		 else if (selection == 4) {			// stateName
			 System.out.println("State Name: ");
			 String stateName = scan.next();
			 
			 update = "UPDATE Employee" + "\n" +
					 	"SET stateName = " + stateName + "\n" +
					 	"WHERE employeeID = " + employeeID + ");";
		 }

		 
		 // SQL CODE HERE
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Employee updated successfully");
		}
			
			scan.close();
    }
    
    
    public static void updateState() {
    	
    	 String stateName, update;
    	 double taxRate;
    	
		 // Instantiate Scanner
		 Scanner scan = new Scanner(System.in);
		 
		 System.out.println("Enter state name: ");
		 stateName = scan.next();
		 
		 System.out.println("Enter new tax rate (0.##): ");
		 taxRate = scan.nextDouble();
		 
		 update = "UPDATE State" + "\n" +
				 	"SET stateTaxRate = " + taxRate + "\n" +
				 	"WHERE stateName = " + stateName + ");";
		 
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("State Tax Rate updated successfully");
		}
		 
		 scan.close();
    }
    
    
    public static void updateFederal() {
	   	 
    	 String fedBracket, update;
	   	 double taxRate;
   	
		 // Instantiate Scanner
		 Scanner scan = new Scanner(System.in);
		 
		 System.out.println("Enter federal tax bracket: ");
		 fedBracket = scan.next();
		 
		 System.out.println("Enter new tax rate (0.##): ");
		 taxRate = scan.nextDouble();
		 
		 update = "UPDATE federalTax" + "\n" +
				 	"SET fedTaxRate = " + taxRate + "\n" +
				 	"WHERE federalTaxBracket = " + fedBracket + ");";
		 
		boolean result = updateSQL(update);
		
		if (result) {
			System.out.println("Federal Tax Rate updated successfully");
		}
		 
		 scan.close();
    }
    
    
    public static void updateContribution(int selection) {
    	
    	String employeeID, update, query, ssn="";
    	double ss, f1k;
    	boolean success;
    	
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the Employee ID: ");
		employeeID = scan.next();
    	
    	if (selection == 1) {				// otherTaxes -> Social Security
    		System.out.println("What is the new contribution amount: ");
    		ss = scan.nextDouble();
    		
    		// 1. Get Employee employeeSSN
    		query = "SELECT employeeSSN" + "\n" +
    				"FROM Employee" + "\n" +
    				"WHERE employeeID =" + employeeID + ");";
    		success = querySQL(query);
    		
    		if (success) {
    			
    			// Reset success variable
    			success = false;
    			
    			try {
    				while(rset.next()) {
    					ssn = rset.getString("employeeSSN");
    				}
    				
    				update = "UPDATE otherTaxes" + "\n" +
    						 "SET ssCompanyPortion = " + ss + "\n" +
    						"WHERE employeeSSN = " + ssn + ");";
    				
    				success = updateSQL(update);
    				
    				if (success) {
    					System.out.println("Social Security Contribution update successfully");
    				} else {
    					System.out.println("Error: Please try again later");
    				}
  
    			}
    			catch(SQLException sqle) {
    				// Handle exceptions
    				System.out.println("SQLException: " + sqle);
    			}
    		}
		}
		else if (selection == 2) {			// Benefits -> 401k
    		System.out.println("What is the new contribution amount: ");
    		f1k = scan.nextDouble();
    		
    		update = "UPDATE Benefits" + "\n" +
					 "SET fourOneKCompanyContr = " + f1k + "\n" +
					 "WHERE employeeID = " + employeeID + ");";
    		
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
    	scan.close();
    }
    
    
    public static void indCont(String employeeID) {
    	
    	String update;
    	double ind;
    	boolean success;
    	
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
		
		System.out.println("New Individual Contribution Amount: ");
		ind = scan.nextDouble();
		
		update = "UPDATE InsurancePlan" + "\n" +
				 "SET individualCompanyContr = " + ind + "\n" +
				 "WHERE employeeID = " + employeeID + ");";
		
		success = updateSQL(update);
		
		if (success) {
			System.out.println("Individual Insurance Contribution update successfully");
		} else {
			System.out.println("Error: Please try again later");
		}
		
		
		scan.close();
    	
    }
    
    
    public static void familyCont(String employeeID) {
    	
    	String update;
    	double fam;
    	boolean success;
    	
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
		fam = scan.nextDouble();
		
		update = "UPDATE InsurancePlan" + "\n" +
				 "SET familyCompanyContr = " + fam + "\n" +
				 "WHERE employeeID = " + employeeID + ");";
		
		success = updateSQL(update);
		
		if (success) {
			System.out.println("Family Insurance Contribution update successfully");
		} else {
			System.out.println("Error: Please try again later");
		}
    	
		scan.close();
    }
    
    
    public static void deleteEmployee() {
    	
    	String employeeID = "";
    	String answer;
    	
    	// Instantiate Scanner
    	Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the Employee ID:");
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		 
		System.out.println("This operation is permanent and cannot be undone. Continue? (Y/N)");
		answer = scan.next();
		
		if (answer == "Y") {
			
			String delete = "DELETE FROM Employee WHERE employeeID = " + employeeID + ");";
			
			boolean result = updateSQL(delete);
			
			if (result) {
				System.out.println("Employee deleted successfully");
			}
			
		}
		else {
			System.out.println("Exiting operation...");
		}
		
		scan.close();
		 
		presentMenu(userType);
    }
    
    
    public static void runReport() {
    	
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
    	
		
		
		
		scan.close();
		
    	presentMenu(userType);
    }
    
    
    public static void updateInfo(){
    	
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
    	
		// Print out options
		System.out.println("What would you like to update?" +
				 				"\n" + "1. Insurance Plan" +								// InsurancePlan
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
		
		scan.close();
		
    	presentMenu(userType);
    	
    }
    
    
    public static void viewInfo() {
    	
    	String employeeID;
    	
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
    	
		// Print out options
		System.out.println("What would you like to view?" +
				 				"\n" + "1. Profile" +								// Employee
				 				"\n" + "2. Paycheck" +								// Paycheck
				 				"\n" + "3. W2" +									// W2
				 				"\n" + "4. Insurance Plan" +						// InsurancePlan
				 				"\n" + "5. Benefits");								// Benefits
		int selection = scan.nextInt();
		
		System.out.println("Enter your employeeID: ");
		employeeID = scan.next();
		
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
		
		scan.close();
		
    	presentMenu(userType);
    	
    }
    
    
    public static void viewProfile(String employeeID) {
    	
    }
    
    
    public static void viewPaycheck(String employeeID) {
    	
    }
    
    
    public static void viewW2(String employeeID) {
    	
    }
    
    
    public static void viewInsurance(String employeeID) {
    	
    }
    
    
    public static void viewBenefits(String employeeID) {
    	
    }
    
    
    public static void logout() {
    	
	   	 userName = null;
	  	 pwd = null;
	  	 userType = null;
	  	 
	  	 System.out.println("Session Terminated");
    	
    }
    
}