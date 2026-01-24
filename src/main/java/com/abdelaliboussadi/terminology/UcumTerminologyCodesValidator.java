package com.abdelaliboussadi.terminology;

import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumService;
import org.fhir.ucum.UcumException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class UcumTerminologyCodesValidator 
{

/**
     * main : arguments :
     *   args[0] = chemin vers ucum-essence.xml
     *   args[1] = code UCUM à vérifier (ex : "mg", "kg/m2", "mm[Hg]", "/min")
     */
    public static void main(String[] args) {
        if (args.length == 0) {
        args = new String[]{
                "C:\\dev\\git\\ucum_client\\ucum-client\\src\\resources\\ucum-essence.xml",
                "ml"
        };
    }
        if (args.length != 2) {
            System.err.println("Usage: java -cp <jar:deps> " +
                    "com.example.ucumvalidator.UcumCodeChecker <path-to-ucum-essence.xml> <ucumCode>");
            System.exit(1);
        }

        String ucumDefinitionPath = args[0];
        String candidateCode = args[1];

        // Vérification basique du fichier de définition
        Path defPath = Paths.get(ucumDefinitionPath);
        if (!Files.exists(defPath)) {
            System.err.println("Fichier de définition UCUM introuvable : " + ucumDefinitionPath);
            System.exit(2);
        }

        UcumService ucumSvc = null;
        try {
            // Instancie le service UCUM en chargeant le fichier d'essence UCUM.
            // Le constructeur prend ici le chemin (String) vers le fichier.
            // (Si ta version de la lib prend InputStream, adapte en conséquence.)
            ucumSvc = new UcumEssenceService(ucumDefinitionPath);

            System.out.println("Chargement de la définition UCUM réussi.");
            System.out.println("Analyse du code UCUM : \"" + candidateCode + "\" ...");

            /*
             * La méthode analyse(...) tente de parser / analyser l'unité.
             * - Si l'analyse réussit, on considère que l'unité est connue / valide.
             * - Si l'unité est inconnue / mal formée, la bibliothèque lance UcumException.
             *
             * Le type de retour exact (String/objet) peut contenir des détails ; ici
             * on affiche le résultat texte si l'analyse renvoie quelque chose d'utile.
             */
            String analysisResult = ucumSvc.analyse(candidateCode);
            // Si on arrive ici, l'analyse s'est déroulée sans exception -> unité valide
            System.out.println("Le code UCUM est VALIDE.");
            System.out.println("Résultat d'analyse (brut) : " + analysisResult);

        } catch (UcumException e) {
            // La librairie signale les erreurs d'analyse via UcumException.
            // On considère que c'est un code invalide / inconnu.
            System.out.println("Le code UCUM est INVALIDE ou INCONNU : " + candidateCode);
            System.out.println("Détail de l'erreur : " + e.getMessage());
            // (ne pas System.exit ici si tu veux continuer; ici on termine avec code non-nul)
            System.exit(3);
        } catch (Exception e) {
            // Toute autre erreur inattendue (IO, runtime...) -> on l'affiche pour debug.
            System.err.println("Erreur inattendue lors de la validation UCUM : " + e.getMessage());
            e.printStackTrace();
            System.exit(99);
        }
    }
    
}
