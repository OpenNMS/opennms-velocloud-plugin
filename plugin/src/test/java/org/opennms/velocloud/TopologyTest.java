package org.opennms.velocloud;

import org.json.JSONException;
import org.junit.Test;
import org.opennms.velocloud.model.Topology;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TopologyTest {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Verifies that the object is serialized to JSON as expected.
     */
    @Test
    public void canSerializeToJson() throws JsonProcessingException, JSONException {
    }
}
