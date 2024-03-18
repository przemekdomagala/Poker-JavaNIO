package pz1.poker.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyScannerTest {
    ByteArrayInputStream inputStream;

    @BeforeEach
    public void checkInSetUp() {
        inputStream = new ByteArrayInputStream("1\naaa".getBytes());
        System.setIn(inputStream);
    }
    @Test
    void scannerTest() {
        MyScanner.hasNextInt();
        assertEquals(1, MyScanner.nextInt());
        assertEquals("aaa", MyScanner.nextLine());
    }
}