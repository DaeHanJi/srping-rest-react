package com.han.rest.sample.greglturnquist.payroll;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;

    @Autowired
    public DatabaseLoader(EmployeeRepository employeeRepository, ManagerRepository managerRepository) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Manager dhji1 = this.managerRepository.save(new Manager("dhji1", "1111", "ROLE_MANAGER"));
        Manager dhji2 = this.managerRepository.save(new Manager("dhji2", "2222", "ROLE_MANAGER2"));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "dhji1",
                        "doesn't matter",
                        AuthorityUtils.createAuthorityList("ROLE_MANAGER"))
        );
        this.employeeRepository.save(new Employee("Frodo", "Baggins", "ring bearer", dhji1));
        this.employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar", dhji1));
        this.employeeRepository.save(new Employee("Gandalf", "the Grey", "wizard", dhji1));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "dhji2",
                        "doesn't matter",
                        AuthorityUtils.createAuthorityList("ROLE_MANAGER2"))
        );
        this.employeeRepository.save(new Employee("Samwise", "Gamgee", "gardener", dhji2));
        this.employeeRepository.save(new Employee("Meriadoc", "Brandybuck", "pony rider", dhji2));
        this.employeeRepository.save(new Employee("Peregrin", "Took", "pipe smoker", dhji2));

        SecurityContextHolder.clearContext();
    }
}
