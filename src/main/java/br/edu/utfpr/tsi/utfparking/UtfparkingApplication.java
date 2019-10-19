package br.edu.utfpr.tsi.utfparking;

import br.edu.utfpr.tsi.utfparking.config.properties.DiskProperties;
import br.edu.utfpr.tsi.utfparking.data.AccessCardRepository;
import br.edu.utfpr.tsi.utfparking.data.CarRepository;
import br.edu.utfpr.tsi.utfparking.data.RoleRepository;
import br.edu.utfpr.tsi.utfparking.data.UserRepository;
import br.edu.utfpr.tsi.utfparking.models.entities.AccessCard;
import br.edu.utfpr.tsi.utfparking.models.entities.Car;
import br.edu.utfpr.tsi.utfparking.models.entities.Role;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
import br.edu.utfpr.tsi.utfparking.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableConfigurationProperties(DiskProperties.class)
public class UtfparkingApplication implements CommandLineRunner {

    // TODO remover
    @Autowired
    private AccessCardRepository accessCardRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private FileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(UtfparkingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        initialDB();
//        saveAvatar();
    }

    public void initialDB() throws IOException {
        carRepository.deleteAll();
        accessCardRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();

        var roleUser = roleRepository.save(Role.builder()
                .description("Perfil usuário")
                .name("USER")
                .build());
        var roleAdmin = roleRepository.save(Role.builder()
                .description("Perfil Administrador")
                .name("ADMIN")
                .build());

        var roleOperator = roleRepository.save(Role.builder()
                .description("Perfil Operador")
                .name("OPERATOR")
                .build());

        var bCryptPasswordEncoder = new BCryptPasswordEncoder();
        var admin = AccessCard.builder()
                .password(bCryptPasswordEncoder.encode("1234567"))
                .username("vinicius_admin")
                .roles(List.of(roleAdmin, roleUser))
                .build();

        var user = AccessCard.builder()
                .password(bCryptPasswordEncoder.encode("1234567"))
                .username("vinicius_user")
                .roles(List.of(roleUser))
                .build();

        var operator = AccessCard.builder()
                .password(bCryptPasswordEncoder.encode("1234567"))
                .username("vinicius_operator")
                .roles(List.of(roleOperator, roleUser))
                .build();

        var random = new Random();
        var servidor = User.builder()
                .accessCard(admin)
                .name("ViniciusAdmin")
                .type("Servidor")
                .numberAccess(random.nextInt())
                .authorisedAccess(true)
                .build();


        var operador = User.builder()
                .accessCard(operator)
                .name("ViniciusOperador")
                .type("Operador")
                .numberAccess(random.nextInt())
                .authorisedAccess(true)
                .build();

        var aluno1 = User.builder()
                .accessCard(user)
                .name("Vinicius")
                .type("Aluno")
                .authorisedAccess(true)
                .numberAccess(random.nextInt())
                .build();
        var car1 = Car.builder()
                .model("Gol")
                .plate("AHH0999")
                .user(aluno1)
                .build();

        var plates = Files.walk(Paths.get("/Users/vinicius/development/person/tcc-utfparking/web-app-utfpr-parking/plates"));

        var accessCard = new ArrayList<>(List.of(admin, operator, user));
        var users = new ArrayList<>(List.of(servidor, operador, aluno1));
        var cars = new ArrayList<>(List.of(car1));

        plates.filter(Files::isRegularFile)
                .forEach(file -> {
                    var replace = file.getFileName().toString()
                            .replace("-", "")
                            .replace(".jpg", "")
                            .replace(".png", "")
                            .replace(".JPG", "");
                    var accessCarddrive = AccessCard.builder()
                            .password(bCryptPasswordEncoder.encode("1234567"))
                            .username("vinicius_" + random.nextInt())
                            .roles(List.of(roleUser))
                            .build();

                    accessCard.add(accessCarddrive);
                    var userDriver = User.builder()
                            .accessCard(accessCarddrive)
                            .name(replace)
                            .type("Aluno")
                            .authorisedAccess(random.nextBoolean())
                            .numberAccess(random.nextInt())
                            .build();
                    users.add(userDriver);

                    var carDriver = Car.builder()
                            .model("Gol")
                            .plate(replace.toLowerCase())
                            .user(userDriver)
                            .lastAccess(LocalDateTime.now().plusDays(random.nextInt(100)))
                            .build();
                    cars.add(carDriver);
                });

        carRepository.saveAll(cars);
        userRepository.saveAll(users);
        accessCardRepository.saveAll(accessCard);
    }

    private void saveAvatar() {
        userRepository.findAll().forEach(s -> {
            File file = null;
            try {
                var collect = Files.walk(Paths.get("/Users/vinicius/development/person/tcc/web-app-utfpr-parking/files"))
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());
                file = collect.get(new Random().nextInt(collect.size() -1)).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileService.saveDisk(file, s);
        });

    }
}
