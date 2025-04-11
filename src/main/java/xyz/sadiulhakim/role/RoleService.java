package xyz.sadiulhakim.role;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RoleService {

	private final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public void save(Role role) {

		LOGGER.info("Saving role {}", role.getName());
		roleRepository.save(role);
		LOGGER.info("Done saving role {}", role.getName());
	}

	public Role findByName(String name) {
		LOGGER.info("Finding role by name {}", name);
		return roleRepository.findByName(name)
				.orElseThrow(() -> new EntityNotFoundException("Role is not found with name " + name));
	}

	public Role findById(long id) {
		LOGGER.info("Finding role by id {}", id);
		return roleRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Role is not found with id " + id));
	}

	@RateLimiter(name = "defaultRateLimiter")
	public List<Role> findAll(int pageNumber,int pageSize) {
		return roleRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
	}

	public void delete(long id) {
		LOGGER.info("Deleting role {}", id);
		roleRepository.deleteById(id);
		LOGGER.info("Done deleting role {}", id);
	}
}
