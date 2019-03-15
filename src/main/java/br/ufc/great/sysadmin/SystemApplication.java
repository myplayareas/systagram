package br.ufc.great.sysadmin;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.ufc.great.sysadmin.util.Constantes;

/**Classe principal do sistema que carrega os padrões e convenções do spring boot
 * @author armandosoaressousa
 *
 */
@SpringBootApplication
public class SystemApplication {

	public static void main(String[] args) {
		new Constantes();
		String uploadsPath = Constantes.uploadDirectory;
		System.out.println("Diretório de controle de uploads: "+uploadsPath);
		new File(uploadsPath).mkdir();
		
		SpringApplication.run(SystemApplication.class, args);
	}
}
