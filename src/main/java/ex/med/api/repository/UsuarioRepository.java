package ex.med.api.repository;

import ex.med.api.domain.UsuarioDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<UsuarioDomain, Long> {
    UserDetails findByLogin(String login);
}
