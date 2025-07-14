ALTER TABLE reservation ADD COLUMN id_exemplaire INT;

ALTER TABLE reservation ADD CONSTRAINT fk_reservation_exemplaire
    FOREIGN KEY (id_exemplaire) REFERENCES exemplaire(id) ON DELETE SET NULL;

ALTER TABLE reservation DROP COLUMN id_livre;

