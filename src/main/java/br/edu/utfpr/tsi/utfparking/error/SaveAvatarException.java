package br.edu.utfpr.tsi.utfparking.error;

import java.io.IOException;

public class SaveAvatarException extends RuntimeException {
    public SaveAvatarException(String msg, IOException ex) {
        super(msg, ex);
    }
}
