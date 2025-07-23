package uz.idimzo.beck.entity;

public enum FieldType {
    MONEY,          // Pul miqdori uchun
    INTEGER,        // Butun son
    DROPDOWN,       // Tanlov ro'yxati
    CHECKBOX,       // Ha/Yo'q
    DATE,           // Sana
    DATE_SEPARATE,  // Alohida yil, oy, kun
    TEXT,           // Katta matn
    PHONE,          // Telefon raqam
    DOCUMENT_ID,    // Pasport/JSHSHIR
    STRING,         // Oddiy matn
    FILE            // Fayl
}