package k.thees.shoppinglist.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
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
public class Item {

	@Id
	@GeneratedValue
	@Column(columnDefinition = "integer", updatable = false, nullable = false)
	private Integer id;

	@Version
	private Integer version;

	@NotBlank
	private String text;

	private String modifiedBy;

	private LocalDateTime modifiedAt;
	
	private boolean done;

	public boolean equalsAnotherItem(Item otherItem) {
		return Objects.equals(text, otherItem.text)
				&& Objects.equals(modifiedBy, otherItem.modifiedBy)
				&& Objects.equals(modifiedAt, otherItem.modifiedAt)
				&& Objects.equals(done, otherItem.done);
	}
}
