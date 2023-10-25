package dungnm243.SmartServer.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {
    @Id
    @GeneratedValue
    private long id;
    private String datasetName;
    private String algoType;
    private LocalDate createDate;
    private String datasetPath;
}
