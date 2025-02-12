package esprit.tn.main;

import esprit.tn.entities.User;
import esprit.tn.services.UserService;

public class Main {

    public static void main(String[] args) {

        DatabaseConnection.getInstance();
        UserService userService = new UserService();



    }
}


















 /*        User user = new User("user1", "user1", "99888777", "user1@gmail.com", "0000", "VISITEUR", false, false, 102030,0);
        String userEmail =user.getEmail();



        //////////////////  CREATE
       // userService.ajouter(user);  int iduser =userService.getoneByEmail(userEmail).getId() ;


        //////////////////  UPDATE

                  //  user.setName("karim");
                   // user.setSurname("karim");
                //    user.setTelephone("00111222");
                 //   userService.modifier(user,iduser);


        //////////////////  DELETE
      //  userService.supprimer(iduser);



        //////////////////  DISPLAY
        System.out.println("************************************************************* ");
        for (User u : userService.getall()) {
            System.out.println(u);
        }
        System.out.println("************************************************************* ");*/
