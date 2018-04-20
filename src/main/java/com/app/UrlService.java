package com.app;

import com.app.repository.Repository;
import com.app.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@Service
class UrlService {

    private final Repository repository;

    @Autowired
    UrlService(Repository repository) {
        this.repository = repository;
    }

    String showForm(UrlForm urlForm) {
        return "form";
    }

    String checkAndUploadUrl(@Valid UrlForm urlForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form";
        }

        UrlForm sameRawValue = repository.findByRawValue(urlForm.getRawValue());

        if (sameRawValue != null) {
            urlForm.setId(sameRawValue.getId());
            urlForm.setRawValue(sameRawValue.getRawValue());
            return "results";
        }

        repository.save(urlForm);
        return "results";
    }

    RedirectView getUrlById(@PathVariable(value = "id") Long urlId) {
        UrlForm urlForm = repository.findById(urlId)
                .orElseThrow(() -> new ResourceNotFoundException("urlForm", "id", urlId));
        return new RedirectView(urlForm.getRawValue(), true);
    }
}
