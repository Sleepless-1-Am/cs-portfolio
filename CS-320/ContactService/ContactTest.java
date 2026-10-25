import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ContactTest {
    private Contact sample() {
        return new Contact("C1", "Oziel", "Aldana", "0123456789", "100 Main St");
    }

    @Test
    void createsContactWithExpectedValues() {
        Contact contact = sample();
        assertAll(
            () -> assertEquals("C1", contact.getContactId()),
            () -> assertEquals("Oziel", contact.getFirstName()),
            () -> assertEquals("Aldana", contact.getLastName()),
            () -> assertEquals("0123456789", contact.getPhone()),
            () -> assertEquals("100 Main St", contact.getAddress()));
    }

    @Test
    void acceptsMaximumLengths() {
        Contact contact = new Contact("1234567890", "1234567890", "1234567890",
            "0123456789", "123456789012345678901234567890");
        assertAll(
            () -> assertEquals(10, contact.getContactId().length()),
            () -> assertEquals(10, contact.getFirstName().length()),
            () -> assertEquals(10, contact.getLastName().length()),
            () -> assertEquals(30, contact.getAddress().length()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidId(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new Contact(value, "Oziel", "Aldana", "0123456789", "100 Main St"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidFirstNameInConstructorAndSetter(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new Contact("C1", value, "Aldana", "0123456789", "100 Main St"));
        Contact contact = sample();
        assertThrows(IllegalArgumentException.class, () -> contact.setFirstName(value));
        assertEquals("Oziel", contact.getFirstName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidLastNameInConstructorAndSetter(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new Contact("C1", "Oziel", value, "0123456789", "100 Main St"));
        Contact contact = sample();
        assertThrows(IllegalArgumentException.class, () -> contact.setLastName(value));
        assertEquals("Aldana", contact.getLastName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "123456789", "12345678901", "12345A7890", "12345-7890", "12345 7890"})
    void rejectsInvalidPhoneInConstructorAndSetter(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new Contact("C1", "Oziel", "Aldana", value, "100 Main St"));
        Contact contact = sample();
        assertThrows(IllegalArgumentException.class, () -> contact.setPhone(value));
        assertEquals("0123456789", contact.getPhone());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1234567890123456789012345678901"})
    void rejectsInvalidAddressInConstructorAndSetter(String value) {
        assertThrows(IllegalArgumentException.class,
            () -> new Contact("C1", "Oziel", "Aldana", "0123456789", value));
        Contact contact = sample();
        assertThrows(IllegalArgumentException.class, () -> contact.setAddress(value));
        assertEquals("100 Main St", contact.getAddress());
    }

    @Test
    void updatesFieldsAtMaximumLengthsWithoutChangingId() {
        Contact contact = sample();
        contact.setFirstName("1234567890");
        contact.setLastName("ABCDEFGHIJ");
        contact.setPhone("9876543210");
        contact.setAddress("123456789012345678901234567890");
        assertAll(
            () -> assertEquals("C1", contact.getContactId()),
            () -> assertEquals("1234567890", contact.getFirstName()),
            () -> assertEquals("ABCDEFGHIJ", contact.getLastName()),
            () -> assertEquals("9876543210", contact.getPhone()),
            () -> assertEquals("123456789012345678901234567890", contact.getAddress()));
    }

    @Test
    void idIsPrivateAndFinal() throws NoSuchFieldException {
        int modifiers = Contact.class.getDeclaredField("contactId").getModifiers();
        assertTrue(Modifier.isPrivate(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
    }
}
