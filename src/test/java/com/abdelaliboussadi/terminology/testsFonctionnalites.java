package com.abdelaliboussadi.terminology;

import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumService;
import org.fhir.ucum.UcumModel;
import org.fhir.ucum.DefinedUnit;
import org.fhir.ucum.UcumException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class testsFonctionnalites {





    public static void main(String[] args) {


        // ------------------------------------------------------------
        // MODE TEST AUTOMATIQUE si le programme est lancé sans arguments
        // // ------------------------------------------------------------
        // if (args.length == 0) {
        //     System.out.println("Mode TEST activé (aucun argument fourni).");

        //     // 👉 ADAPTE CET CHEMIN selon où tu as placé ucum-essence.xml
        //     String defaultUcumPath = "C:\\\\dev\\\\git\\\\ucum_client\\\\ucum-client\\\\src\\\\resources\\\\ucum-essence.xml";

        //     // Exemple de mot-clé à rechercher
        //     String defaultKeyword = "gr";

        //     System.out.println("Utilisation des valeurs :");
        //     System.out.println("  Fichier UCUM  : " + defaultUcumPath);
        //     System.out.println("  Mot-clé testé : " + defaultKeyword);
        //     args = new String[]{ defaultUcumPath, defaultKeyword };
        // }
        // ------------------------------------------------------------

        // Vérification des arguments
        if (args.length != 2) {
            System.err.println("Usage: java -cp <jar:deps> com.example.ucumvalidator.UcumUnitSearch <path-to-ucum-essence.xml> <keyword>");
            System.exit(1);
        }

        String ucumDefinitionPath = args[0];
        String keywordRaw = args[1];

        // Normalisation pour recherche insensible à la casse
        String keyword = (keywordRaw == null) ? "" : keywordRaw.toLowerCase().trim();

        // Vérifie que le fichier existe avant d'essayer de le charger
        Path defPath = Paths.get(ucumDefinitionPath);
        if (!Files.exists(defPath)) {
            System.err.println("Le fichier UCUM est introuvable : " + ucumDefinitionPath);
            System.exit(2);
        }

        try {
            // Charge la définition UCUM
            UcumService ucumSvc = new UcumEssenceService(ucumDefinitionPath);

            // Récupère le modèle UCUM qui contient la liste des DefinedUnit
            UcumModel model = ucumSvc.getModel();
            List<DefinedUnit> units = model.getDefinedUnits();

            System.out.println("Recherche d'unités contenant : \"" + keywordRaw + "\"");
            boolean found = false;

            // Parcours toutes les unités définies
            for (DefinedUnit unit : units) {

                // Récupère le code (ex: "mg")
                String code = unit.getCode();
                String codeLower = (code == null) ? "" : code.toLowerCase();

                // Récupère la liste des noms/alias (ex: ["milligram", "milligramme"])
                List<String> namesList = unit.getNames();
                String namesJoinedLower = "";

                if (namesList != null && !namesList.isEmpty()) {
                    // Concatène tous les noms pour une recherche simple (séparateur ', ')
                    namesJoinedLower = String.join(", ", namesList).toLowerCase();
                }

                // Critère : le mot-clé est contenu dans le code OU dans l'un des noms
                if (codeLower.contains(keyword) || namesJoinedLower.contains(keyword)) {
                    found = true;
                    System.out.println("------------------------------------");
                    System.out.println("Code UCUM : " + (code == null ? "(aucun code)" : code));

                    if (namesList == null || namesList.isEmpty()) {
                        System.out.println("Noms : (aucun nom défini)");
                    } else {
                        // Affiche chaque nom séparé par " ; "
                        System.out.println("Noms : " + String.join(" ; ", namesList));
                    }
                }
            }

            if (!found) {
                System.out.println("❗ Aucune unité ne correspond au mot-clé : " + keywordRaw);
            }

        } catch (UcumException ucx) {
            // Erreurs provenant de la librairie UCUM (par ex. fichier mal formé)
            System.err.println("Erreur lors du chargement/analyse UCUM : " + ucx.getMessage());
            ucx.printStackTrace();
            System.exit(3);
        } catch (Exception ex) {
            // Autres erreurs (IO, runtime...)
            System.err.println("Erreur inattendue : " + ex.getMessage());
            ex.printStackTrace();
            System.exit(99);
        }
    }

}



