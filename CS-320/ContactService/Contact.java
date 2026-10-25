/** Stores one contact and checks values before saving them. */
public final class Contact {
    private final String contactId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Contact(String contactId, String firstName, String lastName,
                   String phone, String address) {
        checkLength(contactId, 10, "Contact ID");
        this.contactId = contactId;
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
        setAddress(address);
    }

    private static void checkLength(String value, int limit, String field) {
        if (value == null || value.length() > limit) {
            throw new IllegalArgumentException(field + " is null or too long.");
        }
    }

    public String getContactId() { return contactId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public void setFirstName(String firstName) {
        checkLength(firstName, 10, "First name");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        checkLength(lastName, 10, "Last name");
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        if (phone == null || !phone.matches("[0-9]{10}")) {
            throw new IllegalArgumentException("Phone must contain exactly 10 digits.");
        }
        this.phone = phone;
    }

    public void setAddress(String address) {
        checkLength(address, 30, "Address");
        this.address = address;
    }
}
