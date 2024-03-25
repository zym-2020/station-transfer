package nnu.edu.reception;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("nnu.edu.reception.dao")
@EnableScheduling
public class ReceptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceptionApplication.class, args);
    }

}
