package br.edu.utfpr.tsi.utfparking.config.disk;

import br.edu.utfpr.tsi.utfparking.UtfparkingApplication;
import br.edu.utfpr.tsi.utfparking.error.SaveAvatarException;
import br.edu.utfpr.tsi.utfparking.utils.ToolsTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = UtfparkingApplication.class)
@ActiveProfiles("test")
public class DiskConfigTest {

    @Autowired
    private DiskConfig diskConfig;

    @Test
    public void should_saved_avatar_user_by_file() throws IOException {
        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver.setId(1L);

        var file = new ClassPathResource("images/user.jpg", this.getClass().getClassLoader()).getFile();
        diskConfig.saveAvatar(file, driver);
        var exists = Files.exists(file.toPath());

        assertTrue(exists);
    }

    @Test
    public void should_saved_avatar_user_by_multipart() throws IOException {
        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver.setId(1L);

        var file = new ClassPathResource("images/user.jpg", this.getClass().getClassLoader()).getFile();
        var mockMultipartFile = new MockMultipartFile("image.png", file.getPath().getBytes());

        diskConfig.saveAvatar(mockMultipartFile, driver);

        var exists = Files.exists(file.toPath());

        assertTrue(exists);
    }

    @Test
    public void should_return_avatar_file_by_user() throws IOException {
        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");
        driver.setId(1L);

        var file = new ClassPathResource("images/user.jpg", this.getClass().getClassLoader()).getFile();
        diskConfig.saveAvatar(file, driver);
        var exists = Files.exists(file.toPath());

        assertTrue(exists);

        var result = diskConfig.loadAvatar(1L);

        assertThat(result.getName(), Matchers.containsString("1.png"));
    }

    @Test(expected = SaveAvatarException.class)
    public void should_return_error_when_save_avatar_by_file() {
        var file = new File("");
        var driver = ToolsTest.createUser(ToolsTest.createUserWithAccessCardUser(), "Driver");

        diskConfig.saveAvatar(file, driver);
    }
}
