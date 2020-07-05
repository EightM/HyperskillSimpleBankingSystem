package banking;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BankAccount {
    private String cardNumber;
    private int pin;
    private int balance;


    public BankAccount() {}

    public static BankAccount createNewBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.cardNumber = generateCardNumber();
        bankAccount.pin = generatePin();
        bankAccount.balance = 0;

        return bankAccount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "cardNumber='" + cardNumber + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    private static long generateAccountIdentifier() {
        return ThreadLocalRandom.current().nextLong(100000000L, 999999999L);
    }

    private static String generateCardNumber() {
        String cardNumberWithoutChecksum = "400000" + generateAccountIdentifier();
        return cardNumberWithoutChecksum + generateCheckSum(cardNumberWithoutChecksum);
    }

    private static int generateCheckSum(String cardNumberWithoutChecksum) {
        int sum = getLuhnBasedSumOfDigits(cardNumberWithoutChecksum);
        return (100 - sum) % 10;
    }

    public static int getLuhnBasedSumOfDigits(String cardNumberWithoutChecksum) {
        List<Integer> digits = Pattern.compile("").splitAsStream(cardNumberWithoutChecksum)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        IntStream.range(0, digits.size())
                .filter(i -> (i + 1) % 2 != 0)
                .forEach(i -> digits.set(i, digits.get(i) * 2));
        IntStream.range(0, digits.size())
                .filter(i -> digits.get(i) > 9)
                .forEach(i -> digits.set(i, digits.get(i) - 9));
        return digits.stream().mapToInt(i -> i).sum();
    }

    private static int generatePin() {
        return ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    public void printBalance() {
        System.out.println("Balance: " + balance);
    }
}
