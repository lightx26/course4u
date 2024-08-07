package com.mgmtp.cfu.util;

import com.mgmtp.cfu.dto.MailContentUnit;
import com.mgmtp.cfu.entity.User;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

import java.util.List;
import java.util.Map;

public class EmailUtil {

    private static final Map<String, String> titleBackgroundColorMap = Map.of(
            "Important Deadline", "background-color: #F7C631;",
            "Registration Approved", "background-color: #50C878;",
            "Document Approved", "background-color: #50C878;",
            "Registration Declined", "background-color: #F44336;",
            "Document Declined", "background-color: #F44336;",
            "Registration Closed", "background-color: #808080;",
            "Verification of Course Documents", "background-color: #29CE1B;",
            "Welcome to Course4U!", "background-color: #861fa2;"
    );

    public static String generateSubject(String subject) {
        if (subject == null) {
            return null;
        }

        return "Course4U - " + subject;
    }

    public static MailContentUnit generateTitle(String title) {
        if (title == null) {
            return null;
        }

        return MailContentUnit.builder().id("title").innerContent(List.of(DocumentHelper.createText(title))).build();
    }

    public static MailContentUnit updateTitleStyle(String title) {
        if (title == null) {
            return null;
        }

        return MailContentUnit.builder().id("title-wrapper").style(titleBackgroundColorMap.get(title)).build();
    }

    public static MailContentUnit generateGreeting(String greeting, User user) {
        if (greeting == null) {
            return null;
        }

        if (user.getFullName() != null) {
            greeting = greeting.replace("{name}", user.getFullName().split(" ")[0]);
        } else {
            greeting = greeting.replace("{name}", user.getUsername());
        }
        return MailContentUnit.builder().id("greeting").innerContent(List.of(DocumentHelper.createText(greeting))).build();
    }

    public static MailContentUnit generateApproveContent(String courseName) {
        return MailContentUnit.builder().id("content").innerContent(generateApproveContentList(courseName)).build();
    }

    private static List<Node> generateApproveContentList(String courseName) {
        Text content1 = DocumentHelper.createText("Congratulations! Your registration for course ");
        Element courseNameElement = DocumentHelper.createElement("strong").addText(courseName);
        Text content2 = DocumentHelper.createText(" has been approved. You can now log in and start exploring a variety of courses tailored to your interests. Happy learning!");
        return List.of(content1, courseNameElement, content2);
    }

    public static MailContentUnit generateDeclineContent(String courseName) {
        return MailContentUnit.builder().id("content").innerContent(generateDeclineContentList(courseName)).build();
    }

    private static List<Node> generateDeclineContentList(String courseName) {
        Text content1 = DocumentHelper.createText("Unfortunately! Your registration for course ");
        Element courseNameElement = DocumentHelper.createElement("strong").addText(courseName);
        Text content2 = DocumentHelper.createText(" has been declined. You can now check the information about registration carefully and then re-submit registration. Happy learning!");
        return List.of(content1, courseNameElement, content2);
    }

    public static MailContentUnit generateCloseContent(String courseName) {
        return MailContentUnit.builder().id("content").innerContent(generateCloseContentList(courseName)).build();
    }

    private static List<Node> generateCloseContentList(String courseName) {
        Text content1 = DocumentHelper.createText("Your registration for course ");
        Element courseNameElement = DocumentHelper.createElement("strong").addText(courseName);
        Text content2 = DocumentHelper.createText(" has been closed. You can review the reasons below:");

        Element ulElement = DocumentHelper.createElement("ul").addAttribute("style", "text-align: left; margin: 20px 0;");
        Element liElement1 = DocumentHelper.createElement("li").addText("Refund Issued: We have processed your refund. If you have any problems, please contact our support team.");
        Element liElement2 = DocumentHelper.createElement("li").addText("Excessive Resubmissions: Your registration documents have been resubmitted too many times without necessary corrections.");
        Element liElement3 = DocumentHelper.createElement("li").addText("No Updates: Your registration has been inactive for an extended period without any document updates.");

        ulElement.add(liElement1);
        ulElement.add(liElement2);
        ulElement.add(liElement3);

        Text content3 = DocumentHelper.createText("We encourage you to review the registration process carefully. If you wish to register again in the future, please ensure all documents are accurate and complete. Thank you for your understanding, and we hope to assist you in your learning journey soon!");

        return List.of(content1, courseNameElement, content2, ulElement, content3);
    }

    public static MailContentUnit generateNotifyAccountantContent(String content) {
        return MailContentUnit.builder().id("content").innerContent(List.of(DocumentHelper.createText(content))).build();
    }

    public static MailContentUnit generateWelcomeContent() {
        return MailContentUnit.builder().id("content").innerContent(List.of(DocumentHelper.createText(
                "Welcome to Course4U! Thank you for using our application. Discover and enroll in a variety of courses tailored to your interests." +
                        " Happy learning!"))).build();
    }

    public static MailContentUnit generateDeadlineEmailContent(String courseName, String deadline) {
        return MailContentUnit.builder().id("content").innerContent(generateDeadlineEmailContentList(courseName, deadline)).build();
    }

    private static List<Node> generateDeadlineEmailContentList(String courseName, String deadline) {
        Text content1 = DocumentHelper.createText("The deadline for the ");
        Element courseNameElement = DocumentHelper.createElement("strong").addText(courseName);
        Text content2 = DocumentHelper.createText(" course is approaching. Please make sure to complete all necessary tasks and submit your work by the due date.");
        Element deadlineElement = DocumentHelper.createElement("div").
                addAttribute("style", "font-weight: bold; color: #F7C631; margin-top: 20px; font-size: 18px;").
                addText("Deadline: " + deadline + " ICT.");

        return List.of(content1, courseNameElement, content2, deadlineElement);
    }

    public static MailContentUnit generateDocumentDeclinedContent(String message) {
        return MailContentUnit.builder().id("content").innerContent(List.of(DocumentHelper.createText(message))).build();
    }
}
