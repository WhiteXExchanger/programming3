package lab_03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

class Main
{
    static File wd;
    public static void main(String []args) throws IOException {
        wd = new File(System.getProperty("user.dir"));

        Scanner input = new Scanner(System.in);

        while (true) {
            // Input feldolgozás
            String[] cmd = input.nextLine().split(" ");

            // Parancs futtatás
            switch (cmd[0]) {
                case "hello":
                    hello(cmd);
                    break;
            
                case "pwd":
                    pwd(cmd);
                    break;

                case "cd":
                    cd(cmd);
                    break;

                case "ls":
                    ls(cmd);
                    break;

                case "rm":
                    rm(cmd);
                    break;

                case "cat":
                    cat(cmd);
                    break;
                
                case "mv":
                    mv(cmd);
                    break;
                
                case "wc":
                    wc(cmd);
                    break;

                case "grep":
                    grep(cmd);
                    break;

                case "exit":
                    input.close();
                    exit(cmd);
                    break;

                default:
                    System.out.println("Nincs ilyen parancs!");
            }
        }
    }

    protected static void rm(String[] cmd) {
        if (cmd.length < 2) return;
        File file = new File(wd, cmd[1]);
        if (file.exists())
            if (file.delete()) System.out.println("Valami hiba történt átnevezés közben!");
    }

    protected static void mv(String[] cmd) {
        if (cmd.length < 3) return;
        File oldFile = new File(wd, cmd[1]);
        File newFile = new File(wd, cmd[2]);
        if (oldFile.renameTo(newFile)) {
            System.out.println("megy");
        }
        else {
            System.out.println("Valami hiba történt átnevezés közben!");
        }
    }

    protected static void cat(String[] cmd) {
        if (cmd.length < 2) return;
        File file = new File(wd, cmd[1]);
        if (!file.exists())
            System.out.println("A file nem létezik!");
        
        Scanner input = new Scanner(System.in);
        try {
            input = new Scanner(file);    
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (input.hasNextLine()) {
            System.out.println(input.nextLine());
        }
    }

    protected static void wc(String[] cmd) {
        if (cmd.length < 2) return;
        File file = new File(wd, cmd[1]);
        if (!file.exists())
            System.out.println("A file nem létezik!");
        
        Scanner input = new Scanner(System.in);
        try {
            input = new Scanner(file);    
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int lineCount = 0, wordsCount = 0, charCount = 0;
        while (input.hasNextLine()) {
            String[] words = input.nextLine().split(" ");

            for (String string : words) {
                charCount+=string.length();
            }
            wordsCount+=words.length;
            lineCount++;
        }

        System.out.println(file.getName() + " fileban " + lineCount + " sor " + wordsCount + " szó " + charCount + " betu van!");
    }

    protected static void cd(String[] cmd) {
        File folder;
        if
            (cmd[1].equals("..")) folder = wd.getParentFile();
        else
            folder = new File(wd, cmd[1]);

        if (folder.exists())
            wd = folder;
        else
            System.out.println("Nem létezik ez a file/folder");
    }

    protected static void ls(String[] cmd) {
        if (cmd.length == 1) {
            for (File file : wd.listFiles()) {
                System.out.println(file.getName());
            }
        }
        else if (cmd[1].equals("-l")) {
            for (File file : wd.listFiles()) {
                String type;
                if (file.isDirectory())
                    type = "d";
                else
                    type = "f";
                System.out.println(file.getName() + " " + file.length() + " " + type);
            }
        }
        
    }

    protected static void grep(String[] cmd) {
        if (cmd.length < 3) return;

        String input = null;

        File file = new File(wd, input);
        if (!file.exists())
            System.out.println("A file nem létezik!");
        
        Scanner inputScanner = new Scanner(System.in);
        try {
            inputScanner = new Scanner(file);    
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            String[] split = line.split(" ");
            
            for (String string : split) {
                if (string.matches(cmd[1])) {
                    System.out.println(line);
                    break;
                }
            }
        }

        inputScanner.close();
    }

    protected static void pwd(String[] cmd) {
        System.out.println(wd.getPath() + "\t" + wd.list().length + " files");
    }

    protected static void hello(String[] cmd) {
        System.out.println("Hello World!");
    }

    protected static void exit(String[] cmd) {
        System.exit(0);
    }
}