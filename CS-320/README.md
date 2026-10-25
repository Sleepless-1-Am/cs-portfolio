# CS 320 Software Test Automation and QA

This portfolio entry includes my contact service from Project One and my summary and reflections report from Project Two. I used Java and JUnit 5 to check requirements, reject invalid inputs, and test changes to stored records.

## Project artifacts

- [Contact.java](ContactService/Contact.java)
- [ContactService.java](ContactService/ContactService.java)
- [ContactTest.java](ContactService/ContactTest.java)
- [ContactServiceTest.java](ContactService/ContactServiceTest.java)
- [Project Two summary and reflections report](CS320_Project_Two_Summary_and_Reflections.docx)

The contact service stores contacts in memory and supports adding, deleting, and updating contacts by ID. The tests check field limits, null values, phone formatting, duplicate IDs, and whether updates leave other data unchanged. The report explains my testing approach across the contact, task, and appointment services.

## How can I ensure that my code program or software is functional and secure

I start with clear requirements and test both valid and invalid inputs. In my contact service, I checked field limits, duplicate IDs, and whether rejected updates preserved the original data. I rerun tests after changes and review the code for mistakes. For a complete application, I would also check dependencies, permissions, and sensitive data handling because passing unit tests alone does not prove security.

## How do I interpret user needs and incorporate them into a program

I break user needs into small requirements that I can test. For example, the contact requirements became rules for fixed IDs, name lengths, and phone numbers with exactly ten digits. I write tests for the expected results and ask questions when a requirement is unclear. This helps me build what the user needs without adding assumptions.

## How do I approach designing software

I start by identifying the data, required actions, and limits. I separated contact validation from the service that manages records, which made each part easier to understand and test. I work in small steps, use clear names, and reuse simple logic when it helps. I also plan for invalid inputs and future changes instead of only designing for a successful example.

## Running the contact tests in Eclipse

1. Create a Java project without `module-info.java`.
2. Import the four files in `ContactService` into `src` and keep them in the default package.
3. Open project **Properties > Java Build Path > Libraries**. Select **Classpath > Add Library > JUnit > JUnit 5**, then apply the change.
4. Right-click the project and choose **Run As > JUnit Test** to run both test classes.

No database or user interface is required.
