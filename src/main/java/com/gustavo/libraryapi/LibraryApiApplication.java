package com.gustavo.libraryapi;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.gustavo.libraryapi.service.EmailService;

@SpringBootApplication
@EnableScheduling // Habilita o Scheduling (agendamento de tarefas) na aplicação
public class LibraryApiApplication {
	/*
	@Autowired
	private EmailService emailService;
	*/
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	/*
	//Testando serviço de emails
	// Toda vez que um @Bean de um CommandLineRunner é criado, ele será executado assim que ele "subir" a aplicação
	@Bean
	public CommandLineRunner runner() {
		return args -> {
			List<String> emails = Arrays.asList("library-api-876ade@inbox.mailtrap.io");
			emailService.sendMails("Testando serviço de emails", emails);
			System.out.println("EMAILS ENVIADOS");
		};
	}
	 */
	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
