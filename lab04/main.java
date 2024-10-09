package lab04;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

class Main {
    private static List<Beer> beers = new ArrayList<Beer>();
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        
        while (true) {
            String[] cmd = reader.nextLine().split(" ");
        
            switch (cmd[0]) {
                case "add" -> add(cmd);
                case "list" -> list(cmd);
                case "search" -> search(cmd);
                case "find" -> find(cmd);
                case "delete" -> delete(cmd);
                case "save" -> save(cmd);
                case "load" -> load(cmd);
                case "exit" -> exit(reader);
                default -> System.out.println("Ismeretlen parancs: \t" + cmd[0]);
            }
        }
    }

    private static void exit(Scanner reader) {
        reader.close();
        System.exit(0);
    }

    private static void load(String[] cmd) {
        File file;
        if (cmd.length < 2) {
            file = new File("lab04\sorok.sor");
        } else {
            file = new File(cmd[1]);
        }
        
        if (!file.exists()) {
            System.out.println("Nem létezik ilye fájl: " + cmd[1]);
            return;
        }

        try {
            FileInputStream stream = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(stream);
            beers = (ArrayList<Beer>) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void save(String[] cmd) {
        File file;
        if (cmd.length < 2) {
            file = new File("lab04\sorok.sor");
        } else {
            file = new File(cmd[1]);
        }
        
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println("A file létrehozása");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Serializable serializable = new ArrayList(beers);
            FileOutputStream stream = new FileOutputStream(file);
            ObjectOutputStream output = new ObjectOutputStream(stream);
            output.writeObject(serializable);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void add(String[] cmd) {
        if (cmd.length < 4) return;
        beers.add(new Beer(cmd[1],cmd[2],Double.valueOf(cmd[3])));
    }

    private static Comparator SortBy(String type) {
        if (type.compareTo("name") == 0) {
            return new SortByName();
        } else if (type.compareTo("style") == 0) {
            return new SortByStyle();
        } else if (type.compareTo("strength") == 0) {
            return new SortByStrength();
        } else {
            return new SortByName();
        }
    }

    private static void list(String[] cmd) {
        if (cmd.length < 2) {
            for (Beer beer : beers) {
                System.out.println(beer);
            }
        } else {
            Collections.sort(beers, SortBy(cmd[1]));
            for (Beer beer : beers) {
                System.out.println(beer);
            }
        }
    }

    private static void search(String[] cmd) {
        if (cmd.length < 2) return;

        String key = cmd[1];
        for (Beer beer : beers) {
            if (beer.getName().compareTo(key) == 0) {
                System.out.println(beer);
            }
        }
    }

    private static void find(String[] cmd) {
        if (cmd.length < 2) return;

        String key = cmd[1];
        for (Beer beer : beers) {
            if (beer.getName().contains(key)) {
                System.out.println(beer);
            }
        }
    }

    private static void delete(String[] cmd) {
        if (cmd.length < 2) return;

        String key = cmd[1];
        Iterator<Beer> iter = beers.iterator();
        while (iter.hasNext()) {
            if (iter.next().getName().compareTo(key) == 0) {
                iter.remove();
            }
        }

    }
}
