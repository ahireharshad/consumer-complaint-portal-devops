package in.ac.consumerportal;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Complaint {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String referenceId;
 private String consumerName;
 private String email;
 private String category;
 private String subject;
 @Column(length=4000) private String description;
 private String status="SUBMITTED";
 private String reviewerNote="";
 private LocalDateTime createdAt=LocalDateTime.now();
 protected Complaint() {}
 public Complaint(String consumerName,String email,String category,String subject,String description) {
  this.consumerName=consumerName; this.email=email; this.category=category; this.subject=subject; this.description=description; this.status="SUBMITTED";
 }
 @PrePersist void assignReference(){ if(referenceId==null) referenceId="CCP-"+System.nanoTime()%1000000; if(createdAt==null)createdAt=LocalDateTime.now(); }
 public Long getId(){return id;} public String getReferenceId(){return referenceId;}
 public String getConsumerName(){return consumerName;} public String getEmail(){return email;}
 public String getCategory(){return category;} public String getSubject(){return subject;}
 public String getDescription(){return description;} public String getStatus(){return status;}
 public String getReviewerNote(){return reviewerNote;} public LocalDateTime getCreatedAt(){return createdAt;}
 public void decide(String decision,String note){this.status=decision;this.reviewerNote=note==null?"":note;}
}
