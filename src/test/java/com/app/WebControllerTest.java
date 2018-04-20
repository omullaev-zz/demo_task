package com.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkPostRawValueSuccess() throws Exception {
        mockMvc.perform(postRawValueBuilder("http://apple.com"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk());
    }

    @Test
    public void checkPostRawValueReturnsValidResponse() throws Exception {
        String expectedContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<b>http://localhost/1</b>\n" +
                "</body>\n" +
                "</html>\n";
        mockMvc.perform(postRawValueBuilder("http://apple.com"))
                .andExpect(content().string(expectedContent));
    }

    @Test
    public void checkPostDuplicatedRawValuesReturnsAlreadySavedEntityResponse() throws Exception {
        String expectedContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<b>http://localhost/1</b>\n" +
                "</body>\n" +
                "</html>\n";
        mockMvc.perform(postRawValueBuilder("http://apple.com"));
        mockMvc.perform(postRawValueBuilder("http://apple.com"))
                .andExpect(content().string(expectedContent));
    }

    @Test
    public void checkPostUniqueRawValuesReturnsRecentlySavedEntityResponse() throws Exception {
        String expectedContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<b>http://localhost/2</b>\n" +
                "</body>\n" +
                "</html>\n";
        mockMvc.perform(postRawValueBuilder("http://apple.com"));
        mockMvc.perform(postRawValueBuilder("http://vk.com"))
                .andExpect(content().string(expectedContent));
    }

    @Test
    public void checkGetShortedValueSuccess() throws Exception {
        mockMvc.perform(postRawValueBuilder("http://apple.com"));
        mockMvc.perform(getShortedValueBuilder(1))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void checkGetShortedValueReturnsRedirectCode() throws Exception {
        mockMvc.perform(postRawValueBuilder("http://apple.com"));
        mockMvc.perform(getShortedValueBuilder(1))
                .andExpect(status().is(302));
    }

    @Test
    public void checkGetShortedValueReturnsRawValue() throws Exception {
        mockMvc.perform(postRawValueBuilder("http://apple.com"));
        mockMvc.perform(getShortedValueBuilder(1))
                .andExpect(redirectedUrl("http://apple.com"));
    }

    @Test
    public void checkPostDuplicatedRawValuesDoesNotCreatesNewEntity() throws Exception {
        mockMvc.perform(postRawValueBuilder("http://apple.com"));
        mockMvc.perform(postRawValueBuilder("http://apple.com"));
        mockMvc.perform(getShortedValueBuilder(2))
                .andExpect(status().is(404));
    }

    @Test
    public void checkPostNonAbsoluteRawValueHasValidErrorMessage() throws Exception {
        String expectedErrorMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/\" method=\"post\">\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td>Raw Value:</td>\n" +
                "            <td><input type=\"text\" id=\"rawValue\" name=\"rawValue\" value=\"apple.com\"/></td>\n" +
                "            <td>must match absolute-URI mask = scheme &quot;:&quot; hier-part [ &quot;?&quot; query ]</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "                <button type=\"submit\">Submit</button>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>\n";
        mockMvc.perform(postRawValueBuilder("apple.com"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    public void checkPostMoreThanMaximumRawValueHasValidErrorMessage() throws Exception {
        String rawValue = "http://fsfsfsfsfsfsfsfsfscercecsdgsgsfcnsvlnvnseklavnvlnslznvzli" +
                "vnelvnefsfsfsfsfsfsfsfsfscercecsdgsgsfcnsvlnvnseklavnvlnslznvzlivnelvnefsfsfsfsfsfsfsfsfscercecsdg" +
                "sgsfcnsvlnvnseklavnvlnslznvzlivnelvnefsfsfsfsfsfsfsfsfscercecsdgsgsfcnsvlnvnseklavnvlnslznvzlivnel" +
                "vnevnseklavnvlnslznvzlivnelvnefcnsvlnvnseklavnvlnslznvzlivnelvnefsfsfsfsfsfsfsfsfscercecsdgsgsfcns" +
                "vlnvnseklavnvlnslznvzlivnelvnevns.com";
        String expectedErrorMessage = String.format("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/\" method=\"post\">\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td>Raw Value:</td>\n" +
                "            <td><input type=\"text\" id=\"rawValue\" name=\"rawValue\" value=\"%s\"/></td>\n" +
                "            <td>must be less than or equal to 300</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "                <button type=\"submit\">Submit</button>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>\n", rawValue);
        mockMvc.perform(postRawValueBuilder(rawValue))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(content().string(expectedErrorMessage));
    }

    @Test
    public void checkPostNullRawValueHasValidErrorMessage() throws Exception {
        String rawValue = "";
        String expectedErrorMessage = String.format("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/\" method=\"post\">\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td>Raw Value:</td>\n" +
                "            <td><input type=\"text\" id=\"rawValue\" name=\"rawValue\" value=\"%s\"/></td>\n" +
                "            <td>must match absolute-URI mask = scheme &quot;:&quot; hier-part [ &quot;?&quot; query ]</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "                <button type=\"submit\">Submit</button>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>\n", rawValue);
        mockMvc.perform(postRawValueBuilder(rawValue))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(content().string(expectedErrorMessage));
    }

    private MockHttpServletRequestBuilder postRawValueBuilder(String rawValue) {
        return post("/")
                .param("rawValue", rawValue)
                .accept(MediaType.TEXT_HTML);
    }

    private MockHttpServletRequestBuilder getShortedValueBuilder(Integer id) {
        return get(String.format("/%s", id))
                .accept(MediaType.TEXT_HTML);
    }
}
