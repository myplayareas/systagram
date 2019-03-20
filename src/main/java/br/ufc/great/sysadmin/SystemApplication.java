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
		String mainPath = "TBD";
		String uploadsPath = Constantes.uploadDirectory;
		String picturesPath = Constantes.picturesDirectory;
		String banco = "TBD";
		String portaBanco = "TBD";
		String ipServidor = "TBD";
		String ipServidorBD = "TBD";
		String portaSA = "TBD";
		
		System.out.println("Diretório principal: " + mainPath);
		System.out.println("Diretório de controle de uploads: "+uploadsPath);
		System.out.println("Diretório de fotos: " + picturesPath);
		System.out.println("Banco da aplicação: " + banco);
		System.out.println("Porta do banco: " + portaBanco);
		System.out.println("Porta do Servidor de Aplicação: " + portaSA);
		System.out.println("Ip do Servidor de Aplicação: " + ipServidor);
		System.out.println("Ip do Servidor de Banco de Dados: " + ipServidorBD);
		
		new File(uploadsPath).mkdir();
		
		SpringApplication.run(SystemApplication.class, args);
	}
}
