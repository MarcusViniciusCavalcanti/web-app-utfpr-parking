package br.edu.utfpr.tsi.utfparking.service;

import br.edu.utfpr.tsi.utfparking.config.disk.DiskConfig;
import br.edu.utfpr.tsi.utfparking.models.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileService {

    private final DiskConfig diskConfig;

    public void saveDisk(File file, User user) {
        diskConfig.saveAvatar(file, user);
    }

    public File getFile(Long id) {
        return diskConfig.loadAvatar(id);
    }

}
