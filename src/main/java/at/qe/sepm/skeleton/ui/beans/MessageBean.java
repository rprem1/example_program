package at.qe.sepm.skeleton.ui.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Message Bean to show informations or errors to the user
 */
@ManagedBean
public class MessageBean implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Show success message
     *
     * @param summary Short summary.
     * @param text    Text to be displayed.
     */
    public void alertInformation(String summary, String info) {
        logger.info("Message Success: " + info);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(summary , new FacesMessage(FacesMessage.SEVERITY_INFO, summary + ": " + info, null));
    }

    /**
     * Show error message
     *
     * @param summary Short summary.
     * @param text    Text to be displayed.
     */
    public void alertError(String summary, String info) {
        logger.info("Message Error: " + info);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(summary, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary + ": " + info, null));
    }

}
