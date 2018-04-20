package com.app.repository;

import com.app.UrlForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {

    @Autowired
    Repository repository;

    @Before
    public void setUp() throws Exception {
        List<String> urls = Stream.of("http://apple.com", "http://google.com", "http://yahoo.com").collect(Collectors.toList());
        for (String url : urls) {
            UrlForm urlForm = new UrlForm();
            urlForm.setRawValue(url);
            repository.save(urlForm);
        }
    }

    @Test
    public void findByRawValue() throws Exception {
        String url = "http://google.com";
        UrlForm expected = new UrlForm();
        expected.setId(2L);
        expected.setRawValue(url);
        UrlForm found = repository.findByRawValue(url);
        assertThat(found.getId(), equalTo(expected.getId()));
        assertThat(found.getRawValue(), equalTo(expected.getRawValue()));
    }
}
