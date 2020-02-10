package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserRole;
import at.qe.sepm.skeleton.services.UserService;
import at.qe.sepm.skeleton.ui.beans.MessageBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Date;
import java.util.Set;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
/**
 * Controller for the user creation view.
 *
 */
@Component
@Scope("view")
public class UserCreationController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageBean messageBean;



    /**
     * Attribute to cache the currently displayed user
     */
    private User user = new User();

    public boolean setNewUser(){


        this.user.setUsername("");
        this.user.setPassword("");
        this.user.setEmail("");
        this.user.setPhone("");
        this.user.setLastName("");
        this.user.setFirstName("");
        this.user.setEnabled(true);
        this.user.setJobTitle("");



        return true;

    }

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public User getUser() {
    	return user;
    }

    public void setUser(){
        this.user = user;
        setNewUser();
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {

        user.addToRoles();
        user = this.userService.saveUser(user);
    }





}
