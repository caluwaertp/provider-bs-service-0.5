package be.bosa.ebox.providerbsservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@Getter
@ComponentScan(basePackages = {
        "be.bosa.dp.common.config",
        "be.bosa.ebox.providerbsservice"
})
public class AppConfig {

    @Value("${uri}")
    private String uri;
    @Value("${environment.name}")
    private String environmentName;
    @Value("${environment.contact}")
    private String environmentContact;
    @Value("${bootstrap.file}")
    private Resource bootstrapFile;
    @Value("${bootstrap.image-file}")
    private Resource bootstrapImageFile;
    @Value("${bootstrap.api-version}")
    private String bootstrapApiVersion;
}
