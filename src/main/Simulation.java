package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Simulation {
    //CHANGE ME
    
    //CHANGE ME
    static Integer groupSizes = 3;
    private static int counterOfStudents = 0;
    private static int numberOfGroups;


    public static void main(String[] args) throws IOException {

        LinkedList<String> participantsList = new LinkedList<>();
        HashMap<Integer,String> participantList = new HashMap<>();
        HashMap<Integer,String> activeParticipantList = new HashMap<>();

        File participants = new File("src/main/participantList.txt");
        Scanner sc = new Scanner(participants);


        int counter = 0;
        while(sc.hasNext()){
            String nameCandidate = sc.nextLine();
            String[] names = nameCandidate.split(" & ");
            for(String name:names){
                participantsList.add(name);
            }
        }


        GUI gui = new GUI();
        gui.startWindow.setSize(700,700);

        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);



        counter = 0;
        for(String student: participantsList){
            JCheckBox checkBox1 = new JCheckBox(student);
            int finalCounter = counter;
            checkBox1.addActionListener(e -> {
                Object source = e.getSource();
                String name = ((JCheckBox)source).getText();

                if (!(((JCheckBox) source).isSelected())) {
                    counterOfStudents--;
                    activeParticipantList.remove(finalCounter);
                } else {
                    counterOfStudents++;
                    activeParticipantList.put(finalCounter,name);
                }
            });

            panel.add(checkBox1);
            counter++;
        }


        Button startHashing= new Button("Done");
        startHashing.addActionListener(e-> {
            String[][] bamboozledGroups = hashing(counterOfStudents, activeParticipantList);
            gui.startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.startWindow.setVisible(false);
            gui.startWindow.dispose();
            gui.groupsWindow.setSize(700,900);
            JPanel groupPanel = new JPanel();

            int h=0;
            String nameTag = "<html><body>";
            String EOL = "<br>";
            String EOF = "</body></html>";

            //need to store it in a list and then rewrite in htm ´to store in a single Label
            for (String[] s:
            bamboozledGroups) {
                for (String name:
                     s) {
                    nameTag = (nameTag.equals(String.valueOf(Integer.MIN_VALUE)))?(""):(nameTag + name + EOL);
                }
                nameTag += EOL;
                nameTag += EOL;
                nameTag += EOL;
            }
            nameTag += EOF;
            JLabel nameTags = new JLabel(nameTag);
            groupPanel.add(nameTags);
            groupPanel.setVisible(true);
            gui.groupsWindow.add(groupPanel);
            gui.groupsWindow.setVisible(true);


        });
        panel.add(startHashing);

        gui.startWindow.getContentPane().add(panel);
        gui.startWindow.setVisible(true);

     }






    private static String[][] hashing(Integer numberOfParticipants, HashMap<Integer,String> actives) {

        int leftovers = numberOfParticipants%groupSizes;
        numberOfGroups = (numberOfParticipants-leftovers)/groupSizes;
        if(leftovers != 0){
            groupSizes++;
        }
        String[][] groupChoices = new String[numberOfGroups][groupSizes];
        for(int i = 0; i<numberOfGroups;i++){
            for(int j = 0; j<groupSizes;j++){
                groupChoices[i][j] = String.valueOf(Integer.MIN_VALUE);
            }
        }
        Random r = new Random();


        actives.forEach((a,b)->{

            int Class = (a+r.nextInt(0,100))%numberOfGroups;
           int k = 0;
           boolean ended = false;
           while(!ended){

               if((a<numberOfParticipants-leftovers)?((leftovers!=0)?(k<groupSizes-1):(k<groupSizes)):(k<groupSizes)){
                   if(groupChoices[Class][k].equals(String.valueOf(Integer.MIN_VALUE))){
                       groupChoices[Class][k] = b;
                       ended = true;
                   }
               }
               k++;
               if(k==groupSizes){
                   if(Class < numberOfGroups-1){
                       Class++;
                   } else {
                       Class = 0;
                   }
                   k = 0;
               }
            }
            ended = false;
        });



        return groupChoices;
    }
}
