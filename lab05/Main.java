package lab05;

import java.io.*;
import java.util.*;

public class Main {
    private static Boolean isRunning = true;
    private static List<Beer> beers = new ArrayList<Beer>();
    private static Map<String, Command> commands = new HashMap<>();
    private static Map<String, Comparator<Beer>> comps = new HashMap<String,Comparator<Beer>>();
    private static ArrayList<String> lparams = new ArrayList<>();
    
    static {
        lparams.add("name");
        lparams.add("style");
        lparams.add("strength");
        commands.put("add", Main::add);
        commands.put("list", Main::list);
        commands.put("search", Main::search);
        commands.put("find", Main::find);
        commands.put("delete", Main::delete);
        commands.put("load", Main::load);
        commands.put("save", Main::save);

        comps.put("name",
            (b1,b2) -> b1.getName().compareTo(b2.getName())
        );
        comps.put("style",
            (b1,b2) -> b1.getStyle().compareTo(b2.getStyle())
        );
        comps.put("strength",
            (b1,b2) -> b1.getStrength().compareTo(b2.getStrength())
        );
    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);


        
        while (isRunning) {
            String[] cmd = reader.nextLine().split(" ");
        
            if (commands.get(cmd[0]) == null) {
                if (cmd[0].compareTo("exit") == 0) {
                    exit();
                } else {
                    System.out.println("Ismeretlen parancs: \t" + cmd[0]);
                }
            } else {
                commands.get(cmd[0]).execute(cmd);
            }
        }

        reader.close();
        System.exit(0);
    }

    private static void exit() {
        isRunning = false;
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

    private static void modifyList(String name) {
        if (lparams.contains(name)) {
            lparams.remove(name); // Töröljük a megadott nevet
        }
        lparams.add(0, name); // Helyezzük a lista elejére
    }

    private static void list(String[] cmd) {

        if (cmd.length == 2) {
            modifyList(cmd[1]);
        } else { 
            System.out.println("Undefined behaviour!");
            return;
        }

        Collections.sort(beers,
        new CComparator<>(
            comps.get(lparams.get(0)),
            comps.get(lparams.get(1))
        ).then(comps.get(lparams.get(2))));

        for (Beer beer : beers) {
            System.out.println(beer);
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
