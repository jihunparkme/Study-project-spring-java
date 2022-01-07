package com.youthcon.start;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {

    @GetMapping("/reviews/{id}")
    public Object getById(@PathVariable Long id) {
        return null;
    }
}
