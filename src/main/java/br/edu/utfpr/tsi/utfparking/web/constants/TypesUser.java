package br.edu.utfpr.tsi.utfparking.web.constants;

public enum TypesUser {

    STUDENTS("Aluno"),
    SERVICE("Servidor");

    private String description;

    TypesUser(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
