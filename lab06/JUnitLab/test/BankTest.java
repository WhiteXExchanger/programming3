package test;

import junitlab.bank.AccountNotExistsException;
import junitlab.bank.Bank;
import junitlab.bank.impl.*;
import org.junit.jupiter.api;

public class BankTest {
    BankTest() {
        Bank bank = new FirstNationalBank();
    }
    @Test
    public void testOpenAccount() {
        String szamlaszam = assertAll(bank.openAccount());
        assertAll(0, bank.getBalance(szamlaszam));
    }
    
    public void testUniqueAccount() {
        String szamlaszam1 = bank.openAccount();
        String szamlaszam2 = bank.openAccount();
        assertNotEquals(szamlaszam1, szamlaszam2);
    }

    public void testInvalidAccount() {
        assertThrows(AccountNotExistsException, bank.getBalance(null));
    }

    public void testDeposit() {
        String szamlaszam = bank.openAccount();
        bank.deposit(szamlaszam, 2000);
        assertAll(bank.getBalance(szamlaszam), 2000);
    }
    
}