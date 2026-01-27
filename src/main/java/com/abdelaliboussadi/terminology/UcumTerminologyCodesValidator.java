package com.abdelaliboussadi.terminology;

import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumService;
import org.fhir.ucum.UcumException;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UcumTerminologyCodesValidator 
{

    private static final Logger logger = LogManager.getLogger(UcumTerminologyCodesValidator.class);

/**
     * main : arguments :
     *   args[0] = chemin vers ucum-essence.xml
     *   args[1] = code UCUM à vérifier (ex : "mg", "kg/m2", "mm[Hg]", "/min")
     */
    public static void main(String[] args) {
        logger.info("Starting UCUM Terminology Codes Validation execution.");
        if (args.length == 0) {
        args = new String[]{
                PropertiesUtil.getProperties("ucum.essence.path"),
                PropertiesUtil.getProperties("ucum.default.code")
        };
    }
        if (args.length != 2) {
            logger.error("Usage: java -cp <jar:deps> " +
                    "com.example.ucumvalidator.UcumCodeChecker <path-to-ucum-essence.xml> <ucumCode>");
            logger.info("Execution ended with an exception.");
            logger.info("");
            System.exit(1);
        }

        String ucumDefinitionPath = args[0];
        String candidateCode = args[1];

        // Vérification basique du fichier de définition
        try {
            Path defPath = Paths.get(ucumDefinitionPath);
            if (!Files.exists(defPath)) {
                logger.error("Fichier de définition UCUM introuvable : " + ucumDefinitionPath);
                logger.info("Execution ended with an exception.");
                logger.info("");
                System.exit(2);
            }
        } catch (InvalidPathException e) {
            logger.error("Chemin d'accès invalide pour le fichier de définition UCUM : " + ucumDefinitionPath);
            logger.info("Execution ended with an exception.");
            logger.info("");
            System.exit(2);
        }

        try {
            if (ucumDefinitionPath == null || ucumDefinitionPath.trim().isEmpty() || candidateCode == null || candidateCode.trim().isEmpty()) {
                throw new IllegalArgumentException("the argument is missing");
            }

            UcumService ucumSvc = null;
            // Instancie le service UCUM en chargeant le fichier d'essence UCUM.
            // Le constructeur prend ici le chemin (String) vers le fichier.
            // (Si ta version de la lib prend InputStream, adapte en conséquence.)
            ucumSvc = new UcumEssenceService(ucumDefinitionPath);

            logger.info("Chargement de la définition UCUM réussi.");
            logger.info("Analyse du code UCUM : \"" + candidateCode + "\" ...");

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
            logger.info("Le code UCUM est VALIDE.");
            logger.info("Résultat d'analyse (brut) : " + analysisResult);
            logger.info("Execution completed successfully.");
            logger.info("");

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            logger.info("Execution ended with an exception.");
            logger.info("");
            System.exit(1);
        } catch (UcumException e) {
            // La librairie signale les erreurs d'analyse via UcumException.
            // On considère que c'est un code invalide / inconnu.
            logger.error("Le code UCUM est INVALIDE ou INCONNU : " + candidateCode);
            logger.error("Détail de l'erreur : " + e.getMessage());
            logger.info("Execution ended with an exception.");
            logger.info("");
            // (ne pas System.exit ici si tu veux continuer; ici on termine avec code non-nul)
            System.exit(3);
        } catch (Exception e) {
            // Toute autre erreur inattendue (IO, runtime...) -> on l'affiche pour debug.
            logger.error("Erreur inattendue lors de la validation UCUM : " + e.getMessage());
            e.printStackTrace();
            logger.info("Execution ended with an exception.");
            logger.info("");
            System.exit(99);
        }
    }
    
}
