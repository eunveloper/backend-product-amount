package antigravity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@Slf4j
public class H2Runner implements ApplicationRunner {

    @Autowired
    DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            log.info("======================================== database info ========================================");
            log.info("connection : {}", connection);
            String url = connection.getMetaData().getURL();
            log.info("url        : {}", url);
            String user = connection.getMetaData().getUserName();
            log.info("user       : {}", user);
            log.info("===============================================================================================");
        }
    }
}