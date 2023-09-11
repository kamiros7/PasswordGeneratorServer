package service;

import utils.Utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PasswordVerifierService implements PropertyChangeListener {

    private static final String FILE_NAME = "src/main/java/files/server_resources.txt"; // Replace with the path to your file
    private static List<String> currentPasswords = new ArrayList<>();

    public void start() {
        /*
        Remember that:
        0 -> salt
        1 -> user
        2 -> seed
         */

        //Read the constraints in file
        List<String> serviceResources = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                serviceResources.add(parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(serviceResources.isEmpty()) {
            System.out.println("File with constraints is empty, is necessary fill the file");
            System.exit(1);
        }

        PasswordGeneratorService service = new PasswordGeneratorService();
        service.addObservable("PASSWORD_UPDATED", this);
        service.startGeneratingPasswords(serviceResources.get(0), serviceResources.get(2));

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter your user: ");
            String user = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            if(user.equals(serviceResources.get(1)) && currentPasswords.contains(password)) {
                System.out.println("Chave válida");

                //Obtain the position of the password to remove the password used at this moment.
                //and the next passwords that can be obtained with successive hash
                int index = currentPasswords.indexOf(password);
                for(int i = currentPasswords.size() - 1; i >= index; i--) {
                    currentPasswords.remove(i);
                }
            } else {
                System.out.println("Chave inválida");
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("PASSWORD_UPDATED".equals(evt.getPropertyName())) {
            System.out.println("passwords updated in propertyChange()");
            List<String> passwords = (List<String>) evt.getNewValue();
            if (passwords != null) {
                currentPasswords.clear();
                currentPasswords = passwords;
            }
        }
    }
}
