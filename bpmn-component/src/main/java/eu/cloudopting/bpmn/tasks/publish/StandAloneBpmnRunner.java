package eu.cloudopting.bpmn.tasks.publish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class StandAloneBpmnRunner {

    public static void main(String[] args) {
        SpringApplication.run(StandAloneBpmnRunner.class, args);
    }

}