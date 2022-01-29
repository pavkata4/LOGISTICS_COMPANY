package bg.nbu.logistics.domain.entities;

import static javax.persistence.FetchType.EAGER;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "offices")
public class Office extends BaseEntity {
    @Column(name = "address", unique = true, nullable = false)
    private String address;

    @ManyToMany(targetEntity = User.class, fetch = EAGER)
    @JoinTable(name = "office_employees", joinColumns = @JoinColumn(name = "office_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> employees = new HashSet<>();
}
