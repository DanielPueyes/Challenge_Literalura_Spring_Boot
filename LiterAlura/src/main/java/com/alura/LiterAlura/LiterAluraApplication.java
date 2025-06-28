package com.alura.LiterAlura;


import com.alura.LiterAlura.principal.Principal;
import com.alura.LiterAlura.repository.AutorRepository;
import com.alura.LiterAlura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository repository;
	@Autowired
	private AutorRepository repositoryAutor;
	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository, repositoryAutor);
		principal.muestraelMenu();;
	}
}
