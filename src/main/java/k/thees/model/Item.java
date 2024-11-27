package k.thees.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	@Id
	@GeneratedValue
	@Column(columnDefinition = "integer", updatable = false, nullable = false)
	private Integer id;

	@Version
	private Integer version;

	private String text;

	private String modifiedBy;

	private LocalDateTime modifiedAt;
}
