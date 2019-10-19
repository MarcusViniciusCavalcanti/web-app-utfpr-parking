package br.edu.utfpr.tsi.utfparking.config.disk;

import br.edu.utfpr.tsi.utfparking.models.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DiskConfig {

    @Value("${file.root}")
    private String directoryRoot;

    @Value("${file.avatar}")
    private String directoryAvatar;

    public void saveAvatar(File file, User user) {
        var filePath = new File(directoryRoot + directoryAvatar + "/" + user.getId() + ".png");
        try {
            var read = ImageIO.read(file);
            ImageIO.write(read, "png", filePath);
        } catch (IOException e) {
            throw new RuntimeException("Problemas na tentativa de salvar arquivo.", e);
        }
    }

    public void saveAvatar(MultipartFile file, User user) {
        var name = user.getName();
        var id = user.getId();

        var path = Paths.get(this.directoryRoot, directoryAvatar);
        var filePath = path.resolve(name + "-" + id);

        try {
            Files.createDirectories(path);
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Problemas na tentativa de salvar arquivo.", e);
        }
    }


    public File loadAvatar(Long id) {
        return new File(directoryRoot + directoryAvatar + "/" + id + ".png");
    }
}
