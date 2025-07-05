package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.Utilisateur;

public interface LoginService {
    Utilisateur authenticate(String username, String password);
}
