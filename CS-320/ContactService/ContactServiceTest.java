import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ContactServiceTest {
    private ContactService service;

    @BeforeEach
    void setUp() {
        service = new ContactService();
        service.addContact(new Contact("C1", "Oziel", "Aldana", "0123456789", "100 Main St"));
        service.addContact(new Contact("C2", "Ana", "Lopez", "1112223333", "200 Oak St"));
    }

    @Test
    void addsContactsWithDifferentIds() {
        assertEquals("Oziel", service.getContact("C1").getFirstName());
        assertEquals("Ana", service.getContact("C2").getFirstName());
    }

    @Test
    void rejectsDuplicateIdWithoutReplacingOriginal() {
        Contact duplicate = new Contact("C1", "Bob", "Smith", "9998887777", "300 Pine St");
        assertThrows(IllegalArgumentException.class, () -> service.addContact(duplicate));
        assertEquals("Oziel", service.getContact("C1").getFirstName());
    }

    @Test
    void rejectsNullContact() {
        assertThrows(IllegalArgumentException.class, () -> service.addContact(null));
    }

    @Test
    void deletesOnlyTheRequestedContact() {
        service.deleteContact("C1");
        assertThrows(IllegalArgumentException.class, () -> service.getContact("C1"));
        assertEquals("Ana", service.getContact("C2").getFirstName());
    }

    @Test
    void updatesEachFieldForTheRequestedId() {
        service.updateFirstName("C1", "1234567890");
        service.updateLastName("C1", "ABCDEFGHIJ");
        service.updatePhone("C1", "9876543210");
        service.updateAddress("C1", "123456789012345678901234567890");
        Contact contact = service.getContact("C1");
        assertAll(
            () -> assertEquals("C1", contact.getContactId()),
            () -> assertEquals("1234567890", contact.getFirstName()),
            () -> assertEquals("ABCDEFGHIJ", contact.getLastName()),
            () -> assertEquals("9876543210", contact.getPhone()),
            () -> assertEquals("123456789012345678901234567890", contact.getAddress()),
            () -> assertEquals("Ana", service.getContact("C2").getFirstName()),
            () -> assertEquals("Lopez", service.getContact("C2").getLastName()),
            () -> assertEquals("1112223333", service.getContact("C2").getPhone()),
            () -> assertEquals("200 Oak St", service.getContact("C2").getAddress()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"missing"})
    void rejectsOperationsForUnknownId(String id) {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> service.getContact(id)),
            () -> assertThrows(IllegalArgumentException.class, () -> service.deleteContact(id)),
            () -> assertThrows(IllegalArgumentException.class, () -> service.updateFirstName(id, "Bob")),
            () -> assertThrows(IllegalArgumentException.class, () -> service.updateLastName(id, "Smith")),
            () -> assertThrows(IllegalArgumentException.class, () -> service.updatePhone(id, "9998887777")),
            () -> assertThrows(IllegalArgumentException.class, () -> service.updateAddress(id, "300 Pine St")));
        assertEquals("Oziel", service.getContact("C1").getFirstName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidNameUpdates(String value) {
        assertThrows(IllegalArgumentException.class, () -> service.updateFirstName("C1", value));
        assertThrows(IllegalArgumentException.class, () -> service.updateLastName("C1", value));
        assertEquals("Oziel", service.getContact("C1").getFirstName());
        assertEquals("Aldana", service.getContact("C1").getLastName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "123456789", "12345678901", "12345A7890", "12345-7890", "12345 7890"})
    void rejectsInvalidPhoneUpdates(String value) {
        assertThrows(IllegalArgumentException.class, () -> service.updatePhone("C1", value));
        assertEquals("0123456789", service.getContact("C1").getPhone());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1234567890123456789012345678901"})
    void rejectsInvalidAddressUpdates(String value) {
        assertThrows(IllegalArgumentException.class, () -> service.updateAddress("C1", value));
        assertEquals("100 Main St", service.getContact("C1").getAddress());
    }

    @Test
    void separateServicesDoNotShareContacts() {
        ContactService other = new ContactService();
        assertThrows(IllegalArgumentException.class, () -> other.getContact("C1"));
    }

    @Test
    void equalIdStringsReferToTheSameContact() {
        Contact original = service.getContact("C1");
        Contact duplicate = new Contact(new String("C1"), "Bob", "Smith", "9998887777", "300 Pine St");
        assertThrows(IllegalArgumentException.class, () -> service.addContact(duplicate));
        assertSame(original, service.getContact(new String("C1")));
        service.updateFirstName(new String("C1"), "Luis");
        assertEquals("Luis", original.getFirstName());
        service.deleteContact(new String("C1"));
        assertThrows(IllegalArgumentException.class, () -> service.getContact("C1"));
        assertEquals("Ana", service.getContact("C2").getFirstName());
    }

    @Test
    void firstNameUpdateLeavesOtherFieldsAlone() {
        service.updateFirstName("C1", "Luis");
        checkContactDetails("Luis", "Aldana", "0123456789", "100 Main St");
    }

    @Test
    void lastNameUpdateLeavesOtherFieldsAlone() {
        service.updateLastName("C1", "Garcia");
        checkContactDetails("Oziel", "Garcia", "0123456789", "100 Main St");
    }

    @Test
    void phoneUpdateLeavesOtherFieldsAlone() {
        service.updatePhone("C1", "9876543210");
        checkContactDetails("Oziel", "Aldana", "9876543210", "100 Main St");
    }

    @Test
    void addressUpdateLeavesOtherFieldsAlone() {
        service.updateAddress("C1", "400 Elm St");
        checkContactDetails("Oziel", "Aldana", "0123456789", "400 Elm St");
    }

    private void checkContactDetails(String firstName, String lastName,
                                     String phone, String address) {
        Contact contact = service.getContact("C1");
        assertAll(
            () -> assertEquals("C1", contact.getContactId()),
            () -> assertEquals(firstName, contact.getFirstName()),
            () -> assertEquals(lastName, contact.getLastName()),
            () -> assertEquals(phone, contact.getPhone()),
            () -> assertEquals(address, contact.getAddress()),
            () -> assertEquals("Ana", service.getContact("C2").getFirstName()),
            () -> assertEquals("Lopez", service.getContact("C2").getLastName()),
            () -> assertEquals("1112223333", service.getContact("C2").getPhone()),
            () -> assertEquals("200 Oak St", service.getContact("C2").getAddress()));
    }
}
