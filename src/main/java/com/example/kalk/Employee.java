package com.example.kalk;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Employee {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty niezakonczoneDzialania;
    private final ObservableList<Task> tasks;

    public Employee(int id, String name, int niezakonczoneDzialania) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.niezakonczoneDzialania = new SimpleIntegerProperty(niezakonczoneDzialania);
        this.tasks = FXCollections.observableArrayList();
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getNiezakonczoneDzialania() {
        return niezakonczoneDzialania.get();
    }

    public IntegerProperty niezakonczoneDzialaniaProperty() {
        return niezakonczoneDzialania;
    }


    public ObservableList<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return name.get();
    }

    public ObservableList<Task> getUnfinishedTasks() {
        return tasks;
    }

    public static class Task {
        private final IntegerProperty id;
        private final StringProperty category;
        private final StringProperty description;
        private final IntegerProperty idworkere;

        public Task(int id, String category, String description, int idworkere) {
            this.id = new SimpleIntegerProperty(id);
            this.category = new SimpleStringProperty(category);
            this.description = new SimpleStringProperty(description);
            this.idworkere = new SimpleIntegerProperty(idworkere);
        }

        public int getId() {
            return id.get();
        }

        public IntegerProperty idProperty() {
            return id;
        }
        public int getIdworkere(){return idworkere.get();}

        public String getCategory() {
            return category.get();
        }

        public StringProperty categoryProperty() {
            return category;
        }

        public String getDescription() {
            return description.get();
        }

        public StringProperty descriptionProperty() {
            return description;
        }
    }
}
