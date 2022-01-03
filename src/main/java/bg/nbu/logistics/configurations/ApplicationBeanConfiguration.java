package bg.nbu.logistics.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import bg.nbu.logistics.commons.utils.Mapper;

@Configuration
public class ApplicationBeanConfiguration {
    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Mapper mapper(ModelMapper modelMapper) {
        return new Mapper(modelMapper);
    }
    
    @Bean
    public PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}