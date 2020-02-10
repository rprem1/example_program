package at.qe.sepm.skeleton.services;

import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.AuditLog;
import at.qe.sepm.skeleton.model.Holiday;
import at.qe.sepm.skeleton.repositories.AuditLogRepository;
import at.qe.sepm.skeleton.repositories.UserRepository;
import at.qe.sepm.skeleton.ui.beans.MessageBean;

import java.util.Collection;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("application")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;
    
	@Autowired
	private MessageBean messageBean;
    
    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<User> getAllPilots(){return userRepository.findPilots();}
    
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<User> getBoardpersonal(){return userRepository.findBoardcrew();}

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username or hasAuthority('MANAGER')")
    public User loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createDate}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public User saveUser(User user) {
        AuditLog auditlog = new AuditLog();
        auditlog.setDate(new Date());
    	
        if (user.isNew()) {
            user.setCreateDate(new Date());
            user.setCreateUser(getAuthenticatedUser());
            auditlog.setMessage("User " + user.getUsername() + " was created.");
            auditLogRepository.save(auditlog);
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
            auditlog.setMessage("User " + user.getUsername() + " was updated.");
            auditLogRepository.save(auditlog);
        }
        EmailService emailService = new EmailService();
        if(user.getEmail()!=null){
            String text = "Dear "+user.getLastName()+",\na new account at the airport was created for you for more information check online";
            emailService.sendMessage(user.getEmail(),"A new account was created for you",text);
        }
        if(user.getUpdateDate() == null)
        	messageBean.alertInformation("Info", "User was created!");
        else
        	messageBean.alertInformation("Info", "User was updated!");
        return userRepository.save(user);
    }


    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public void deleteUser(User user) {
    	AuditLog auditlog = new AuditLog();
        auditlog.setDate(new Date());
        auditlog.setMessage("User " + user.getUsername() + " was deleted.");
        auditLogRepository.save(auditlog);
        
    	messageBean.alertInformation("Success", "Deleted user " + user.getUsername() + " !");

        
        userRepository.delete(user);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

}
