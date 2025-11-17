package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.JobPosition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/job-positions")
public class JobPositionController {

    @GetMapping
    public List<JobPosition> listAll() {
        List<JobPosition> list = new ArrayList<>();
        for (JobPosition p : JobPosition.values()) {
            list.add(p);
        }
        return list;
    }

    @GetMapping("/{name}")
    public JobPosition findByName(@PathVariable String name) {
        if (name == null) {
            return null;
        }
        for (JobPosition p : JobPosition.values()) {
            if (name.equalsIgnoreCase(p.name())) {
                return p;
            }
        }
        return null;
    }
}
