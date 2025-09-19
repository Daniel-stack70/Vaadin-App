package dev.daniel;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import dev.daniel.utility.DataSeeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme("default")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) throws Exception {
        var context = SpringApplication.run(Application.class, args);

        context.getBean(DataSeeder.class).run();

    }

}
