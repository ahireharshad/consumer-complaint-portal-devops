package in.ac.consumerportal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
 Optional<Complaint> findByReferenceId(String referenceId);
}
