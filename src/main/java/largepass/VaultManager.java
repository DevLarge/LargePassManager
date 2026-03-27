package largepass;

import java.util.ArrayList;
import java.util.Collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VaultManager {

    private String masterPassDecrypted;

    public VaultManager() {

    }

    public static ObservableList<Entry> getPasswordList() {
        // read and decrypt from vault.db
        // temp entries
        ArrayList<Entry> list = new ArrayList<>();
        Entry pass1 = new Entry("oscar", "passord", "ntnu");
        Entry pass2 = new Entry("lea", "leapass", "ntnu");
        list.add(pass1);
        list.add(pass2);
        
        return FXCollections.observableList(list);
    }

    
    
}
