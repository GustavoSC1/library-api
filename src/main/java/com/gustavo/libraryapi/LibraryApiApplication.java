package com.gustavo.libraryapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling // Habilita o Scheduling (agendamento de tarefas) na aplicação
public class LibraryApiApplication {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Scheduled(cron = "0 42 19 1/1 * ?") //http://www.cronmaker.com/
	public void testeAgendamentoTarefas() {
		System.out.println("AGENDAMENTO DE TAREFAS FUNCIONANDO");
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
