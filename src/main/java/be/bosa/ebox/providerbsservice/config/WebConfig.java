package be.bosa.ebox.providerbsservice.config;

import be.bosa.dp.common.config.CentralWebConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebConfig extends CentralWebConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        addReqRespExchangeLoggingInterceptor(registry);
    }
}
