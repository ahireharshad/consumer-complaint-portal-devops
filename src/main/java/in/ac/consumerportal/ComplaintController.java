package in.ac.consumerportal;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class ComplaintController {
 private final ComplaintRepository repository;
 public ComplaintController(ComplaintRepository repository){this.repository=repository;}
 @GetMapping("/") public String home(Model model){model.addAttribute("complaint",new ComplaintForm());return "index";}
 @PostMapping("/complaints") public String submit(@ModelAttribute ComplaintForm form,Model model,RedirectAttributes redirect){
  String name=clean(form.name), email=clean(form.email), category=clean(form.category), subject=clean(form.subject), description=clean(form.description);
  if(name.isBlank()||email.isBlank()||!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")||category.isBlank()||subject.isBlank()||description.length()<10){
   model.addAttribute("complaint",form); model.addAttribute("error","Enter all required fields, a valid email, and a description of at least 10 characters."); return "index";
  }
  Complaint saved=repository.save(new Complaint(name,email,category,subject,description));
  redirect.addFlashAttribute("submittedReference",saved.getReferenceId()); return "redirect:/";
 }
 @GetMapping("/track") public String track(@RequestParam(required=false) String reference,Model model){
  if(reference!=null&&!reference.isBlank()) model.addAttribute("result",repository.findByReferenceId(reference.trim().toUpperCase(Locale.ROOT)).orElse(null));
  return "track";
 }
 @GetMapping("/review") public String review(Model model){model.addAttribute("complaints",repository.findAll());return "review";}
 @PostMapping("/review/{id}/{decision}") public String decide(@PathVariable Long id,@PathVariable String decision,@RequestParam(defaultValue="") String note,RedirectAttributes redirect){
  if(!decision.equals("APPROVED")&&!decision.equals("REJECTED")) {redirect.addFlashAttribute("error","Invalid decision.");return "redirect:/review";}
  repository.findById(id).ifPresent(c->c.decide(decision,clean(note))); return "redirect:/review";
 }
 private String clean(String value){return value==null?"":value.trim();}
 public static class ComplaintForm {
  private String name="",email="",category="",subject="",description="";
  public String getName(){return name;} public void setName(String v){name=v;}
  public String getEmail(){return email;} public void setEmail(String v){email=v;}
  public String getCategory(){return category;} public void setCategory(String v){category=v;}
  public String getSubject(){return subject;} public void setSubject(String v){subject=v;}
  public String getDescription(){return description;} public void setDescription(String v){description=v;}
 }
}
