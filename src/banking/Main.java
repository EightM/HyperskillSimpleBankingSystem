package banking;

import java.util.Scanner;

public class Main {

    private static boolean isLogged = false;
    private static final BankSystem bankSystem = new BankSystem();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (isLogged) {
                bankSystem.showLoginMenu();
            } else {
                bankSystem.showStartMenu();
            }
            int action = Integer.parseInt(scanner.nextLine());
            performAction(action);
        }
    }

    private static void performAction(int action) {
        switch (action) {
            case 1:
                if (!isLogged) {
                    bankSystem.createAccount();
                } else {
                    bankSystem.printBalance();
                }
                break;
            case 2:
                if (!isLogged) {
                    isLogged = bankSystem.logIntoAccount();
                } else {
                    isLogged = bankSystem.logOut();
                }
                break;
            case 0:
                bankSystem.exit();
                break;
            default:
                System.out.println("Wrong option");
        }
    }

}
