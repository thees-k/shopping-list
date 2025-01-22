package k.thees.shoppinglist.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="_user")
public class User {
	@Id
	@GeneratedValue
	@Column(columnDefinition = "integer", updatable = false, nullable = false)
	private Integer id;

	@NotBlank
	private String loginName;
	private String name;

	@NotBlank
	private String password;

	private LocalDateTime modifiedAt;
}
