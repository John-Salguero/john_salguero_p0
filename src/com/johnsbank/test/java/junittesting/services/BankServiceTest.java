package com.johnsbank.test.java.junittesting.services;

import static org.junit.jupiter.api.Assertions.*;

import com.johnsbank.java.models.User;
import com.johnsbank.java.services.BankServiceImplementation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankServiceTest {
    static BankServiceImplementation service = new BankServiceImplementation();

    // Unsuccessful add User case
    @Test
    public void addUser() {
        User addUser = new User();
        addUser = service.addUser(addUser);

        assertTrue(addUser == null);
    }

    // Successful add User case

}
