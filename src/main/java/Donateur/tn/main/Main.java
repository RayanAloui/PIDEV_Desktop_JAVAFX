package Donateur.tn.main;

import Donateur.tn.entities.Dons;
import Donateur.tn.entities.donateur;
import Donateur.tn.services.DonateurService;
import Donateur.tn.services.DonsService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        DatabaseConnection.getInstance();

        DonateurService dt=new DonateurService();
        DonsService dn=new DonsService();
        donateur d1 = new donateur("yassine", "ben ali", "ahmed@gmail.com", 28488428,"06 Rue esprit");
        donateur d2 = new donateur("benlahssan", "trabelsi", "belahssan@gmail.com", 28488428,"06 Rue marsa");
        donateur d3 = new donateur("imed", "trabelsi", "imed@gmail.com", 28488428,"06 Rue marsa");
        donateur d4 = new donateur("leila", "trabelsi", "leila@gmail.com", 28488428,"06 Rue marsa");
        donateur d5 = new donateur("moncef", "ben ali", "moncef@gmail.com", 28488428,"06 Rue marsa");
        donateur d6 = new donateur("sofiene", "ben ali", "sofiene@gmail.com", 28488428,"06 Rue marsa");
        //Dons s1 = new Dons(1,200,,"materiel","je fait ce don pour des orphelins","en attente");

        //dt.ajouter(d1);
        //dt.ajouter(d2);
        //dt.ajouter(d3);
        //dt.ajouter(d6);
        //dt.getall();
        //dt.getone(2);
        //dt.modifier(d1);
        //dt.supprimer(7);
        //dt.getall();

        try {
            // Conversion de la date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateDon = dateFormat.parse("01/02/2025");

            // Création de l'objet Dons
            Dons s1 = new Dons(1, 200, dateDon, "materiel", "je fait ce don pour des orphelins", "en attente");
            Dons s2 = new Dons(2, 300, dateDon, "argent", "je fait ce don pour des orphelins", "effectué");
            Dons s3 = new Dons(2, 400, dateDon, "materiel", "je fait ce don pour des orphelins", "en attente");
            //dn.ajouter(s1);
            //dn.ajouter(s2);
            //dn.ajouter(s3);
            //dn.supprimer(3);
            //dn.supprimer(4);
            dn.getall();

            System.out.println("Don créé avec succès : " + s1);
        } catch (ParseException e) {
            System.out.println("Erreur lors de la conversion de la date : " + e.getMessage());
        }



    }







}