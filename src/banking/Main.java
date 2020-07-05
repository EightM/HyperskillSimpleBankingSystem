package banking;

import java.util.Scanner;

public class Main {

    private static boolean isLogged = false;
    private static final BankSystem bankSystem = new BankSystem();

    public static void main(String[] args) {

       // String dbFilename = "db.s3db";
        if (args.length != 2) {
            System.out.println("Please specify db name");
            System.exit(0);
        }
        String dbFilename = args[1];
        Database.init(dbFilename);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (isLogged) {
                bankSystem.showLoginMenu();
            } else {
                bankSystem.showStartMenu();
            }
            int action = Integer.parseInt(scanner.nextLine());
            if (isLogged) {
                performLoginAction(action);
            } else {
                performAction(action);
            }
        }
    }

    private static void performLoginAction(int action) {
        switch (action) {
            case 1:
                bankSystem.printBalance();
                break;
            case 2:
                bankSystem.addIncome();
                break;
            case 3:
                bankSystem.transfer();
                break;
            case 4:
                bankSystem.closeAccount();
                isLogged = false;
                break;
            case 0:
                bankSystem.exit();
                break;
            default:
                System.out.println("Wrong option");
        }
    }

    private static void performAction(int action) {
        switch (action) {
            case 1:
                bankSystem.createAccount();
                break;
            case 2:
                isLogged = bankSystem.logIntoAccount();
                break;
            case 0:
                bankSystem.exit();
                break;
            default:
                System.out.println("Wrong option");
        }
    }

}
