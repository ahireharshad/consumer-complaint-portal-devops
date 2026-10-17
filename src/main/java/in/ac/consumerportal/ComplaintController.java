package in.ac.consumerportal;

import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ComplaintController {
    private final ComplaintRepository repository;

    public ComplaintController(ComplaintRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("complaint", new ComplaintForm());
        return "index";
    }

    @PostMapping("/complaints")
    public String submit(@ModelAttribute("complaint") ComplaintForm form, Model model) {
        String name = clean(form.name);
        String email = clean(form.email);
        String category = clean(form.category);
        String subject = clean(form.subject);
        String description = clean(form.description);

        if (name.isBlank() || email.isBlank()
                || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || category.isBlank() || subject.isBlank() || description.length() < 10) {
            model.addAttribute("complaint", form);
            model.addAttribute("error",
                    "Enter all required fields, a valid email, and a description of at least 10 characters.");
            return "index";
        }

        Complaint saved = repository.save(new Complaint(name, email, category, subject, description));
        model.addAttribute("submittedReference", saved.getReferenceId());
        model.addAttribute("complaint", new ComplaintForm());
        return "index";
    }

    @GetMapping("/track")
    public String track(@RequestParam(required = false) String reference, Model model) {
        if (reference != null && !reference.isBlank()) {
            model.addAttribute("result",
                    repository.findByReferenceId(reference.trim().toUpperCase(Locale.ROOT)).orElse(null));
        }
        return "track";
    }

    @GetMapping("/review")
    public String review(Model model) {
        model.addAttribute("complaints", repository.findAll());
        return "review";
    }

    @PostMapping("/review/{id}/{decision}")
    public String decide(@PathVariable Long id, @PathVariable String decision,
                         @RequestParam(defaultValue = "") String note) {
        if (!decision.equals("APPROVED") && !decision.equals("REJECTED")) {
            return "redirect:/review";
        }
        repository.findById(id).ifPresent(c -> {
            if ("SUBMITTED".equals(c.getStatus())) {
                c.decide(decision, clean(note));
                repository.save(c);
            }
        });
        return "redirect:/review";
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    public static class ComplaintForm {
        private String name = "";
        private String email = "";
        private String category = "";
        private String subject = "";
        private String description = "";

        public String getName() { return name; }
        public void setName(String value) { name = value; }
        public String getEmail() { return email; }
        public void setEmail(String value) { email = value; }
        public String getCategory() { return category; }
        public void setCategory(String value) { category = value; }
        public String getSubject() { return subject; }
        public void setSubject(String value) { subject = value; }
        public String getDescription() { return description; }
        public void setDescription(String value) { description = value; }
    }
}
