package dungnm243.SmartServer.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MLModel {
    @Id
    @GeneratedValue
    private long id;
    private String modelName;
    private String algoType;
    private String modelPath;
    private LocalDate createDate;
    private boolean isActive;
    private double acc;
    private double pre;
    private double rec;
    private double f1;
	@ManyToOne
	private Dataset trainDataset;
}
