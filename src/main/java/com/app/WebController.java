package com.app;

import com.app.exception.ResourceNotFoundException;
import com.app.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.net.UnknownHostException;


@Controller
public final class WebController implements WebMvcConfigurer {

    @Autowired
    Repository repository;

    @Autowired
    UrlService urlService;

    /**
     * Get url form to fill rawValue field and submit
     *
     * @param urlForm
     * @return url form template with rawValue field and submit button.
     */
    @GetMapping("/")
    public String showForm(UrlForm urlForm) {
        return urlService.showForm(urlForm);
    }

    /**
     * Post request with url info
     *
     * @param urlForm
     * @param bindingResult
     * @return results template page with shortenedValue of recently created entity in DB if rawValue doesn't already exist
     * or return template page with shortenedValue of existing entity in DB if entity with the rawValue already exist in DB
     * or return form template with error message if recently filled rawValue is not valid.
     * @throws UnknownHostException
     */
    @PostMapping("/")
    public String checkAndUploadUrl(@Valid UrlForm urlForm, BindingResult bindingResult) throws UnknownHostException {
        return urlService.checkAndUploadUrl(urlForm, bindingResult);
    }

    /**
     * Redirect to rawValue field when send get request with id of url entity
     *
     * @param urlId
     * @return redirect to rawValue or throws ResourceNotFoundException if entity with rawValue doesn't exist in DB.
     */
    @GetMapping("/{id}")
    @ExceptionHandler(ResourceNotFoundException.class)
    public RedirectView getUrlById(@PathVariable(value = "id") Long urlId) {
        return urlService.getUrlById(urlId);
    }
}
