package com.app;

import com.app.repository.Repository;
import com.app.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public final class UrlServiceTest {

    @TestConfiguration
    static class UrlServiceTestContextConfiguration {

        @MockBean
        Repository repository;

        @Bean
        public UrlService urlService() {
            return new UrlService(repository);
        }
    }

    @Autowired
    UrlService urlService;

    @Autowired
    Repository repository;

    @Before
    public void setUp() {
        UrlForm urlForm = new UrlForm();
        urlForm.setId(1L);
        urlForm.setRawValue("http://apple.com");

        when(repository.findByRawValue(urlForm.getRawValue()))
                .thenReturn(urlForm);
        when(repository.findById(urlForm.getId()))
                .thenReturn(Optional.ofNullable(urlForm));
        when(repository.save(urlForm))
                .thenReturn(urlForm);
    }

    @Test
    public void showForm() throws Exception {
        UrlForm urlForm = new UrlForm();
        String expected = "form";
        assertThat(urlService.showForm(urlForm), equalTo(expected));
    }

    @Test
    public void checkAndUploadUrlBindingHasErrors() throws Exception {
        UrlForm urlForm = new UrlForm();
        urlForm.setRawValue("http://apple.com");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String expected = "form";
        assertThat(urlService.checkAndUploadUrl(urlForm, bindingResult), equalTo(expected));
    }

    @Test
    public void checkAndUploadUrlDuplicateFound() throws Exception {
        UrlForm urlForm = new UrlForm();
        urlForm.setRawValue("http://apple.com");
        repository.save(urlForm);
        BindingResult bindingResult = mock(BindingResult.class);
        String expected = "results";
        assertThat(urlService.checkAndUploadUrl(urlForm, bindingResult), equalTo(expected));
    }

    @Test
    public void checkAndUploadUrlSuccess() throws Exception {
        UrlForm urlForm = new UrlForm();
        urlForm.setRawValue("http://apple.com");
        BindingResult bindingResult = mock(BindingResult.class);
        String expected = "results";
        assertThat(urlService.checkAndUploadUrl(urlForm, bindingResult), equalTo(expected));
    }

    @Test
    public void getUrlByIdSuccess() throws Exception {
        UrlForm urlForm = new UrlForm();
        urlForm.setId(1L);
        urlForm.setRawValue("http://apple.com");
        repository.save(urlForm);
        RedirectView expected = new RedirectView(urlForm.getRawValue(), true);
        RedirectView actual = urlService.getUrlById(urlForm.getId());
        assertThat(actual.getUrl(), equalTo(expected.getUrl()));
        assertThat(actual.getContentType(), equalTo(expected.getContentType()));
    }

    @Test
    public void getUrlByIdNotFound() throws Exception {
        UrlForm urlForm = new UrlForm();
        urlForm.setId(2L);
        urlForm.setRawValue("http://google.com");
        String resourceName = "urlForm";
        String fieldName = "id";
        Long fieldValue = 2L;
        String message = "urlForm not found with id : '2'";
        try {
            urlService.getUrlById(urlForm.getId());
        } catch (ResourceNotFoundException ex) {
            assertThat(ex.getResourceName(), equalTo(resourceName));
            assertThat(ex.getFieldName(), equalTo(fieldName));
            assertThat(ex.getFieldValue(), equalTo(fieldValue));
            assertThat(ex.getMessage(), equalTo(message));
        }

    }
}
