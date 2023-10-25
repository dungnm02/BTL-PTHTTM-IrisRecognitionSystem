package dungnm243.SmartServer.DAO.repos;

import dungnm243.SmartServer.models.MLModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MLModelRepo extends JpaRepository<MLModel, Long> {
    List<MLModel> findAllByAlgoType(String algoType);
}
