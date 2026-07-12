package com.consultapet.petcare;

import org.springframework.boot.SpringApplication;

public class TestPetcareApplication {

	public static void main(String[] args) {
		SpringApplication.from(PetcareApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
