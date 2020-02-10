package at.qe.sepm.skeleton.services;

import at.qe.sepm.skeleton.model.Flight;
import at.qe.sepm.skeleton.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Service to send e-mails to users
 */
@Component
public class EmailService{

    public JavaMailSender emailSender;

    /**
     * Method to send a mail to a user
     * @param to the target mail-address
     * @param subject the subject of the mail
     * @param text to text of the mail
     */
    public void sendMessage(String to, String subject, String text){
        if(emailSender==null){
            emailSender = getEmailSender();
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * Sends a Mail to the whole Crew of a flight
     * @param pilotsExecuting the pilots of the plane
     * @param boardCrewExecuting the crew of the plane
     * @param flight the flight
     */
    public void sendMailToCrew(Set<User> pilotsExecuting, Set<User> boardCrewExecuting, Flight flight){
        String subject = "You got added to a new flight";
        SendToSet(pilotsExecuting, flight, subject);
        SendToSet(boardCrewExecuting, flight, subject);
    }

    /**
     * Iterates through the set and sends a mail to each one, if he/she has a email-address
     * @param users
     * @param flight
     * @param subject
     */
    private void SendToSet(Set<User> users, Flight flight, String subject) {
        if(users!=null) {
            for (User crew : users) {
                if (crew.getEmail() != null) {
                    String text = "Dear " + crew.getLastName() + ",\nyou got added to the flight " + flight.getFlightId() + " from "
                            + flight.getIataFrom() + " to " + flight.getIataTo() + " at " + flight.getDepartureTime()
                            + "\n for more information check your schedule online";
                    sendMessage(crew.getEmail(), subject, text);
                }
            }
        }
    }

    /**
     * Method to get a Instance of EmailSender
     * @return returns a JavaMailSender
     */
    @Bean
    public JavaMailSender getEmailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("softwarearch20@gmail.com");
        mailSender.setPassword("software123MAIL");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


}
