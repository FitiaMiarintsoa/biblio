package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.Notification;
import com.itu.bibliotheque.model.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByAdherentAndEstLuFalse(Adherent adherent);
}
