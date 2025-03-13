package weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import weather.repository.WeatherRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class WeatherControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }


    /**
     * GET
     */

    @Test
    public void testGetUserName() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("John")));
    }


    @Test
    void getOneById() throws Exception {
        this.mockMvc.perform(get("/weather/12").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("index"));
    }

    @Test
    void getOneById55() throws Exception {
        this.mockMvc.perform(get("/weather/19").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("index2", preprocessResponse(prettyPrint())));
    }

    @Test
    void getOneById2() throws Exception {
        this.mockMvc.perform(get("/weather").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    /**
     * Test Bad Request Errors
     */
    @Test
    void errorBadRequest() throws Exception {
        this.mockMvc
                .perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/notes")
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE, "The tag 'http://localhost:8080/nonexist' does not exist"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", is("Bad Request")))
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is(notNullValue())))
                .andDo(document("error-bad-request",
                        responseFields(
                                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                                fieldWithPath("path").description("The path to which the request was made"),
                                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
    }


    /**
     * POST
     */

    @Test
    void getOneById5() throws Exception {
        this.mockMvc.perform(post("/weather/37").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("indexDelete", preprocessResponse(prettyPrint())));

    }
// todo remove pretty print from methods

    @Test
    void notesCreateExample() throws Exception {
        Map<String, String> weather = new HashMap<String, String>();
        weather.put("date", "1985-01-01");
        weather.put("lat", "36.1189");
        weather.put("lon", "-86.6892");
        weather.put("city", "New York");
        weather.put("state", "New York");
        weather.put("temperatures", "17.3,16.8,16.4,16.0,15.6,15.3,15.0,14.9,15.8,18.0,20.2,22.3,23.8,24.9,25.5,25.7,24.9,23.0,21.7,20.8,29.9,29.2,28.6,28.1");


        String tagLocation = this.mockMvc
                .perform(
                        post("/weather").contentType("application/json").content(
                                this.objectMapper.writeValueAsString(weather)))
                .andExpect(status().isCreated()).andReturn().getResponse()
                .getHeader("Location");

        Map<String, Object> note = new HashMap<String, Object>();
        note.put("title", "REST maturity model");
        note.put("body", "https://martinfowler.com/articles/richardsonMaturityModel.html");
        note.put("tags", Arrays.asList(tagLocation));

        this.mockMvc.perform(
                        post("/notes").contentType("application/json").content(
                                this.objectMapper.writeValueAsString(note))).andExpect(
                        status().isCreated())
                .andDo(document("notes-create-example"));
        requestFields(
                fieldWithPath("title").description("The title of the note"),
                fieldWithPath("body").description("The body of the note"),
                fieldWithPath("tags").description("An array of tag resource URIs"))));
    }

    /**
     * PUT
     */

    @Test
    void getOneById4() throws Exception {
        this.mockMvc.perform(put("/weather/37").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("indexDelete", preprocessResponse(prettyPrint())));

    }


    /**
     * DELETE
     */

    @Test
    void getOneById3() throws Exception {
        this.mockMvc.perform(delete("/weather/37").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("getOneById3", preprocessResponse(prettyPrint())));

    }

    @Test
    public void testGetWeather() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/weather"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("California")));
    }

    @Test
    public void indexExample() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andDo(document("index",
                        links(linkWithRel("crud").description("The resource")),
                        responseFields(subsectionWithPath("_links")
                                .description("Links to other resources")),
                        responseHeaders(headerWithName("Content-Type")
                                .description("The Content-Type of the payload"))));
    }

    @Test
    public void testWeather() throws Exception {
        Map<String, Object> weather = new HashMap<>();
        weather.put("title", "SomeWether");
        weather.put("body", "Body");

        this.mockMvc.perform(post("/crud").contentType("application/json")
                        .content(this.objectMapper.writeValueAsString(weather)))
                        .andExpect(status().isCreated())
                        .andDo(document("create-crud-example",
                        requestFields(fieldWithPath("id").description("The id of the input"),
                                fieldWithPath("title").description("The title of the input"),
                                fieldWithPath("body").description("The body of the input"),
                                ))));
    }


    @Test
    public void crudDeleteExample() throws Exception {
        this.mockMvc.perform(delete("/crud/{id}", 10)).andExpect(status().isOk())
                .andDo(document("crud-delete-example",
                        pathParameters(
                                parameterWithName("id").description("The id of the input to delete"))));
    }

    @Test
    void getOneById3() throws Exception {
        this.mockMvc.perform(delete("/weather/37").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("indexDelete", preprocessResponse(prettyPrint())));

    }

    @Test
    void getOneById4() throws Exception {
        this.mockMvc.perform(put("/weather/37").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("indexDelete", preprocessResponse(prettyPrint())));

    }

    @Test
    void getOneById5() throws Exception {
        this.mockMvc.perform(post("/weather/37").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("indexDelete", preprocessResponse(prettyPrint())));

    }
}

}