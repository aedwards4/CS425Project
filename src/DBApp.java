import java.util.*;
import java.sql.*;

public class DBApp {
	
    static String userType;
    static String userName;
    static String pwd;
    static boolean loginSuccess;
    static int attempts;
    
    public static void main(String[] args){
    	
    	 userName = null;
      	 String = null;
      	 userType = null;
      	 loginSuccess = false;
      	 attempts = 0;
      	 
      	 // Sign in the user
      	 signIn();
      	 
      	 if loginSuccess {
      		 
          	 // Present the user menu
          	 presentMenu(userType)
          	 
      	 }

    }
    
    
    public static void signin(){
        Scanner input = new Scanner(System.in);
        
        //user log in with password and userName
        System.out.println("------Log In------");
        System.out.println("User name:/n");
        String userName = input.nextLine();
        System.out.println("Password:/n");
        String password = input.nextLine();
        
        //validate the log in credentials
        while (!validate(userName, password)){
            System.out.println("Invalid username/password!");
            attempts += 1;
            
            if attempts >= 3 {
            	System.out.println("Too many unsuccessfule attemps. Try again later.");
            }
            else {
            	signIn();
            }
        }
        
        System.out.println("Logged in successfully!");
        loginSuccess = true;
        attempts = 0;
        
        input.close();
        
    }
    
    
    public static boolean validate(String loginName, String password){
        try(
            Connection conn = DriverManager.getConnection(
            "" //leave blank for now
            )
            Statement stmt = conn.createStatement();
        ){
            String strSelect = "select userName, pwd, userType from user";
            System.out.println("The SQL statement is: " + strSelect + "\n" ); // Echo for debugging
            
            ResultSet rset = stmt.executeQuery(strSelect);
            
            while(rset.next()){
                String userName = rset.getString("userName");
                String pwd = rset.getString("pwd");
                userType = rset.getString("userType"); //store static var userType
                
                if(loginName == userName){
                    if(pwd == password){
                    	this.userName = userName;
                    	this.pwd = password;
                    	this.userType = userType;
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
    }
    
    
    public static void presentMenu(String userType) {
    	
    	 int temp = 0;
    	 Scanner scan = new Scanner(System.in);

	 	 if(userType == "Admin")
	 	 {
	 		 System.out.println("MENU" + 
	 				 		"\n" + "1: Add record" + 
	 				 		"\n" + "2: Update record" + 
	 				 		"\n" + "3: Delete record" + 
	 				 		"\n" + "4: Run report" + 
	 				 		"\n" + "5: Exit");
	 		 
	 		 temp = input.nextInt();
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
	 			 deleteRecord();
	 		 }
	 		 else if(temp == 4) // Run report
	 		 {
	 			 runReport();
	 		 }
	 		 else if(temp == 5) // Exit
	 		 {
	 			 exit();
	 		 }
	 		 else
	 		 {
	 			System.out.println(“Invalid command, Please try again”);
	 		 }
	 	 }
	 	 else
	 	 {
	 		 System.out.println("MENU" + 
	 				 		"\n" + "1: Update information" + 
	 				 		"\n" + "2: View information" + 
	 				 		"\n" + "3. Exit");
	 		 
	 		temp = input.nextInt();
			 if(temp == 1) // Update Information
			 {
				 System.out.println("Please add the following: Things that cannot be blank");
				 
			 }
			 else if(temp == 2) // View Information
			 {
				 
			 }
			else if(temp == 3) // Exit
			 {
				 
			 }
			else
			 {
				System.out.println(“Invalid command, Please try again”);
			 }
	 	 }
	 	 
	 	 scan.close()
    	
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
			System.out.println(“Invalid command, Please try again”);
		 }
		 
		 scan.close();
		 
		 presentMenu(this.userType);
    	
    }
    
    public static void addEmployee() {
    	
    	int employeeID, bonus, federalTaxBracket, ssn;
    	String firstName, lastName, jobTitle, salaryType, stateName;
    	
    	// Instantiate Scanner
    	Scanner scan = new Scanner(System.in);
    	
		System.out.println(“Please provide the following information:”);
		 
		// Employee ID
		System.out.println(“Employee ID: ”);
		employeeID = scan.nextInt();
		
		// Employee Name
		System.out.println(“Employee First Name: ”);
		firstName = scan.next();
		System.out.println(“Employee Last Name: ”);
		lastName = scan.next();
		
		// Job Title
		System.out.println(“Job Title: ”);
		jobTitle = scan.next();
		
		// Salary Type
		System.out.println(“Salary Type (W2/Hourly): ”);
		salaryType = scan.next();
		
		// Bonus
		System.out.println(“Yearly Bonus: ”);
		bonus = scan.nextInt();
		
		// Federal Tax Bracket
		System.out.println(“Federal Tax Bracket (1/2/3): ”);
		federalTaxBracket = scan.nextInt();
		
		// State
		System.out.println(“Employee State of Residence: ”);
		stateName = scan.next();
		
		// SSN
		System.out.println(“Employee SSN (No dashes): ”);
		ssn = scan.nextInt();
		
		
		
		// ADD SQL CODE HERE
		
		
		scan.close();
		
    }
    
    public static void addInsurancePlan() {
    	
    }
    
    public static void addBenefits() {
    	
    }
    
    public static void addPaycheck() {
    	
    }
    
    public static void addW2() {
    	
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
			 
		 }
		 else if (selection == 2) {			// State
			 
		 }
		 else if (selection == 3) {			// federalTax
			 
		 }
		 else if (selection == 4) {			// otherTaxes, Benefits, InsurancePlan
			 
			System.out.println("Which contribution would you like to update?" +
	 				"\n" + "1. Social Security" +						// otherTaxes
	 				"\n" + "2. Benefits" +								// Benefits
	 				"\n" + "3. InsurancePlan");							// InsurancePlan
			
			selection = scan.nextInt();
			if (selection == 1 || selection == 2 || selection == 3) {
				updateContribution(selection)
			}
			else {
 				System.out.println(“Invalid command, Please try again”);
 				updateRecord();
 			}
			
		 }
		 else {
			System.out.println(“Invalid command, Please try again”);
			updateRecord();
		 }
		 
		 scan.close();
		 
		 presentMenu(this.userType);
    }
    
    
    public static void updateEmployee() {
		 // Instantiate Scanner
		 Scanner scan = new Scanner(System.in);
		 
		 
		 
		 scan.close()
    }
    
    
    public static void updateState() {
		 // Instantiate Scanner
		 Scanner scan = new Scanner(System.in);
		 
		 
		 
		 scan.close()
    }
    
    
    public static void updateFederal() {
		 // Instantiate Scanner
		 Scanner scan = new Scanner(System.in);
		 
		 
		 
		 scan.close()
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
    	
    	
    	scan.close()
    	
    }
    
    
    public static void deleteRecord() {
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
		 
		
		
		scan.close()
		
    	presentMenu(this.userType);
    }
    
    
    public static void runReport() {
    	
		// Instantiate Scanner
		Scanner scan = new Scanner(System.in);
    	
		
		
		
		scan.close();
		
    	presentMenu(this.userType);
    }
    
    
}