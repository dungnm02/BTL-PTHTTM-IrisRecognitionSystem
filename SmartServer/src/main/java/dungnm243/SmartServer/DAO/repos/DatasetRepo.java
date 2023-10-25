package dungnm243.SmartServer.DAO.repos;

import dungnm243.SmartServer.models.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetRepo extends JpaRepository<Dataset, Long> {
    List<Dataset> findAllByAlgoType(String algoType);
}
