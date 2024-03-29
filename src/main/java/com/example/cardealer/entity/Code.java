package com.example.cardealer.entity;

public enum Code {

    STATUS("UP!"),
    SUCCESS("Operacja zakończona sukcesem"),
    PERMIT("Przyznano dostep"),
    A1("Podany uzytkownik o danej nazwie nie istnieje lub nie aktywował konta"),
    A2("Podane dane są nieprawidłowe"),
    A3("Wskazany token jest pusty lub nie ważny"),
    A4("Użytkownik o podanej nazwie juz istnieje"),
    A5("Użytkownik o podanmym mailu juz istnieje"),
    A6("Dealer o podanym NIPie już istnieje");

    public final String label;
    private Code(String label){
        this.label = label;
    }
}
