package banking;

import java.util.Objects;
import java.util.Scanner;

public class BankSystem {

    private BankAccount currentAccount;

    public void showStartMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    public void showLoginMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    public void createAccount() {
        BankAccount bankAccount = BankAccount.createNewBankAccount();
        Database.save(bankAccount);
        System.out.println("Your card has been created");
        System.out.println("You card number:");
        System.out.println(bankAccount.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(bankAccount.getPin());
    }

    public boolean logIntoAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number;");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        int pinCode = Integer.parseInt(scanner.nextLine());

        boolean isValidCheckSum = checkCheckSum(cardNumber);
        BankAccount bankAccount = Database.load(cardNumber, pinCode);
        if (!isValidCheckSum || bankAccount == null || bankAccount.getPin() != pinCode) {
            System.out.println("Wrong card number or PIN!");
            return false;
        }

        System.out.println("You have successfully logged in!");
        currentAccount = bankAccount;
        return true;
    }

    private boolean checkCheckSum(String cardNumber) {
        return BankAccount.getLuhnBasedSumOfDigits(cardNumber) % 10 == 0;
    }

    public void printBalance() {
        Objects.requireNonNull(currentAccount);
        currentAccount.printBalance();
    }

    public boolean logOut() {
        Objects.requireNonNull(currentAccount);
        currentAccount = null;
        System.out.println("You have successfully logged out!");
        return false;
    }

    public void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

    public void addIncome() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter income:");
        int income = Integer.parseInt(scanner.nextLine());
        currentAccount.setBalance(currentAccount.getBalance() + income);
        System.out.println("Income was added!");
        Database.update(currentAccount);
    }

    public void transfer() {
        System.out.println("Transfer");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter card number:");

        String cardNumber = scanner.nextLine();
        BankAccount recipient = checkCardNumber(cardNumber);
        if (recipient == null) {
            return;
        }

        System.out.println("How much money yoy want to transfer:");
        int moneyToTransfer = Integer.parseInt(scanner.nextLine());
        if (moneyToTransfer > currentAccount.getBalance()) {
            System.out.println("Not enough money!");
            return;
        }

        currentAccount.setBalance(currentAccount.getBalance() - moneyToTransfer);
        Database.save(currentAccount);
        recipient.setBalance(recipient.getBalance() + moneyToTransfer);
        Database.save(recipient);

        System.out.println("Success!");
    }

    private BankAccount checkCardNumber(String cardNumber) {
        boolean isValidNumber = checkCheckSum(cardNumber);

        if (!isValidNumber) {
            System.out.println("Probably you made mistake in the card number." +
                    "Please try again!");
            return null;
        }

        BankAccount recipient = Database.load(cardNumber);
        if (recipient == null) {
            System.out.println("Such a card does not exist.");
            return null;
        }

        return recipient;
    }

    public void closeAccount() {
        Database.delete(currentAccount);
        currentAccount = null;
        System.out.println("The account has been closed!");
    }
}
