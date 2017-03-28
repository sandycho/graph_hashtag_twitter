package graph;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


/*
 * Created by SCO on 22/03/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GraphControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void getTrackData() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getTrackData"))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("{\"counter\":0}")));
    }

    @Test
    public void track() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/track").accept(MediaType.TEXT_PLAIN).content("sometext").param("text", "textToTrack"))
        //.andExpect(status().isOk())
        .andExpect(content().string(equalTo("")));
    }
}

