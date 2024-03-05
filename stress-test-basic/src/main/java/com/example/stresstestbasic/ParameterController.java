package com.example.stresstestbasic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParameterController {

    private static final Logger logger = LoggerFactory.getLogger(ParameterController.class);

    @RequestMapping(value = "/login-with-id-password", method = RequestMethod.POST)
    public String loginWithIdPassword(@RequestBody IdAndPassword idAndPassword) {
        logger.info("{} / {}", idAndPassword.getId(), idAndPassword.getPassword());

        return "Login Success";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query) {
        logger.info("query: {}", query);

        return "Query Success";
    }

    static class IdAndPassword {
        private String id;
        private String password;

        public String getId() {
            return id;
        }

        public String getPassword() {
            return password;
        }
    }
}
