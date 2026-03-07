package pl.uksford.api;

import org.springframework.boot.SpringApplication;

public class TestUksfordApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(UksfordApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
