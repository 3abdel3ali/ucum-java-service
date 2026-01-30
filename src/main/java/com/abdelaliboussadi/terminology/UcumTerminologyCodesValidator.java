/*
License

This project is licensed under the Apache License 2.0 with an additional attribution clause.

Any use of this code must provide explicit attribution to:
- The original author: Abdelali Boussadi, PhD
- The original repository: https://github.com/3abdel3ali/ucum-java-service

See the LICENSE file for full details.
*/
package com.abdelaliboussadi.terminology;

import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.Decimal;

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
     * Validation mode:
     *   args[0] = chemin vers ucum-essence.xml
     *   args[1] = code UCUM à vérifier (ex : "mg", "kg/m2", "mm[Hg]", "/min")
     * Conversion mode:
     *   args[0] = chemin vers ucum-essence.xml
     *   args[1] = value to convert (e.g., "10")
     *   args[2] = source unit (e.g., "kg")
     *   args[3] = destination unit (e.g., "[lb_av]")
     */
    public static void main(String[] args) {
        logger.info("Starting UCUM Terminology Codes Validation execution.");
        boolean codeValidation = Boolean.parseBoolean(PropertiesUtil.getProperties("codeValidation"));
        boolean codeConversion = Boolean.parseBoolean(PropertiesUtil.getProperties("codeConversion"));

        String ucumDefinitionPath = null;
        String candidateCode = null;
        String conversionValue = null;
        String sourceUnit = null;
        String destinationUnit = null;

        if (codeConversion && args.length != 0 && args.length != 4 && args.length != 2) {
            logger.error("Usage for conversion: java -cp <jar:deps> " +
                "com.example.ucumvalidator.UcumCodeChecker <path-to-ucum-essence.xml> <value> <sourceUnit> <destUnit>");
            logger.info("Execution ended with an exception.");
            logger.info("");
            System.exit(1);
        }

        if (codeValidation && args.length == 0) {
            ucumDefinitionPath = PropertiesUtil.getProperties("ucum.essence.path");
            candidateCode = PropertiesUtil.getProperties("ucum.default.code");
        } else if (codeValidation && args.length == 2) {
            ucumDefinitionPath = args[0];
            candidateCode = args[1];
        }

        if (codeConversion) {
            if (args.length == 4) {
                ucumDefinitionPath = args[0];
                conversionValue = args[1];
                sourceUnit = args[2];
                destinationUnit = args[3];
            } else {
                if (ucumDefinitionPath == null) {
                    ucumDefinitionPath = PropertiesUtil.getProperties("ucum.essence.path");
                }
                conversionValue = PropertiesUtil.getProperties("conversion.value");
                sourceUnit = PropertiesUtil.getProperties("conversion.source.unit");
                destinationUnit = PropertiesUtil.getProperties("conversion.destination.unit");
            }

            if (codeValidation && candidateCode == null) {
                candidateCode = PropertiesUtil.getProperties("ucum.default.code");
            }
        }

        if (!codeValidation && !codeConversion) {
            logger.error("Both codeValidation and codeConversion are false. Enable at least one feature.");
            logger.info("Execution ended with an exception.");
            logger.info("");
            System.exit(1);
        }

        if (ucumDefinitionPath == null || ucumDefinitionPath.trim().isEmpty()) {
            logger.error("UCUM definition path is missing.");
            logger.info("Execution ended with an exception.");
            logger.info("");
            System.exit(1);
        }

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
            UcumService ucumSvc = null;
            // Instancie le service UCUM en chargeant le fichier d'essence UCUM.
            // Le constructeur prend ici le chemin (String) vers le fichier.
            // (Si ta version de la lib prend InputStream, adapte en conséquence.)
            ucumSvc = new UcumEssenceService(ucumDefinitionPath);

            logger.info("Chargement de la définition UCUM réussi.");
            if (codeValidation) {
                validateCode(ucumSvc, candidateCode);
            }

            if (codeConversion) {
                convertValue(ucumSvc, conversionValue, sourceUnit, destinationUnit);
            }

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

    private static void validateCode(UcumService ucumSvc, String candidateCode) throws UcumException {
        if (candidateCode == null || candidateCode.trim().isEmpty()) {
            throw new IllegalArgumentException("the argument is missing");
        }
        logger.info("Analyse du code UCUM : \"" + candidateCode + "\" ...");
        String analysisResult = ucumSvc.analyse(candidateCode);
        logger.info("Le code UCUM est VALIDE.");
        logger.info("Résultat d'analyse (brut) : " + analysisResult);
    }

    private static void convertValue(UcumService ucumSvc, String value, String sourceUnit, String destinationUnit) throws UcumException {
        if (value == null || value.trim().isEmpty() || sourceUnit == null || sourceUnit.trim().isEmpty()
                || destinationUnit == null || destinationUnit.trim().isEmpty()) {
            throw new IllegalArgumentException("conversion arguments are missing");
        }
        logger.info("Conversion demandée : " + value + " " + sourceUnit + " -> " + destinationUnit);
        Decimal converted = ucumSvc.convert(new Decimal(value), sourceUnit, destinationUnit);
        logger.info("Résultat de conversion : " + converted.asDecimal() + " " + destinationUnit);
    }
    
}
