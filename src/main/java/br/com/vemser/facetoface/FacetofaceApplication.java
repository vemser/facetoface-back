package br.com.vemser.facetoface;

import br.com.vemser.facetoface.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class FacetofaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacetofaceApplication.class, args);
	}

}
