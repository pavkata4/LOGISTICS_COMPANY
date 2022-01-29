package bg.nbu.logistics.domain.entities;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {
    private static final long serialVersionUID = 7373030764827481736L;

    @Column(name = "authority", nullable = false)
    private String authority;
}