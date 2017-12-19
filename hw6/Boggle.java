import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;


public class Boggle {
    private Tries boggleTries;
    private List<Character> board;
    private int width = 4;
    private int height = 4;
    private int numOfWords = 1;

    public Boggle(int num, String bdName) {
        numOfWords = num;
        boggleTries = readDict("words");
        board = readBoard(bdName);
    }

    public Boggle(int num, int w, int h, String dictPath, String bdName) {
        width = w;
        height = h;
        numOfWords = num;
        boggleTries = readDict(dictPath);
        board = readBoard(bdName);
    }

    private List<Character> readBoard(String boardName) {
        List<Character> bd = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(boardName))) {
            while (scanner.hasNext()) {
                String s = scanner.next();
                for (int i = 0; i < s.length(); i++) {
                    bd.add(s.charAt(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bd;
    }

    private Tries readDict(String dictFile) {
        Tries t = new Tries();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dictFile));
            for (String line : lines) {
                t.put(line);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return t;
    }

    private void solver(String word, int index, List<String> result, List<Integer> visited) {

        if (boggleTries.keysWithPrefix(word).isEmpty()) {return;}
        if (boggleTries.keysThatMatch(word)) {
            if (!result.contains(word)) {
                result.add(word);
            }
        }

        Map<Integer, Character> adj = getAdjChar(index);

        for (int i : adj.keySet()) {
            List<Integer> tmp = new ArrayList<>(visited);
            if (tmp.contains(i)) {
                continue;
            } else {
                tmp.add(i);
                char c = adj.get(i);
                solver(word + c, i, result, tmp);
            }
        }
    }

    public void play() {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < board.size(); i++) {
            List<Integer> v = new ArrayList<>();
            v.add(i);
            char c = board.get(i);
            solver(Character.toString(c), i, result, v);
        }

        /*
        result.sort(new Comparator<String>() {
            @Override
            public int compare(String w1, String w2) {
                int dist1 = w1.length();
                int dist2 = w2.length();

                return dist2 - dist1;
            }
        });*/

        result.sort((w1, w2) -> w2.length() - w1.length());  // Compare using lambda

        for (int i = 0; i < numOfWords; i++) {
            try {
                System.out.println(result.get(i));
            } catch (Exception e) {

            }
        }
    }

    private Map<Integer, Character> getAdjChar(int index) {
        int[] adjIndex = new int[8];
        Map<Integer, Character> adjChar = new HashMap<>();

        adjIndex[0] = index - 1;    // left
        adjIndex[1] = index + 1;    // right
        adjIndex[2] = index - width;    // up
        adjIndex[3] = index + width;    // down
        adjIndex[4] = index - width - 1;    // upleft
        adjIndex[5] = index - width + 1;    // upright
        adjIndex[6] = index + width - 1;    // downleft
        adjIndex[7] = index + width + 1;    // downright

        if (index % width == 0) {  // left corner
            adjIndex[0] = -1;
            adjIndex[4] = -1;
            adjIndex[6] = -1;
        }

        if (index % width == width - 1) {  // right corner
            adjIndex[1] = -1;
            adjIndex[5] = -1;
            adjIndex[7] = -1;
        }

        for (int i : adjIndex) {
            if (i < 0 || i > width * height - 1) {
                continue;
            }
            adjChar.put(i, board.get(i));
        }

        return adjChar;
    }

    public static void main(String[] args) {
        int num = 1;
        int w = 4;
        int h = 4;
        String dic = "words";
        String bgName = "testBoggle";

        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-k":
                    num = Integer.parseInt(args[i+1]);
                    break;

                case "-n":
                    w = Integer.parseInt(args[i+1]);
                    break;

                case "-m":
                    h = Integer.parseInt(args[i+1]);
                    break;

                case "-d":
                    dic = args[i+1];
                    break;

                case "-r":
                    bgName = args[i+1];
                    break;
            }
        }

        Boggle bg = new Boggle(num, w, h, dic, bgName);
        bg.play();

    }
}