import java.util.*;
import java.sql.*;

public class DBApp {
	
    public static String userType;
    public static String userName;
    public static String pwd;
    static boolean loginSuccess;
    static int attempts;
    
    // Alexis Postgres Info
    public static String user = "postgres";
    public static String sqlPwd = "postgres";
    
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
    
    public static boolean runSQL(String query) {
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
		
		boolean result = runSQL(update);
		
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
		
		boolean result = runSQL(update);
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
			
		boolean result = runSQL(update);
		
		if (result) {
			System.out.println("Benefit Plan added successfully");
		}
		
		scan.close();
    	
    }
    
    public static void addPaycheck() {
    	
    	int income, stateTax, fedTax, socialSecurity, medicare, f1kDeduction, insPremium;
    	String employeeID, paycheckID;
    	
    	
    	// Instantiate Scanner
    	Scanner scan = new Scanner(System.in);
    	
		System.out.println("Please provide the following information:");
		
		// Employee ID
		System.out.println("Employee ID: ");
		employeeID = scan.next();
		
		// Paycheck ID
		System.out.println("Paycheck ID: ");
		paycheckID = scan.next();
		
		//THE REST CAN BE OBTAINED THROUGH SELECT STATEMENTS
		// TO BE ADDED LATER
		
		// Income
		System.out.println("Gross Pay: ");
		income = scan.nextInt();
		
		// stateTax
		// 1. Get Employee stateName
		// 2. Get stateTaxRate from States using stateName
		// 3. Multiply state tax rate * income
		
		// fedTax
		// 1. Get Employee fedTaxBracket
		// 2. Get fedTaxRate from federalTax using fedTaxBracket
		// 3. Multiply federal tax rate * income
		
		// Social Security & medicare
		// 1. Get Employee employeeSSN
		// 2. Get ssEmpPortion from otherTaxes using employeeSSN
		// 3. Get medicare from otherTaxes using employeeSSN
		
		// 401K Deduction
		// 1. Use employeeID
		// 2. Get fourOneKEmployeeContr from Benefits using employeeID
		
		// Insurance Premium
		// 1. Use employeeID
		// 2. Get individualEmployeeContr from Benefits using employeeID
		// 3. Get familyEmployeeContr from Benefits using employeeID
		// 4. Add individualEmployeeContr + familyEmployeeContr
		
		
		// SQL CODE
		String update = "insert into Paycheck" + "\n" +
						"values(" + employeeID + "," + paycheckID + "," + income + "," + stateTax + "," +
						fedTax + "," + socialSecurity + "," + medicare + "," + f1kDeduction + "," + insPremium + ");";
		
		boolean result = runSQL(update);
		
		if (result) {
			System.out.println("Paycheck added successfully");
		}
		
		scan.close();
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
		
		boolean result = runSQL(update);
		
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
	 				"\n" + "2. Benefits" +								// Benefits
	 				"\n" + "3. InsurancePlan");							// InsurancePlan
			
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
		boolean result = runSQL(update);
		
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
		 
		boolean result = runSQL(update);
		
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
		 
		boolean result = runSQL(update);
		
		if (result) {
			System.out.println("Federal Tax Rate updated successfully");
		}
		 
		 scan.close();
    }
    
    public static void updateContribution(int selection) {
    	
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
    	
    	if (selection == 1) {				// otherTaxes -> Social Security
			 
		}
		else if (selection == 2) {			// Benefits -> Benefits
			 
		}
		else if (selection == 3) {			// InsurancePlan -> InsurancePlan
			 
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
			
			boolean result = runSQL(delete);
			
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
		
		if (selection == 1) {
			
		} else if (selection == 2) {
			
		} else if (selection == 3) {
			
		} else if (selection == 4) {
			
		} else if (selection == 5) {
			
		} else {
			System.out.println("Invalid command. Please try again");
			updateInfo();
		}
		
		scan.close();
		
    	presentMenu(userType);
    	
    }
    
    
    public static void logout() {
    	
	   	 userName = null;
	  	 pwd = null;
	  	 userType = null;
	  	 
	  	 System.out.println("Session Terminated");
    	
    }
    
}