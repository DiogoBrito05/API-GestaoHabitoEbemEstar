package API_GestaHabitosEbemEstar.models;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data // Gera getters, setters, equals, hashCode, toString automaticamente
@Table(name = "usuarios")
public class UsersModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer idUser;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NOME")
    private String name;

    @Column(name = "SENHA")
    private String password;

    @Column(name = "DATA_CRIACAO")
    private String dateCreation;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "USUARIO_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Roles> roles = new HashSet<>();

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    } 

    public String getPassword () { 
        return this.password; 
    } 

    @Override 
    @JsonIgnore
    public String getUsername () { 
        return this.email; 
    } 

    @Override
    @JsonIgnore 
    public  boolean  isAccountNonExpired () { 
        return  true ; 
    } 

    @Override
    @JsonIgnore 
    public  boolean  isAccountNonLocked () { 
        return  true ; 
    } 

    @Override
    @JsonIgnore 
    public  boolean  isCredentialsNonExpired () { 
        return  true ; 
    } 

    @Override
    @JsonIgnore 
    public  boolean  isEnabled () { 
        return  true ; 
    }

}
