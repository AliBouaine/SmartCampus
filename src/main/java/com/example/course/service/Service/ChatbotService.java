package com.example.course.service.Service;

import com.example.course.service.Entity.Course;
import com.example.course.service.Repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private final CourseRepository courseRepository;

    public ChatbotService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public String getResponse(String message) {
        String lower = message.toLowerCase().trim();

        // Salutations
        if (lower.matches(".*(bonjour|salut|hello|bonsoir|hey).*")) {
            return "Bonjour ! Je suis l'assistant SmartCampus. Posez-moi vos questions sur les cours 😊";
        }

        // Combien de cours
        if (lower.matches(".*(combien|nombre|total).*(cours).*") ||
                lower.matches(".*(cours).*(combien|nombre|total).*")) {
            long count = courseRepository.count();
            return "Il y a actuellement " + count + " cours disponibles sur SmartCampus.";
        }

        // Liste des cours
        if (lower.matches(".*(liste|quels?|c quoi|exist|disponible|voir).*(cours).*") ||
                lower.matches(".*(cours).*(exist|disponible|liste|quels?).*")) {
            List<Course> courses = courseRepository.findAll();
            if (courses.isEmpty()) {
                return "Aucun cours n'est disponible pour le moment.";
            }
            String list = courses.stream()
                    .map(c -> "• " + c.getTitle())
                    .collect(Collectors.joining("\n"));
            return "Voici les cours disponibles :\n" + list;
        }

        // Chercher un cours spécifique
        if (lower.matches(".*(cours|formation).*(sur|de|en).*")) {
            String keyword = lower
                    .replaceAll(".*(cours|formation).*(sur|de|en)\\s*", "")
                    .trim();
            List<Course> found = courseRepository.findByTitleContainingIgnoreCase(keyword);
            if (!found.isEmpty()) {
                return "Oui, nous avons un cours sur \"" + found.get(0).getTitle() + "\" : " + found.get(0).getDescription();
            } else {
                return "Je n'ai pas trouvé de cours sur \"" + keyword + "\". Tapez 'liste des cours' pour voir tout ce qui est disponible.";
            }
        }

        // C'est quoi SmartCampus
        if (lower.matches(".*(c quoi|qu.est.ce|présent|smartcampus|plateforme).*")) {
            return "SmartCampus est une plateforme de formation en ligne proposant des cours dans divers domaines. Tapez 'liste des cours' pour les voir !";
        }

        // Inscription
        if (lower.matches(".*(inscri|register|compte|créer).*")) {
            return "Pour vous inscrire, créez un compte sur notre plateforme SmartCampus.";
        }

        // Certificat
        if (lower.matches(".*(certificat|diplôme|attestation).*")) {
            return "Oui, un certificat est délivré à la fin de chaque cours réussi !";
        }

        // Prix
        if (lower.matches(".*(prix|coût|gratuit|payant|tarif).*")) {
            return "Nos cours sont gratuits pour les étudiants inscrits sur SmartCampus.";
        }

        // Durée
        if (lower.matches(".*(durée|duree|temps|semaine|longtemps).*")) {
            return "La durée varie entre 4 et 12 semaines selon le cours.";
        }

        // Contact
        if (lower.matches(".*(contact|email|mail|joindre).*")) {
            return "Contactez-nous à contact@smartcampus.com";
        }

        // Au revoir
        if (lower.matches(".*(au revoir|bye|bonne continuation|merci).*")) {
            return "Au revoir ! Bonne continuation sur SmartCampus 👋";
        }

        // Réponse par défaut
        return "Je ne comprends pas votre question 🤔 Essayez :\n" +
                "• 'liste des cours'\n" +
                "• 'combien de cours'\n" +
                "• 'cours sur Java'\n" +
                "• 'inscription'\n" +
                "• 'certificat'";
    }
}