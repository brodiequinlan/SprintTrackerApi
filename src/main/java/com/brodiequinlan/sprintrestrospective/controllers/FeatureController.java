package com.brodiequinlan.sprintrestrospective.controllers;


import com.brodiequinlan.sprintrestrospective.database.SqlConnection;
import com.brodiequinlan.sprintrestrospective.models.Feature;
import com.brodiequinlan.sprintrestrospective.models.Login;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RequestMapping("/api/features")
@RestController
public class FeatureController {

    @RequestMapping(value = "/{projectid}/{token}/{username}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Feature> all(@PathVariable String projectid, @PathVariable String token, @PathVariable String username) {
        SqlConnection sql = new SqlConnection();
        if (Login.validate_token(token, username)) {
            var feats = sql.getFeatures(projectid);
            sql.close();
            return feats;
        } else return new ArrayList<>();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Feature add(@RequestBody Map<String, Object> payload) {
        SqlConnection sql = new SqlConnection();
        Feature feature = new Feature("-1", payload.get("name").toString(), payload.get("points").toString(), "-1", new ArrayList<>());
        if (Login.validate_token(payload.get("token").toString(), payload.get("username").toString())) {
            var s = sql.addFeature(feature, payload.get("username").toString(), payload.get("project").toString());
            sql.close();
            return s;
        }
        sql.close();
        return feature;
    }

}
