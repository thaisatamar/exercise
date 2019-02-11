package com.thais.http.response;

import com.thais.rest.response.PartResponse;
import com.thais.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
public class TestSerialize {

    @Test
    public void serDes() {
        PartResponse response = new PartResponse("a", "b", 3);
        String json = JsonUtils.toJ(response);
        String jsonagain = "";
        try {
            PartResponse ragain = JsonUtils.fromJ(json, PartResponse.class);
            jsonagain = JsonUtils.toJ(ragain);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assert (json.equals(jsonagain));
    }
}
